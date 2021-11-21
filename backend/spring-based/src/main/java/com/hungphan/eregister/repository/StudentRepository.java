package com.hungphan.eregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hungphan.eregister.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Student findByStudentNumber(String studentNumber);
    
}
