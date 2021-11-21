package com.hungphan.eregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hungphan.eregister.Application;
import com.hungphan.eregister.dto.CourseDto;
import com.hungphan.eregister.dto.CourseStatusDto;
import com.hungphan.eregister.model.Course;
import com.hungphan.eregister.model.Student;
import com.hungphan.eregister.model.StudentCourseRelation;
import com.hungphan.eregister.repository.CourseRepository;
import com.hungphan.eregister.repository.StudentCourseRelationRepository;
import com.hungphan.eregister.repository.StudentRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CourseService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);
    
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
            LOGGER.info("Save new StudentCourseRelation student {} course {} into database", student.getStudentNumber(), course.getCourseNumber());
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
