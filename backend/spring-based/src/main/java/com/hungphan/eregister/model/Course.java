package com.hungphan.eregister.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Course extends BasicEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "COURSE_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "COURSE_GEN", sequenceName = "COURSE_SEQ", allocationSize = 1)
    private Long id;

    private String courseNumber;
    private String courseName;
    private Integer courseLimit;
    private String teacher;
    private Long price;
    
    @Column(length = 1000)
    private String description;

    private String image;

    public Course(String courseNumber, String courseName, Integer limit, String teacher, Long price, String image) {
        super();
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.courseLimit = limit;
        this.teacher = teacher;
        this.price = price;
        this.image = image;
    }
    
}
