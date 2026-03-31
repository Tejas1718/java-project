package com.college.scms.model;

public class Enrollment {
    private final String studentId;
    private final String courseId;
    private final double marks;
    private final double attendance;

    public Enrollment(String studentId, String courseId, double marks, double attendance) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.marks = marks;
        this.attendance = attendance;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public double getMarks() {
        return marks;
    }

    public double getAttendance() {
        return attendance;
    }

    public String getGrade() {
        if (marks >= 90) {
            return "A+";
        }
        if (marks >= 80) {
            return "A";
        }
        if (marks >= 70) {
            return "B";
        }
        if (marks >= 60) {
            return "C";
        }
        if (marks >= 50) {
            return "D";
        }
        return "F";
    }
}
