package com.hungphan.studentapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hungphan.studentapplication.model.Course;
import com.hungphan.studentapplication.repository.CourseRepository;
import com.hungphan.studentapplication.service.CourseService;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @GetMapping("/courses")
    List<Course> getAll() {
        return courseRepository.findAll();
    }
    
    @GetMapping("/join-course/{courseId}")
    private String joinCourse(@PathVariable Long courseId) {
        try {
            if (courseService.joinCourse(courseId)) {
                return "Completed!";
            }
            return "Over limit!";
        } catch (DataIntegrityViolationException exception) {
            return "You has already assigned";
        } catch (Exception exception) {
            return "Unknown error";
        }
    }

}
