package com.hungphan.studentapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hungphan.studentapplication.model.StudentCourseRelation;

public interface StudentCourseRelationRepository extends JpaRepository<StudentCourseRelation, Long> {
    
    @Query("select count(sc.id) from StudentCourseRelation sc where sc.courseId = ?1")
    int countNumberOfStudentInACourse(Long courseId);
    
}
