package com.hungphan.eregister.dto;

public class CourseStatusDto {

    private Long courseId;
    private Integer numberOfStudents;

    public CourseStatusDto(Long courseId, Long numberOfStudents) {
        this.courseId = courseId;
        this.numberOfStudents = numberOfStudents.intValue();
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}
