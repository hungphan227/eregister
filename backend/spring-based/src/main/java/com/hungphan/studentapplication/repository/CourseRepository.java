package com.hungphan.studentapplication.repository;

import com.hungphan.studentapplication.dto.CourseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hungphan.studentapplication.model.Course;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
}
