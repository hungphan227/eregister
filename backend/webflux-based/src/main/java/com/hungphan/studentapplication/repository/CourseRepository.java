package com.hungphan.studentapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hungphan.studentapplication.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    
}
