from __future__ import annotations

import copy
import zipfile
from pathlib import Path
from xml.etree import ElementTree as ET


TEMPLATE = Path(r"C:\Users\Admin\Downloads\documentation of To-Do.docx")
OUTPUT = Path("StudentCourseManagementSystem_Project_Report_Pratik_Tejas.docx")

W = "{http://schemas.openxmlformats.org/wordprocessingml/2006/main}"

ET.register_namespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main")
ET.register_namespace("r", "http://schemas.openxmlformats.org/officeDocument/2006/relationships")
ET.register_namespace("wp", "http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing")
ET.register_namespace("a", "http://schemas.openxmlformats.org/drawingml/2006/main")
ET.register_namespace("pic", "http://schemas.openxmlformats.org/drawingml/2006/picture")
ET.register_namespace("w14", "http://schemas.microsoft.com/office/word/2010/wordml")
ET.register_namespace("w15", "http://schemas.microsoft.com/office/word/2012/wordml")
ET.register_namespace("w16cid", "http://schemas.microsoft.com/office/word/2016/wordml/cid")


def paragraph_text(paragraph: ET.Element) -> str:
    return "".join(t.text or "" for t in paragraph.findall(f".//{W}t"))


def set_paragraph_text(paragraph: ET.Element, text: str) -> None:
    text_nodes = paragraph.findall(f".//{W}t")
    if not text_nodes:
        return
    text_nodes[0].text = text
    for node in text_nodes[1:]:
        node.text = ""


def non_empty_paragraphs(root: ET.Element) -> list[ET.Element]:
    return [p for p in root.findall(f".//{W}p") if paragraph_text(p).strip()]


