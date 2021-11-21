package com.hungphan.eregister.dto;

public class CourseDto {

    private Long id;

    private String courseNumber;
    private String courseName;
    private Integer limit;
    private Integer remainingSlots;
    private String teacher;
    private String description;

    public CourseDto() {
    }

    public CourseDto(Long id, String courseNumber, String courseName, Integer limit, String teacher, String description) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.limit = limit;
        this.teacher = teacher;
        this.description = description;
    }

    public CourseDto(Long id, String courseNumber, String courseName, Integer limit, String teacher, String description, Integer remainingSlots) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.limit = limit;
        this.teacher = teacher;
        this.description = description;
        this.remainingSlots = remainingSlots;
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

    public Integer getRemainingSlots() {
        return remainingSlots;
    }

    public void setRemainingSlots(Integer remainingSlots) {
        this.remainingSlots = remainingSlots;
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
