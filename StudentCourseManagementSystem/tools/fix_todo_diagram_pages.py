from __future__ import annotations

import copy
import zipfile
from pathlib import Path
from xml.etree import ElementTree as ET


DOCX = Path(r"C:\Users\Admin\Downloads\Project_Report_Pratik_Tejas.docx")
W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main"
W = f"{{{W_NS}}}"

ET.register_namespace("w", W_NS)
ET.register_namespace("r", "http://schemas.openxmlformats.org/officeDocument/2006/relationships")
ET.register_namespace("wp", "http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing")
ET.register_namespace("a", "http://schemas.openxmlformats.org/drawingml/2006/main")
ET.register_namespace("pic", "http://schemas.openxmlformats.org/drawingml/2006/picture")
ET.register_namespace("w14", "http://schemas.microsoft.com/office/word/2010/wordml")
ET.register_namespace("w15", "http://schemas.microsoft.com/office/word/2012/wordml")
ET.register_namespace("w16cid", "http://schemas.microsoft.com/office/word/2016/wordml/cid")


def text_of(node: ET.Element) -> str:
    return "".join(t.text or "" for t in node.findall(f".//{W}t")).strip()


def set_text(node: ET.Element, value: str) -> None:
    texts = node.findall(f".//{W}t")
    if not texts:
        return
    texts[0].text = value
    for extra in texts[1:]:
        extra.text = ""


def para(value: str, bold: bool = False) -> ET.Element:
    p = ET.Element(f"{W}p")
    r = ET.SubElement(p, f"{W}r")
    if bold:
        rpr = ET.SubElement(r, f"{W}rPr")
        ET.SubElement(rpr, f"{W}b")
    t = ET.SubElement(r, f"{W}t")
    t.set(f"{W}space", "preserve")
    t.text = value
    return p


def replace_between(body: ET.Element, start_text: str, end_text: str, replacement: list[ET.Element]) -> None:
    children = list(body)
    start = end = None
    for index, child in enumerate(children):
        text = text_of(child)
        if start is None and text == start_text:
            start = index
        elif start is not None and text == end_text:
            end = index
            break
    if start is None or end is None:
        return
    for child in children[start:end]:
        body.remove(child)
    for offset, node in enumerate(replacement):
        body.insert(start + offset, node)


def main() -> None:
    with zipfile.ZipFile(DOCX, "r") as zin:
        root = ET.fromstring(zin.read("word/document.xml"))
        body = root.find(f"{W}body")
        if body is None:
            raise RuntimeError("Document body not found")

        activity_section = [
            para("3.3.5 Activity Diagram", bold=True),
            para("Add Student Activity Diagram:", bold=True),
            para("This activity shows the process of entering student details, validating required fields, saving the record in CSV storage, and refreshing the student table."),
            para("Add Course Activity Diagram:", bold=True),
            para("This activity shows how course details such as course ID, title, credits, faculty, and capacity are entered, validated, and saved in the system."),
            para("Enrollment Activity Diagram:", bold=True),
            para("This activity explains how a student is selected, a course is selected, marks and attendance are entered, and the grade is generated automatically."),
            para("Update Performance Activity Diagram:", bold=True),
            para("This activity shows how existing marks and attendance are modified and how the updated grade and dashboard statistics are refreshed."),
        ]
        replace_between(body, "3.3.5 Activity Diagram", "Sequence Diagram", activity_section)

        sequence_section = [
            para("Sequence Diagram", bold=True),
            para("Add Student Sequence Diagram", bold=True),
            para("User enters student details. The Swing form sends data to CampusService. CampusService validates the student ID and semester, then DataStore saves the updated students.csv file."),
            para("Add Course Sequence Diagram:", bold=True),
            para("User enters course details. CampusService checks course ID, credits, and capacity. After successful validation, the course record is stored and the course table is refreshed."),
            para("Add Enrollment Sequence Diagram:", bold=True),
            para("User selects a student and course, enters marks and attendance, and submits the form. CampusService checks duplicate enrollment and course capacity, creates the enrollment, and saves it in enrollments.csv."),
            para("Update Marks and Attendance Sequence Diagram:", bold=True),
            para("User selects an enrollment record and updates marks or attendance. The service layer validates values between 0 and 100, recalculates grade, saves the record, and refreshes dashboard statistics."),
        ]
        replace_between(body, "Sequence Diagram", "Component Diagram:", sequence_section)

        replacements = {
            "Add Task Activity Diagram:": "Add Student Activity Diagram:",
            "Delete Task Activity Diagram:": "Delete Student/Course Activity Diagram:",
            "Mark Task as Completed Activity Diagram:": "Enrollment Activity Diagram:",
            "Change Priority Activity Diagram:": "Update Marks and Attendance Activity Diagram:",
            "Add Task Sequence Diagram": "Add Student Sequence Diagram",
            "Delete Task Sequence Diagram:": "Delete Record Sequence Diagram:",
            "3.5.3 Mark Task as Completed Sequence Diagram:": "3.5.3 Add Enrollment Sequence Diagram:",
            "3.5.4 Change Priority Sequence Diagram:": "3.5.4 Update Marks and Attendance Sequence Diagram:",
        }
        for p in body.findall(f".//{W}p"):
            current = text_of(p)
            if current in replacements:
                set_text(p, replacements[current])

        new_xml = ET.tostring(root, encoding="utf-8", xml_declaration=True)
        temp = DOCX.with_suffix(".diagram-fixed.docx")
        with zipfile.ZipFile(temp, "w", zipfile.ZIP_DEFLATED) as zout:
            for item in zin.infolist():
                data = zin.read(item.filename)
                if item.filename == "word/document.xml":
                    data = new_xml
                zout.writestr(copy.copy(item), data)

    temp.replace(DOCX)
    print(DOCX)


if __name__ == "__main__":
    main()