def main() -> None:
    if not TEMPLATE.exists():
        raise FileNotFoundError(TEMPLATE)

    with zipfile.ZipFile(TEMPLATE, "r") as zin:
        document_xml = zin.read("word/document.xml")
        root = ET.fromstring(document_xml)
        paragraphs = non_empty_paragraphs(root)

        replacements: dict[int, str] = {
            3: "Student Course & Performance Management System",
            9: "Pratik Pravin Dalal (Roll No. 145)\nTejas Dipak Yambal (Roll No. 69)",
            19: "This is to certify that the project “Student Course & Performance Management System” is being submitted for the award",
            20: "of the degree of Master of Computer Application by Pratik Pravin Dalal and Tejas Dipak Yambal to Shri Swami Samarth Institute",
            22: "This is the result of the original work completed by Pratik Pravin Dalal and Tejas Dipak Yambal under the",
            26: "We wish them all the best for their future endeavors.",
            30: "We hereby declare that the research work presented in the Project Report entitled “Student Course & Performance Management System”",
            41: "Pratik Pravin Dalal (Roll No. 145)\nTejas Dipak Yambal (Roll No. 69)",
            48: "Pratik Pravin Dalal (Roll No. 145)\nTejas Dipak Yambal (Roll No. 69)",
            32: "and Management of Savitribai Phule Pune University, Pune is an outcome of our own efforts and a",
            34: "We also declare that this Project Report or any part therein has not been previously submitted by us for the",
            37: "We further declare that the material obtained from other sources has been duly acknowledged.",
            139: "The Student Course & Performance Management System is a user-friendly desktop-based academic management application developed using the Java programming language and Java Swing framework. The primary objective of this project is to help colleges manage student records, course details, enrollments, marks, attendance, and grades through an interactive graphical user interface. Manual academic record handling can become time-consuming and error-prone, so this application provides a simple digital solution for organizing academic information.",
            140: "The application allows users to add, update, delete, and view student records and course records. It also allows enrollment of students into courses, recording of marks and attendance, and automatic grade generation based on marks. A dashboard is included to show total students, total courses, total enrollments, average marks, average attendance, and high performers. The interface is designed using Java Swing components such as JFrame, JPanel, JButton, JLabel, JTable, JTextField, JComboBox, JScrollPane, and tabbed panels.",
            141: "The project mainly focuses on implementing core Java concepts such as object-oriented programming, event-driven programming, GUI design, collections, validation, and file handling. User actions are handled through event listeners, and the records are saved locally in CSV files so the application remains lightweight and easy to run without a complex database setup.",
            142: "This project is highly beneficial for educational institutions and students who need a simple system for maintaining academic information. It improves record organization by keeping students, courses, enrollments, marks, attendance, and dashboard statistics in one place. The project also serves as a useful mini project for understanding Java Swing desktop application development. Future enhancements can include login authentication, MySQL database connectivity, search filters, report export, and analytics charts.",
            145: "In the existing system, many academic records are maintained manually in registers, spreadsheets, or separate files. These traditional methods are time-consuming and less efficient because student details, course information, marks, and attendance can become difficult to update, search, and verify. Manual systems also increase the chances of duplicate records and calculation mistakes.",
            146: "Manual academic management does not provide a single dashboard view, automatic grade calculation, or quick access to enrollment details. Users may face difficulty in finding which student is enrolled in which course, checking performance, and maintaining updated attendance or marks records.",
            147: "Therefore, traditional record-keeping methods are not sufficient for maintaining student course and performance details in an organized and reliable manner.",
            149: "The Student Course & Performance Management System is developed to overcome the limitations of manual academic record handling. Colleges and departments need to maintain student details, course details, enrollments, marks, attendance, and performance information regularly. Managing all these records manually can lead to errors, delay, and confusion.",
            150: "The proposed system provides an easy and efficient way to maintain academic records using a graphical user interface developed in Java Swing. The application helps users store, update, delete, and view records in a structured manner with local CSV-based data persistence.",
            152: "1. To reduce the complexity of manual student and course record management.",
            153: "2. To provide an easy-to-use and interactive desktop application.",
            154: "3. To manage enrollments, marks, attendance, and grades effectively.",
            155: "4. To improve academic record organization and performance monitoring.",
            156: "The application is especially useful for students, faculty members, academic departments, and beginners learning Java GUI programming, event handling, file handling, and object-oriented concepts.",
            158: "Student Management",
            159: "The system allows users to create, store, update, delete, and manage student records through a simple graphical user interface. Student details such as ID, name, department, semester, and email can be maintained digitally.",
            160: "Course Management",
            161: "The application provides course record management with fields such as course ID, course title, credits, faculty name, and course capacity.",
            162: "It helps maintain organized course information and supports enrollment planning.",
            163: "Enrollment Tracking",
            164: "Users can enroll students into selected courses and maintain marks and attendance for each enrollment record.",
            165: "This helps in tracking student academic progress course-wise.",
            166: "Automatic Grade Generation",
            167: "The system calculates grades automatically based on marks entered for the enrollment.",
            168: "This reduces manual calculation work and improves accuracy.",
            169: "User-Friendly Graphical Interface",
            170: "The project is developed using Java Swing components, providing an interactive and",
            171: "easy-to-use GUI suitable for academic record management.",
            172: "Dashboard and Performance Summary",
            173: "The application displays dashboard statistics such as total students, courses, enrollments, average marks, average attendance, and high performers.",
            174: "Efficient Academic Data Management",
            175: "The system helps academic users organize student and course information effectively, reducing confusion and improving productivity.",
            176: "Educational and Learning Purpose",
            177: "The project can be used as an academic mini project to understand Java programming concepts such as Swing, event handling, collections, file handling, object-oriented programming, and GUI development.",
            178: "Offline Desktop Application",
            179: "The application works completely offline with CSV file storage, making it lightweight and easy to use on any computer system with Java installed.",
            180: "Performance Monitoring",
            181: "The application helps monitor student performance by maintaining marks, attendance, grades, and top performer details.",
            192: "JDK: Version 17 or above",
            196: "Database: CSV File Storage",
            213: "Java is the core programming language used to develop the Student Course & Performance Management System. It is a",
            216: "complete functionality and logic of the system, including student management, course management,",
            217: "enrollment handling, marks entry, attendance tracking, and grade calculation. It supports object-oriented programming concepts such as classes,",
            224: "interactive user interface of the Student Course & Performance Management System. Swing is a part of Java Foundation",
            226: "applications. In this project, Swing components such as JFrame, JButton, JTextField, JLabel,",
            227: "JTable, JComboBox, JScrollPane, JPanel, and tabbed panes are used to create the dashboard, forms,",
            228: "record tables, buttons, and input fields. Swing helps in creating an attractive,",
            235: "development and works together with Swing components. In the Student Course & Performance Management System,",
            237: "arranging GUI components properly inside the application window. It is also used for setting",
            243: "The proposed system is a desktop-based Student Course & Performance Management System developed using Java programming language with Java Swing and AWT technologies. The main objective of the system is to provide an efficient, simple, and user-friendly solution for managing student academic records digitally. The application is designed to replace manual record keeping by providing an organized and interactive graphical interface.",
            244: "The proposed system enables users to maintain student records, course records, and enrollment records. Users can add, update, delete, and view students and courses, enroll students into courses, record marks and attendance, and automatically calculate grades. The dashboard displays academic summaries such as total students, total courses, total enrollments, average marks, average attendance, and high performers.",
            245: "The graphical user interface of the application is developed using Java Swing components such as JFrame, JButton, JTextField, JLabel, JTable, JComboBox, JScrollPane, JPanel, and tabbed panes. These components provide a smooth and interactive experience for users. Event handling mechanisms are implemented to perform operations based on user actions like button clicks, form submission, and table selection.",
            246: "The proposed system is lightweight and uses local CSV files for data storage, making it easy to run on any computer system with Java support. The application reduces paperwork, minimizes calculation errors, and improves academic record organization.",
            247: "The proposed system is especially useful for students, faculty members, academic departments, and beginners learning Java desktop application development. It can also be used as an educational mini project for understanding Java GUI programming, object-oriented concepts, collections, file handling, and event-driven application development.",
            248: "The system is designed with future scalability in mind. Additional features such as user login authentication, database connectivity, advanced search, report generation, charts, backup, and role-based access can be integrated in future versions to enhance the functionality and performance of the application further.",
            251: "The Student Course & Performance Management System is technically feasible because it is developed using Java, Java Swing, AWT, collections, and CSV file handling, which are widely available and easy to implement. The required development tools such as Java Development Kit (JDK) and IDEs are easily accessible and support desktop application development efficiently.",
            252: "The system can run on standard computer systems with minimum hardware requirements. Since the application is standalone and offline-based, there is no need for internet connectivity or server setup. CSV file storage keeps the project simple, reliable, and easy to maintain.",
            254: "The proposed system is economically feasible because the development cost is very low. The technologies used in the project, such as Java and Java Swing, are freely available. No additional licensing cost or expensive software is required for development and execution.",
            255: "The application also does not require external servers or cloud services, which reduces operational and maintenance expenses. Since the project can be developed using existing computer systems and free development tools, the total cost of implementation remains affordable for students and educational institutions.",
            257: "The Student Course & Performance Management System is operationally feasible because it is simple, user-friendly, and easy to operate. The graphical user interface allows users to interact with the system easily without requiring advanced technical knowledge. Users can quickly manage students, courses, enrollments, marks, attendance, and grades through simple form and button operations.",
            258: "The system improves academic record organization while reducing manual effort. It can be effectively used by academic departments and students for maintaining course and performance details. Since the application works offline and requires minimal setup, users can operate it conveniently on personal computers.",
            260: "To Develop a User-Friendly Academic Management System",
            261: "The main objective of the proposed system is to develop a simple, interactive, and user-friendly desktop application that helps users manage student course and performance records through a graphical user interface.",
            262: "To Maintain Student Records Efficiently",
            263: "The system aims to help users organize and maintain student details such as student ID, name, department, semester, and email in a systematic manner.",
            264: "To Provide Course and Enrollment Management",
            265: "The application allows users to manage course details and enroll students into selected courses with marks and attendance information.",
            266: "To Reduce Manual Effort",
            267: "The proposed system replaces paper-based academic record handling, reducing manual work and minimizing the chances of losing important student or course information.",
            268: "To Improve Performance Tracking",
            269: "By maintaining marks, attendance, grades, and dashboard statistics, the system helps monitor student academic performance effectively.",
            270: "To Track Enrolled Students and Courses",
            271: "The application provides features to view which student is enrolled in which course and maintain related performance details.",
            272: "To Provide Flexibility in Record Modification",
            273: "The system allows users to update existing student, course, and enrollment records according to changing academic requirements.",
            274: "To Develop an Offline Desktop Application",
            275: "The application is designed to work without internet connectivity, making it lightweight, fast, and accessible on any computer system with Java support.",
            276: "To Implement Java GUI and File Handling Concepts",
            277: "The project aims to demonstrate practical implementation of Java Swing, AWT, event handling, collections, CSV file handling, and object-oriented programming concepts in desktop application development.",
            280: "Students can use the Student Course & Performance Management System to understand course enrollment, marks, attendance, and performance details. The system helps organize academic information efficiently.",
            281: "2.Faculty Members",
            282: "Faculty members can use the application to maintain student details, course information, enrollment data, marks, attendance, and grades in one place.",
            283: "3.Academic Departments",
            284: "Departments can use the system to manage course-wise student data, faculty course assignment, and academic performance records.",
            285: "4.Administrators",
            286: "Administrators can use the application for maintaining basic academic records and monitoring overall dashboard statistics.",
            287: "5.Project Evaluators",
            288: "Project evaluators can use the application to verify Java Swing GUI design, event handling, file storage, validations, and object-oriented implementation.",
            289: "6.Small Institutes",
            290: "Small institutes can use the system as a lightweight local application for maintaining student and course information.",
            291: "7.Beginners Learning Java",
            292: "The application can also be used by students and beginners who are learning Java programming, GUI development, collections, and file handling concepts through practical implementation.",
            293: "8.Educational Institutions",
            294: "Colleges and training institutes can use this project as a mini project or academic reference for teaching Java Swing and desktop application development concepts.",
            295: "9.Home Practice Users",
            296: "Users practicing Java can run the system locally to understand model classes, service logic, persistence, and Swing UI integration.",
            780: "CHAPTER 5: TESTING",
            782: "These testing techniques are used to ensure that the Student Course & Performance Management System works correctly, efficiently, and without errors. They help verify system functionality, improve reliability, check user interaction, and ensure smooth performance of all application features.",
            784: "Unit testing is used to test individual modules or functions of the application separately. Each feature such as Add Student, Add Course, Add Enrollment, Update Records, and Delete Records is tested independently to verify correct functionality.",
            801: "Test cases for Student, Course, and Enrollment Management Module:",
            809: "Add a new student",
            810: "Enter student details and click Add",
            811: "Enter student details and click Add",
            812: "Student should be added successfully",
            815: "Add multiple courses",
            816: "Add courses one by one",
            817: "Add courses one by one",
            818: "All courses should display properly",
            821: "Delete selected student",
            822: "Select student and click Delete",
            823: "Select student and click Delete",
            824: "Student should be removed from list",
            827: "Enroll student into course",
            828: "Select student/course and click Add Enrollment",
            829: "Select student/course and click Add Enrollment",
            830: "Enrollment should display with grade",
            833: "Update marks and attendance",
            834: "Select enrollment and enter new values",
            835: "Select enrollment and enter new values",
            836: "Marks, attendance, and grade should update correctly",
            839: "View dashboard statistics",
            840: "Open dashboard after adding records",
            841: "Open dashboard after adding records",
            842: "Dashboard values should display properly",
            851: "Add student with empty ID",
            852: "Click Add without entering student ID",
            853: "Warning message should display",
            856: "Delete without selection",
            857: "Click Delete without selecting record",
            858: "No record should be deleted",
            861: "Add enrollment without valid student/course",
            862: "Click Add Enrollment without valid selection",
            863: "Warning message should display",
            866: "Add and delete course",
            867: "Add course then delete it",
            868: "Course should be removed correctly",
            871: "Add and update student",
            872: "Add student then update details",
            873: "Student record should change successfully",
            876: "Add and update enrollment",
            877: "Add enrollment then update marks",
            878: "Enrollment should display updated grade correctly",
            897: "Verify record table display",
            898: "Add multiple students and courses",
            899: "Records should display properly in tables",
            902: "Verify dashboard refresh",
            903: "Run application and update records",
            904: "Dashboard statistics should update correctly",
            916: "CHAPTER 6: LIMITATIONS OF SYSTEM",
            917: "CSV File Storage",
            918: "The current system stores data in CSV files, so it is suitable for small-scale usage but not for large enterprise-level academic data.",
            919: "No User Authentication",
            920: "The application does not provide login or password protection, which limits security and multi-user access.",
            921: "Offline Application",
            922: "The system works only as an offline desktop application and does not support cloud synchronization or online access.",
            923: "Limited Reporting Features",
            924: "Advanced reports, charts, printable mark sheets, and semester-wise analytics are not available in the current version.",
            925: "No Mobile Support",
            926: "The application is designed only for desktop systems and cannot be accessed on mobile devices",
            930: "No Advanced Search Functionality",
            931: "Users cannot currently search or filter records using advanced filters such as department, semester, course, or grade.",
            934: "No Backup Facility",
            935: "The application does not provide automatic backup or recovery of CSV data.",
            936: "No Report Export",
            937: "The current system cannot export reports in PDF or Excel format.",
            939: "Database Connectivity",
            940: "The future version of the Student Course & Performance Management System can include database integration using MySQL or PostgreSQL to store student, course, and enrollment records permanently and securely.",
            941: "User Login and Authentication",
            942: "A secure login and role-based access system can be implemented to allow administrators, faculty members, and students to access relevant features securely.",
            943: "Reports and Analytics",
            944: "Report generation, charts, and printable summaries can be added to analyze student marks, attendance, grades, and course-wise performance.",
            945: "Search and Filter Management",
            946: "Users can be allowed to search and filter records by student name, department, semester, course, marks, attendance, and grade.",
            947: "Export Options",
            948: "Export functionality can be added to generate PDF, Excel, or CSV reports for students, courses, enrollments, and dashboard summaries.",
            949: "Backup and Restore",
            950: "Backup and restore features can be added to protect academic data and recover records when required.",
            951: "Cloud Storage Support",
            952: "Cloud integration can be implemented to synchronize academic records across multiple devices and provide online backup facilities.",
            954: "The Student Course & Performance Management System is a simple, efficient, and user-friendly desktop application developed using Java, Java Swing, AWT, collections, and CSV file handling. The project successfully provides a digital solution for managing student academic records in an organized manner. The application allows users to manage students, courses, enrollments, marks, attendance, grades, and dashboard statistics through an interactive graphical user interface.",
            955: "The system helps users improve academic record organization while reducing manual effort and confusion associated with traditional paper-based methods. The application is lightweight, easy to operate, and uses CSV file storage, making it suitable for college mini project demonstration and learning purposes.",
            956: "The project also demonstrates the practical implementation of object-oriented programming, event handling, file handling, collections, and GUI development concepts in Java. Overall, the Student Course & Performance Management System achieves its objectives successfully and provides a strong foundation for future enhancements such as database integration, report generation, login authentication, backup, and analytics.",
        }

        code_lines = [
            "CHAPTER 4: CODING SAMPLE CODE",
            "package com.college.scms;",
            "",
            "import com.college.scms.persistence.DataStore;",
            "import com.college.scms.service.CampusService;",
            "import com.college.scms.ui.MainFrame;",
            "import javax.swing.SwingUtilities;",
            "import javax.swing.UIManager;",
            "import java.nio.file.Path;",
            "",
            "public class Main {",
            "    public static void main(String[] args) {",
            "        try {",
            "            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());",
            "        } catch (Exception ignored) {",
            "            // System look and feel is optional.",
            "        }",
            "",
            "        Path dataDirectory = Path.of(\"StudentCourseManagementSystem\", \"data\");",
            "        CampusService service = new CampusService(new DataStore(dataDirectory));",
            "",
            "        SwingUtilities.invokeLater(() -> {",
            "            MainFrame frame = new MainFrame(service);",
            "            frame.setVisible(true);",
            "        });",
            "    }",
            "}",
            "",
            "public void addStudent(Student student) {",
            "    require(!student.getId().isBlank(), \"Student ID is required.\");",
            "    require(!students.containsKey(student.getId()), \"Student ID already exists.\");",
            "    require(student.getSemester() > 0, \"Semester must be greater than 0.\");",
            "    students.put(student.getId(), student);",
            "    persist();",
            "}",
            "",
            "public void addCourse(Course course) {",
            "    require(!course.getId().isBlank(), \"Course ID is required.\");",
            "    require(!courses.containsKey(course.getId()), \"Course ID already exists.\");",
            "    require(course.getCredits() > 0, \"Credits must be greater than 0.\");",
            "    require(course.getCapacity() > 0, \"Capacity must be greater than 0.\");",
            "    courses.put(course.getId(), course);",
            "    persist();",
            "}",
            "",
            "public void addEnrollment(String studentId, String courseId, double marks, double attendance) {",
            "    require(students.containsKey(studentId), \"Please select a valid student.\");",
            "    require(courses.containsKey(courseId), \"Please select a valid course.\");",
            "    require(marks >= 0 && marks <= 100, \"Marks must be between 0 and 100.\");",
            "    require(attendance >= 0 && attendance <= 100, \"Attendance must be between 0 and 100.\");",
            "",
            "    boolean exists = enrollments.stream().anyMatch(enrollment ->",
            "        enrollment.getStudentId().equals(studentId)",
            "            && enrollment.getCourseId().equals(courseId));",
            "",
            "    require(!exists, \"This student is already enrolled in the selected course.\");",
            "    enrollments.add(new Enrollment(studentId, courseId, marks, attendance));",
            "    persist();",
            "}",
            "",
            "public DashboardStats getDashboardStats() {",
            "    double averageMarks = enrollments.stream()",
            "        .mapToDouble(Enrollment::getMarks)",
            "        .average()",
            "        .orElse(0.0);",
            "",
            "    double averageAttendance = enrollments.stream()",
            "        .mapToDouble(Enrollment::getAttendance)",
            "        .average()",
            "        .orElse(0.0);",
            "",
            "    long highPerformers = enrollments.stream()",
            "        .filter(enrollment -> enrollment.getMarks() >= 85)",
            "        .count();",
            "",
            "    return new DashboardStats(",
            "        students.size(),",
            "        courses.size(),",
            "        enrollments.size(),",
            "        averageMarks,",
            "        averageAttendance,",
            "        (int) highPerformers",
            "    );",
            "}",
        ]

        for offset, line in enumerate(code_lines):
            replacements[668 + offset] = line
        for index in range(668 + len(code_lines), 780):
            replacements[index] = ""

        for index, text in replacements.items():
            if index < len(paragraphs):
                set_paragraph_text(paragraphs[index], text)

        # Keep the provided ER/table/diagram section intact, but update repeated project wording elsewhere.
        for text_node in root.findall(f".//{W}t"):
            if text_node.text:
                text_node.text = (
                    text_node.text
                    .replace("TO-Do List Application", "Student Course & Performance Management System")
                    .replace("To–Do List Application", "Student Course & Performance Management System")
                    .replace("Smart To-Do List Application", "Student Course & Performance Management System")
                    .replace("Sanika Deshmukh", "Pratik Pravin Dalal and Tejas Dipak Yambal")
                )

        new_xml = ET.tostring(root, encoding="utf-8", xml_declaration=True)

        with zipfile.ZipFile(OUTPUT, "w", zipfile.ZIP_DEFLATED) as zout:
            for item in zin.infolist():
                data = zin.read(item.filename)
                if item.filename == "word/document.xml":
                    data = new_xml
                zout.writestr(copy.copy(item), data)

    print(OUTPUT.resolve())


if __name__ == "__main__":
    main()
