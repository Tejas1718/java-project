# Student Course & Performance Management System

## 1. Introduction

The Student Course & Performance Management System is a Java-based desktop application designed for academic institutions to manage student information efficiently. It helps store and organize records related to students, courses, marks, attendance, and grades through a user-friendly graphical interface.

The purpose of this project is to reduce manual record handling and provide a simple digital solution that can be demonstrated as a medium-level college mini project.

## 2. Objectives

- To maintain student records digitally
- To manage course details in an organized manner
- To track student enrollments in different courses
- To record marks and attendance
- To calculate grades automatically
- To display summary statistics through a dashboard

## 3. Scope of the Project

This project is useful for small-scale academic management where student course details and performance need to be stored and accessed easily. It is suitable for demonstration, learning object-oriented programming, and understanding Java Swing application development.

## 4. Technologies Used

- Java 17
- Java Swing
- OOP concepts
- Java Collections Framework
- File handling using CSV

## 5. Modules of the Project

### 5.1 Student Management

This module allows the user to:

- Add new student records
- Update existing student records
- Delete student records
- View student details in tabular form

### 5.2 Course Management

This module allows the user to:

- Add new course records
- Update course details
- Delete course records
- View course information in tabular form

### 5.3 Enrollment Management

This module allows the user to:

- Enroll students in courses
- Update marks and attendance
- Delete enrollment records
- Generate grades based on marks

### 5.4 Dashboard

The dashboard provides:

- Total number of students
- Total number of courses
- Total number of enrollments
- Average marks
- Average attendance
- Top performers list

## 6. System Design

The system is divided into multiple packages:

- `model` for entity classes such as Student, Course, and Enrollment
- `service` for business logic and validations
- `persistence` for file storage and loading
- `ui` for Java Swing screens and components

This layered structure improves readability and makes the application easy to maintain.

## 7. Working of the Project

1. The application starts from the main class.
2. Existing data is loaded from CSV files.
3. The user interacts with the dashboard and management tabs.
4. Data entered through forms is validated in the service layer.
5. Updated records are stored back into CSV files.
6. Dashboard values are refreshed dynamically.

## 8. Features

- Attractive Java Swing interface
- Add, update, load, and delete actions
- Local data storage
- Automatic grade calculation
- Real-time dashboard summary
- Suitable for academic project submission

## 9. Advantages

- Easy to use
- Saves time compared to manual record keeping
- Demonstrates multiple Java concepts in one project
- Lightweight and does not require a database
- Can be extended in the future

## 10. Limitations

- Data is stored locally in CSV files only
- No multi-user support
- No online access
- No authentication module in the current version

## 11. Future Enhancements

- Add login and role-based access
- Connect with MySQL database
- Add charts and report export
- Add search and filter functionality
- Add semester-wise report generation

## 12. Conclusion

The Student Course & Performance Management System is a useful and practical Java mini project for academic purposes. It shows how Java Swing, file handling, collections, and object-oriented programming can be combined to build a complete desktop application. The project is easy to understand, visually presentable, and suitable for college submission and demonstration.
