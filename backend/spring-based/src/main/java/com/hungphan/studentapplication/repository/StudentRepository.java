package com.hungphan.studentapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hungphan.studentapplication.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Student findByStudentNumber(String studentNumber);
    
}
