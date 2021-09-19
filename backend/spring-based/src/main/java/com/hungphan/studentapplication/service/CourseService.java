package com.hungphan.studentapplication.service;

import com.hungphan.studentapplication.dto.CourseDto;
import com.hungphan.studentapplication.dto.CourseStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hungphan.studentapplication.model.Course;
import com.hungphan.studentapplication.model.Student;
import com.hungphan.studentapplication.model.StudentCourseRelation;
import com.hungphan.studentapplication.repository.CourseRepository;
import com.hungphan.studentapplication.repository.StudentCourseRelationRepository;
import com.hungphan.studentapplication.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CourseService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentCourseRelationRepository studentCourseRelationRepository;
    
    @Transactional(rollbackOn={Exception.class})
    public CourseDto joinCourse(Long courseId, String studentNumber) {
        int numberOfStudentsInTheCourse = studentCourseRelationRepository.countNumberOfStudentInOneCourse(courseId);
        Course course = courseRepository.findById(courseId).get();
        int remainingSlots = course.getLimit() - numberOfStudentsInTheCourse;
        if (remainingSlots > 0) {
            Student student = studentRepository.findByStudentNumber(studentNumber);
            studentCourseRelationRepository.save(new StudentCourseRelation(student.getId(), courseId));
            return new CourseDto(course.getId(),course.getCourseNumber(),course.getCourseName(), course.getLimit(),course.getTeacher(),course.getDescription(),remainingSlots - 1);
        }
        return null;
    }

    public List<CourseDto> getAllCoursesWithRemainingSlots() {
        List<Course> courses = courseRepository.findAll();
        List<CourseStatusDto> courseStatusDtos = studentCourseRelationRepository.getCourseStatus();
        Map<Long, Integer> mapCourseId2Students = courseStatusDtos.stream().collect(Collectors.toMap(c->c.getCourseId(),c->c.getNumberOfStudents()));

        List<CourseDto> courseDtos = new ArrayList<>();
        for(Course course : courses) {
            CourseDto courseDto = new CourseDto(course.getId(),course.getCourseNumber(),course.getCourseName(),course.getLimit(),course.getTeacher(),course.getDescription());
            Integer remainingSlots = mapCourseId2Students.get(course.getId())!=null?mapCourseId2Students.get(course.getId()):0;
            courseDto.setRemainingSlots(course.getLimit() - remainingSlots);
            courseDtos.add(courseDto);
        }
        return courseDtos;
    }
    
    public CourseDto getCourseWithRemainingSlots(Long courseId) {
        int numberOfStudentsInTheCourse = studentCourseRelationRepository.countNumberOfStudentInOneCourse(courseId);
        Course course = courseRepository.findById(courseId).get();
        int remainingSlots = course.getLimit() - numberOfStudentsInTheCourse;
        return new CourseDto(course.getId(),course.getCourseNumber(),course.getCourseName(), course.getLimit(),course.getTeacher(),course.getDescription(),remainingSlots);
    }

}
