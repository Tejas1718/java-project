package com.college.scms.model;

public class Student {
    private final String id;
    private final String name;
    private final String department;
    private final int semester;
    private final String email;

    public Student(String id, String name, String department, int semester, String email) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.semester = semester;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public int getSemester() {
        return semester;
    }

    public String getEmail() {
        return email;
    }
}
