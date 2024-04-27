package com.example.javafxlogincrudform;

public class StudentData {
    private int studentNumber;
    private String fullName;
    private String year;
    private String course;
    private String gender;

    public StudentData(int studentNumber, String fullName, String year, String course, String gender) {
        this.studentNumber = studentNumber;
        this.fullName = fullName;
        this.year = year;
        this.course = course;
        this.gender = gender;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getYear() {
        return year;
    }

    public String getCourse() {
        return course;
    }

    public String getGender() {
        return gender;
    }
}
