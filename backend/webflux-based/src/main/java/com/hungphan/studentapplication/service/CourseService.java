package com.hungphan.studentapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hungphan.studentapplication.Utils;
import com.hungphan.studentapplication.model.Course;
import com.hungphan.studentapplication.model.Student;
import com.hungphan.studentapplication.model.StudentCourseRelation;
import com.hungphan.studentapplication.repository.CourseRepository;
import com.hungphan.studentapplication.repository.StudentCourseRelationRepository;
import com.hungphan.studentapplication.repository.StudentRepository;

import javax.transaction.Transactional;

@Component
public class CourseService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentCourseRelationRepository studentCourseRelationRepository;
    
    @Transactional(rollbackOn={Exception.class})
    public boolean joinCourse(Long courseId) {
        int numberOfStudentsInTheCourse = studentCourseRelationRepository.countNumberOfStudentInACourse(courseId);
        Course course = courseRepository.findById(courseId).get();
        if (numberOfStudentsInTheCourse < course.getLimit()) {
            try {
                studentRepository.findByStudentNumber(Utils.getCurrentUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Student student = studentRepository.findByStudentNumber(Utils.getCurrentUser());
            studentCourseRelationRepository.save(new StudentCourseRelation(student.getId(), courseId));
            return true;
        }
        return false;
    }

}
