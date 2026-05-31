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


def main() -> None:
    replacements = {
        "Task Data Structure Table :": "Academic Data Structure Table :",
        "Task description entered by user": "Student, course, and enrollment details entered by user",
        "Task creation time": "Record creation time",
        "Change Priority": "Update Record",
        "Add Task": "Add Student",
        "Delete Task": "Delete Record",
        "Complete Task": "Add Enrollment",
        "Task added to list": "Record added to table",
        "Task removed": "Record removed",
        "Task marked ✔️": "Enrollment saved with grade",
        "Task Input": "Record Input",
        "Task operations": "Student, course, and enrollment operations",
        "Task added": "Record added",
    }

    with zipfile.ZipFile(DOCX, "r") as zin:
        root = ET.fromstring(zin.read("word/document.xml"))
        for t in root.findall(f".//{W}t"):
            if t.text:
                value = t.text
                for old, new in replacements.items():
                    value = value.replace(old, new)
                t.text = value

        new_xml = ET.tostring(root, encoding="utf-8", xml_declaration=True)
        temp = DOCX.with_suffix(".terms-fixed.docx")
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
