package com.college.scms.service;

import com.college.scms.model.Course;
import com.college.scms.model.Enrollment;
import com.college.scms.model.Student;
import com.college.scms.persistence.DataStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CampusService {
    private final DataStore dataStore;
    private final Map<String, Student> students;
    private final Map<String, Course> courses;
    private final List<Enrollment> enrollments;

    public CampusService(DataStore dataStore) {
        this.dataStore = dataStore;
        DataStore.Snapshot snapshot = dataStore.load();
        this.students = new LinkedHashMap<>(snapshot.students());
        this.courses = new LinkedHashMap<>(snapshot.courses());
        this.enrollments = new ArrayList<>(snapshot.enrollments());

        if (students.isEmpty() && courses.isEmpty() && enrollments.isEmpty()) {
            seedSampleData();
            persist();
        }
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses.values());
    }

    public List<EnrollmentView> getEnrollmentViews() {
        List<EnrollmentView> views = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            Student student = students.get(enrollment.getStudentId());
            Course course = courses.get(enrollment.getCourseId());
            if (student != null && course != null) {
                views.add(new EnrollmentView(
                        student.getId(),
                        student.getName(),
                        course.getId(),
                        course.getTitle(),
                        enrollment.getMarks(),
                        enrollment.getAttendance(),
                        enrollment.getGrade()));
            }
        }
        return views;
    }

    public void addStudent(Student student) {
        require(!student.getId().isBlank(), "Student ID is required.");
        require(!students.containsKey(student.getId()), "Student ID already exists.");
        require(student.getSemester() > 0, "Semester must be greater than 0.");
        students.put(student.getId(), student);
        persist();
    }

    public void updateStudent(Student student) {
        require(!students.containsKey(student.getId()), "Student record not found.");
        require(student.getSemester() > 0, "Semester must be greater than 0.");
        students.put(student.getId(), student);
        persist();
    }

    public void deleteStudent(String studentId) {
        students.remove(studentId);
        enrollments.removeIf(enrollment -> enrollment.getStudentId().equals(studentId));
        persist();
    }

    public void addCourse(Course course) {
        require(!course.getId().isBlank(), "Course ID is required.");
        require(!courses.containsKey(course.getId()), "Course ID already exists.");
        require(course.getCredits() > 0, "Credits must be greater than 0.");
        require(course.getCapacity() > 0, "Capacity must be greater than 0.");
        courses.put(course.getId(), course);
        persist();
    }

    public void updateCourse(Course course) {
        require(!courses.containsKey(course.getId()), "Course record not found.");
        require(course.getCredits() > 0, "Credits must be greater than 0.");
        require(course.getCapacity() > 0, "Capacity must be greater than 0.");

        long countForCourse = enrollments.stream()
                .filter(enrollment -> enrollment.getCourseId().equals(course.getId()))
                .count();
        require(course.getCapacity() >= countForCourse, "Capacity cannot be less than current enrollment count.");

        courses.put(course.getId(), course);
        persist();
    }

    public void deleteCourse(String courseId) {
        courses.remove(courseId);
        enrollments.removeIf(enrollment -> enrollment.getCourseId().equals(courseId));
        persist();
    }

    public void addEnrollment(String studentId, String courseId, double marks, double attendance) {
        require(students.containsKey(studentId), "Please select a valid student.");
        require(courses.containsKey(courseId), "Please select a valid course.");
        require(marks >= 0 && marks <= 100, "Marks must be between 0 and 100.");
        require(attendance >= 0 && attendance <= 100, "Attendance must be between 0 and 100.");

        boolean exists = enrollments.stream()
                .anyMatch(enrollment -> enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId));
        require(!exists, "This student is already enrolled in the selected course.");

        long countForCourse = enrollments.stream()
                .filter(enrollment -> enrollment.getCourseId().equals(courseId))
                .count();
        require(countForCourse < courses.get(courseId).getCapacity(), "Course capacity has been reached.");

        enrollments.add(new Enrollment(studentId, courseId, marks, attendance));
        persist();
    }

    public void updateEnrollment(String studentId, String courseId, double marks, double attendance) {
        require(students.containsKey(studentId), "Please select a valid student.");
        require(courses.containsKey(courseId), "Please select a valid course.");
        require(marks >= 0 && marks <= 100, "Marks must be between 0 and 100.");
        require(attendance >= 0 && attendance <= 100, "Attendance must be between 0 and 100.");

        for (int index = 0; index < enrollments.size(); index++) {
            Enrollment enrollment = enrollments.get(index);
            if (enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId)) {
                enrollments.set(index, new Enrollment(studentId, courseId, marks, attendance));
                persist();
                return;
            }
        }

        throw new IllegalArgumentException("Enrollment record not found.");
    }

    public void deleteEnrollment(String studentId, String courseId) {
        enrollments.removeIf(enrollment ->
                enrollment.getStudentId().equals(studentId) && enrollment.getCourseId().equals(courseId));
        persist();
    }

    public DashboardStats getDashboardStats() {
        double averageMarks = enrollments.stream().mapToDouble(Enrollment::getMarks).average().orElse(0.0);
        double averageAttendance = enrollments.stream().mapToDouble(Enrollment::getAttendance).average().orElse(0.0);
        long highPerformers = enrollments.stream().filter(enrollment -> enrollment.getMarks() >= 85).count();
        return new DashboardStats(students.size(), courses.size(), enrollments.size(), averageMarks, averageAttendance, (int) highPerformers);
    }

    public List<String> topPerformers() {
        Map<String, Double> averageByStudent = new LinkedHashMap<>();
        for (Student student : students.values()) {
            double avg = enrollments.stream()
                    .filter(enrollment -> enrollment.getStudentId().equals(student.getId()))
                    .mapToDouble(Enrollment::getMarks)
                    .average()
                    .orElse(-1);
            if (avg >= 0) {
                averageByStudent.put(student.getId(), avg);
            }
        }

        return averageByStudent.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> {
                    Student student = students.get(entry.getKey());
                    return student.getName() + " (" + student.getId() + ") - Avg Marks: " + String.format("%.2f", entry.getValue());
                })
                .toList();
    }

    private void persist() {
        dataStore.save(students, courses, enrollments);
    }

    private void seedSampleData() {
        students.put("S101", new Student("S101", "Aarav Mehta", "Computer Science", 4, "aarav@college.edu"));
        students.put("S102", new Student("S102", "Diya Sharma", "Information Technology", 6, "diya@college.edu"));
        students.put("S103", new Student("S103", "Rahul Verma", "Electronics", 2, "rahul@college.edu"));

        courses.put("C201", new Course("C201", "Data Structures", 4, "Prof. Nair", 40));
        courses.put("C202", new Course("C202", "Database Systems", 3, "Prof. Iyer", 35));
        courses.put("C203", new Course("C203", "Operating Systems", 4, "Prof. Kulkarni", 30));

        enrollments.add(new Enrollment("S101", "C201", 91, 95));
        enrollments.add(new Enrollment("S101", "C202", 88, 92));
        enrollments.add(new Enrollment("S102", "C202", 84, 89));
        enrollments.add(new Enrollment("S103", "C203", 76, 85));
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public record DashboardStats(
            int totalStudents,
            int totalCourses,
            int totalEnrollments,
            double averageMarks,
            double averageAttendance,
            int highPerformers
    ) {
    }

    public record EnrollmentView(
            String studentId,
            String studentName,
            String courseId,
            String courseTitle,
            double marks,
            double attendance,
            String grade
    ) {
    }
}
