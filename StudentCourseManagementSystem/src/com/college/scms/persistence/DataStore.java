package com.college.scms.persistence;

import com.college.scms.model.Course;
import com.college.scms.model.Enrollment;
import com.college.scms.model.Student;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private final Path dataDirectory;
    private final Path studentsFile;
    private final Path coursesFile;
    private final Path enrollmentsFile;

    public DataStore(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.studentsFile = dataDirectory.resolve("students.csv");
        this.coursesFile = dataDirectory.resolve("courses.csv");
        this.enrollmentsFile = dataDirectory.resolve("enrollments.csv");
    }

    public Snapshot load() {
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create data directory", e);
        }

        if (!Files.exists(studentsFile) || !Files.exists(coursesFile) || !Files.exists(enrollmentsFile)) {
            return Snapshot.empty();
        }

        try {
            return new Snapshot(loadStudents(), loadCourses(), loadEnrollments());
        } catch (IOException e) {
            throw new RuntimeException("Unable to load saved project data", e);
        }
    }

    public void save(Map<String, Student> students, Map<String, Course> courses, List<Enrollment> enrollments) {
        try {
            Files.createDirectories(dataDirectory);
            Files.write(studentsFile, serializeStudents(students), StandardCharsets.UTF_8);
            Files.write(coursesFile, serializeCourses(courses), StandardCharsets.UTF_8);
            Files.write(enrollmentsFile, serializeEnrollments(enrollments), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save project data", e);
        }
    }

    private Map<String, Student> loadStudents() throws IOException {
        Map<String, Student> students = new LinkedHashMap<>();
        for (String line : Files.readAllLines(studentsFile, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(",", -1);
            Student student = new Student(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
            students.put(student.getId(), student);
        }
        return students;
    }

    private Map<String, Course> loadCourses() throws IOException {
        Map<String, Course> courses = new LinkedHashMap<>();
        for (String line : Files.readAllLines(coursesFile, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(",", -1);
            Course course = new Course(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], Integer.parseInt(parts[4]));
            courses.put(course.getId(), course);
        }
        return courses;
    }

    private List<Enrollment> loadEnrollments() throws IOException {
        List<Enrollment> enrollments = new ArrayList<>();
        for (String line : Files.readAllLines(enrollmentsFile, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(",", -1);
            enrollments.add(new Enrollment(parts[0], parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
        }
        return enrollments;
    }

    private List<String> serializeStudents(Map<String, Student> students) {
        List<String> lines = new ArrayList<>();
        for (Student student : students.values()) {
            lines.add(String.join(",",
                    safe(student.getId()),
                    safe(student.getName()),
                    safe(student.getDepartment()),
                    String.valueOf(student.getSemester()),
                    safe(student.getEmail())));
        }
        return lines;
    }

    private List<String> serializeCourses(Map<String, Course> courses) {
        List<String> lines = new ArrayList<>();
        for (Course course : courses.values()) {
            lines.add(String.join(",",
                    safe(course.getId()),
                    safe(course.getTitle()),
                    String.valueOf(course.getCredits()),
                    safe(course.getInstructor()),
                    String.valueOf(course.getCapacity())));
        }
        return lines;
    }

    private List<String> serializeEnrollments(List<Enrollment> enrollments) {
        List<String> lines = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            lines.add(String.join(",",
                    safe(enrollment.getStudentId()),
                    safe(enrollment.getCourseId()),
                    String.valueOf(enrollment.getMarks()),
                    String.valueOf(enrollment.getAttendance())));
        }
        return lines;
    }

    private String safe(String value) {
        return value.replace(",", " ");
    }

    public record Snapshot(Map<String, Student> students, Map<String, Course> courses, List<Enrollment> enrollments) {
        public static Snapshot empty() {
            return new Snapshot(new LinkedHashMap<>(), new LinkedHashMap<>(), new ArrayList<>());
        }
    }
}
