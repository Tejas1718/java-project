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


def child_text(element: ET.Element) -> str:
    return "".join(t.text or "" for t in element.findall(f".//{W}t")).strip()


def paragraph(text: str, bold: bool = False) -> ET.Element:
    p = ET.Element(f"{W}p")
    r = ET.SubElement(p, f"{W}r")
    if bold:
        rpr = ET.SubElement(r, f"{W}rPr")
        ET.SubElement(rpr, f"{W}b")
    t = ET.SubElement(r, f"{W}t")
    t.set(f"{W}space", "preserve")
    t.text = text
    return p


def main() -> None:
    with zipfile.ZipFile(DOCX, "r") as zin:
        root = ET.fromstring(zin.read("word/document.xml"))
        body = root.find(f"{W}body")
        if body is None:
            raise RuntimeError("Word document body not found")

        children = list(body)
        start = end = None
        for index, child in enumerate(children):
            text = child_text(child)
            if start is None and "Sample Input and Output Screens:" in text:
                start = index
            elif start is not None and text == "CHAPTER 4: CODING SAMPLE CODE":
                end = index
                break

        if start is None or end is None:
            raise RuntimeError("Sample screen section not found")

        replacement = [
            paragraph("Sample Input and Output Screens:", bold=True),
            paragraph("Dashboard Screen:", bold=True),
            paragraph(
                "The dashboard displays total students, total courses, total enrollments, average marks, "
                "average attendance, and high performer count. It gives a quick summary of academic data "
                "after records are loaded from CSV files."
            ),
            paragraph("Student Management Screen:", bold=True),
            paragraph(
                "This screen is used to add, update, delete, and view student records. The user enters "
                "student ID, name, department, semester, and email address. All student records are shown "
                "in a table format for easy checking."
            ),
            paragraph("Course Management Screen:", bold=True),
            paragraph(
                "This screen maintains course details such as course ID, course title, credits, faculty "
                "name, and course capacity. It helps the department organize course-wise academic data."
            ),
            paragraph("Enrollment Management Screen:", bold=True),
            paragraph(
                "This screen connects students with courses. The user selects a student and course, enters "
                "marks and attendance, and the system calculates the grade automatically. Enrollment data "
                "is also displayed in tabular form."
            ),
            paragraph("CSV Data Storage Output:", bold=True),
            paragraph(
                "The system stores records in students.csv, courses.csv, and enrollments.csv files. This "
                "makes the application lightweight and allows data to remain available after closing and "
                "reopening the software."
            ),
        ]

        for child in children[start:end]:
            body.remove(child)
        for offset, node in enumerate(replacement):
            body.insert(start + offset, node)

        new_xml = ET.tostring(root, encoding="utf-8", xml_declaration=True)

        temp = DOCX.with_suffix(".fixed.docx")
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
