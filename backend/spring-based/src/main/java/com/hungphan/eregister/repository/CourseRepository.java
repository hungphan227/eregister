package com.hungphan.eregister.repository;

import com.hungphan.eregister.dto.CourseStatusDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hungphan.eregister.dto.CourseDto;
import com.hungphan.eregister.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseNumberIn(List<String> courseNumbers);

    @Query("from Course c join StudentCourseRelation sc on c.id=sc.courseId where sc.studentId = ?1 and sc.cancelled=false")
    List<Course> getCoursesByStudentId(String studentId);
    
}
