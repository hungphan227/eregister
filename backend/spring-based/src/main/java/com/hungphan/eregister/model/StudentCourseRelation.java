package com.hungphan.eregister.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "StudentCourseRelation", uniqueConstraints = @UniqueConstraint(columnNames = { "studentId", "courseId" }))
public class StudentCourseRelation {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "StudentCourseRelation_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "StudentCourseRelation_GEN", sequenceName = "StudentCourseRelation_SEQ", allocationSize = 1)
    private Long id;

    private Long studentId;
    private Long courseId;
    
    public StudentCourseRelation() {
        super();
    }

    public StudentCourseRelation(Long studentId, Long courseId) {
        super();
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

}
