package com.college.scms.model;

public class Course {
    private final String id;
    private final String title;
    private final int credits;
    private final String instructor;
    private final int capacity;

    public Course(String id, String title, int credits, String instructor, int capacity) {
        this.id = id;
        this.title = title;
        this.credits = credits;
        this.instructor = instructor;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getCapacity() {
        return capacity;
    }
}
