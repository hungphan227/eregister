package com.hungphan.eregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hungphan.eregister.dto.CourseDto;
import com.hungphan.eregister.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseNumberIn(List<String> courseNumbers);
    
}
