package com.hungphan.studentapplication.repository;

import com.hungphan.studentapplication.dto.CourseStatusDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hungphan.studentapplication.model.StudentCourseRelation;

import java.util.List;

public interface StudentCourseRelationRepository extends JpaRepository<StudentCourseRelation, Long> {
    
    @Query("select count(sc.id) from StudentCourseRelation sc where sc.courseId = ?1")
    int countNumberOfStudentInOneCourse(Long courseId);

    @Query("select new com.hungphan.studentapplication.dto.CourseStatusDto(courseId, count(id)) from StudentCourseRelation group by courseId")
    List<CourseStatusDto> getCourseStatus();
    
}
