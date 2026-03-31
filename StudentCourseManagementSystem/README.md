# Student Course & Performance Management System

A medium-level Java Swing college project that manages students, courses, enrollments, marks, attendance, and dashboard statistics.

## Features

- Add and delete student records
- Add and delete course records
- Enroll students into courses
- Store marks and attendance for each enrollment
- Auto-calculate grades
- Dashboard with summary statistics and top performers
- Local CSV-based data persistence

## Technology Used

- Java 17
- Java Swing for GUI
- File handling with CSV storage
- Object-oriented programming

## Project Structure

```text
StudentCourseManagementSystem/
  src/com/college/scms/
    Main.java
    model/
    persistence/
    service/
    ui/
  data/
  README.md
```

## How to Compile

From the `java project` folder:

```powershell
javac -d StudentCourseManagementSystem\out StudentCourseManagementSystem\src\com\college\scms\Main.java StudentCourseManagementSystem\src\com\college\scms\model\*.java StudentCourseManagementSystem\src\com\college\scms\persistence\*.java StudentCourseManagementSystem\src\com\college\scms\service\*.java StudentCourseManagementSystem\src\com\college\scms\ui\*.java
```

## How to Run

```powershell
java -cp StudentCourseManagementSystem\out com.college.scms.Main
```

## Viva Explanation

This is a desktop-based academic management system for handling student records, course records, enrollments, marks, attendance, and performance statistics. It demonstrates Java Swing GUI development, event handling, collections, file handling, and object-oriented design in one complete project.
