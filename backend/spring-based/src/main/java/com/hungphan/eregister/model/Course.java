package com.hungphan.eregister.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import java.math.BigDecimal;

@Entity
public class Course {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "COURSE_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COURSE_GEN", sequenceName = "COURSE_SEQ", allocationSize = 1)
    private Long id;

    private String courseNumber;
    private String courseName;
    private Integer limit;
    private String teacher;
    
    @Column(length = 1000)
    private String description;
    
    public Course() {
        super();
    }

    public Course(String courseNumber, String courseName, Integer limit) {
        super();
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.limit = limit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
