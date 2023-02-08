package com.hungphan.eregister.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Setter
@Entity
@Table(name = "StudentCourseRelation", uniqueConstraints = @UniqueConstraint(columnNames = { "studentId", "courseId" }))
public class StudentCourseRelation {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "StudentCourseRelation_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "StudentCourseRelation_GEN", sequenceName = "StudentCourseRelation_SEQ", allocationSize = 1)
    private Long id;

    private String studentId;
    private Long courseId;
    private Boolean cancelled;
    
    public StudentCourseRelation() {
        super();
    }

    public StudentCourseRelation(String studentId, Long courseId) {
        super();
        this.studentId = studentId;
        this.courseId = courseId;
        this.cancelled = false;
    }

}
