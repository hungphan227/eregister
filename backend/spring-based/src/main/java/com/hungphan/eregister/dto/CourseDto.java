package com.hungphan.eregister.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {

    private Long id;

    private String courseNumber;
    private String courseName;
    private Integer limit;
    private Integer remainingSlots;
    private String teacher;
    private String description;

    private String image;

    public CourseDto() {
    }

    public CourseDto(Long id, String courseNumber, String courseName, Integer limit, String teacher, String description, String image) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.limit = limit;
        this.teacher = teacher;
        this.description = description;
        this.image = image;
    }

    public CourseDto(Long id, String courseNumber, String courseName, Integer limit, String teacher, String description, Integer remainingSlots, String image) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.limit = limit;
        this.teacher = teacher;
        this.description = description;
        this.remainingSlots = remainingSlots;
        this.image = image;
    }
    
}
