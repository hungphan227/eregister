package com.hungphan.eregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hungphan.eregister.dto.CourseStatusDto;
import com.hungphan.eregister.model.StudentCourseRelation;

import java.util.List;

public interface StudentCourseRelationRepository extends JpaRepository<StudentCourseRelation, Long> {
    
    @Query("select count(sc.id) from StudentCourseRelation sc where sc.courseId = ?1")
    int countNumberOfStudentInOneCourse(Long courseId);

    @Query("select new com.hungphan.eregister.dto.CourseStatusDto(courseId, count(id)) from StudentCourseRelation group by courseId")
    List<CourseStatusDto> getCourseStatus();
    
}
