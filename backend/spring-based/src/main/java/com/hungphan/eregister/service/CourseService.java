package com.hungphan.eregister.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hungphan.eregister.dto.CourseDto;
import com.hungphan.eregister.dto.CourseStatusDto;
import com.hungphan.eregister.model.Course;
import com.hungphan.eregister.model.StudentCourseRelation;
import com.hungphan.eregister.repository.CourseRepository;
import com.hungphan.eregister.repository.StudentCourseRelationRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CourseService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentCourseRelationRepository studentCourseRelationRepository;

    @Autowired
    private RestClient elasticClient;
    
    @Transactional(rollbackOn={Exception.class})
    public CourseDto joinCourse(Long courseId, String studentId) {
        int numberOfStudentsInTheCourse = studentCourseRelationRepository.countNumberOfStudentInOneCourse(courseId);
        Course course = courseRepository.findById(courseId).get();
        int remainingSlots = course.getCourseLimit() - numberOfStudentsInTheCourse;
        if (remainingSlots > 0) {
            studentCourseRelationRepository.save(new StudentCourseRelation(studentId, courseId));
            LOGGER.info("Save new StudentCourseRelation student {} course {} into database", studentId, course.getCourseNumber());
            return new CourseDto(course.getId(), course.getCourseNumber(), course.getCourseName(), course.getCourseLimit(),
                    course.getTeacher(),course.getDescription(),remainingSlots - 1, course.getImage());
        }
        return null;
    }

    @Transactional
    public Boolean cancelCourseRegistration(Long courseId, String studentId) {
        if (studentCourseRelationRepository.updateCancelledByCourseIdAndStudentId(courseId, studentId) > 0) {
            LOGGER.info("Delete StudentCourseRelation student {}, course {} in database", studentId, courseId);
            return true;
        }
        return false;
    }

    public List<CourseDto> getAllCoursesWithRemainingSlots() {
        List<Course> courses = courseRepository.findAll();
        List<CourseStatusDto> courseStatusDtos = studentCourseRelationRepository.getCourseStatus();
        Map<Long, Integer> mapCourseId2Students = courseStatusDtos.stream().collect(Collectors.toMap(c->c.getCourseId(),c->c.getNumberOfStudents()));

        List<CourseDto> courseDtos = new ArrayList<>();
        for(Course course : courses) {
            CourseDto courseDto = new CourseDto(course.getId(), course.getCourseNumber(), course.getCourseName(), course.getCourseLimit(),
                    course.getTeacher(), course.getDescription(), course.getImage());
            Integer remainingSlots = mapCourseId2Students.get(course.getId())!=null?mapCourseId2Students.get(course.getId()):0;
            courseDto.setRemainingSlots(course.getCourseLimit() - remainingSlots);
            courseDtos.add(courseDto);
        }
        return courseDtos;
    }
    
    public CourseDto getCourseWithRemainingSlots(Long courseId) {
        int numberOfStudentsInTheCourse = studentCourseRelationRepository.countNumberOfStudentInOneCourse(courseId);
        Course course = courseRepository.findById(courseId).get();
        int remainingSlots = course.getCourseLimit() - numberOfStudentsInTheCourse;
        return new CourseDto(course.getId(), course.getCourseNumber(), course.getCourseName(), course.getCourseLimit(),
                course.getTeacher(), course.getDescription(), remainingSlots, course.getImage());
    }

    public List<CourseDto> searchCourses(String searchString) throws Exception {
        List<String> courseNumbers = getDataFromElastic(searchString);
        List<Course> courses = courseRepository.findByCourseNumberIn(courseNumbers);
        List<CourseStatusDto> listCourseStatusDto = studentCourseRelationRepository.countNumberOfStudentInCourses(courseNumbers);
        Map<Long, Integer> mapCourseId2Students = listCourseStatusDto.stream().collect(Collectors.toMap(CourseStatusDto::getCourseId, CourseStatusDto::getNumberOfStudents));
        List<CourseDto> listCourseDto = new ArrayList<>();
        for(Course course : courses) {
            CourseDto courseDto = new CourseDto(course.getId(),course.getCourseNumber(), course.getCourseName(), course.getCourseLimit(),
                    course.getTeacher(), course.getDescription(), course.getImage());
            Integer remainingSlots = mapCourseId2Students.get(course.getId())!=null?mapCourseId2Students.get(course.getId()):0;
            courseDto.setRemainingSlots(course.getCourseLimit() - remainingSlots);
            listCourseDto.add(courseDto);
        }
        return listCourseDto;
    }

    private List<String> getDataFromElastic(String searchString) throws Exception {
        List<String> courseNumbers = new ArrayList<>();
        Request request = new Request(
                "GET",
                "/studentmngm/_search");
        request.setEntity(new NStringEntity(
                "{\n" +
                        "    \"query\": {\n" +
                        "        \"multi_match\": {\n" +
                        "            \"query\": \"" + searchString + "\",\n" +
                        "            \"type\": \"phrase\",\n" +
                        "            \"fields\": [\n" +
                        "                \"course_name\",\n" +
                        "                \"course_number\",\n" +
                        "                \"teacher\",\n" +
                        "                \"description\"\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
                ContentType.APPLICATION_JSON));
        Response response = elasticClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        final JsonNode root = new ObjectMapper().readTree(responseBody);
        for (JsonNode node : root.get("hits").get("hits")) {
            courseNumbers.add(node.get("_source").get("course_number").asText());
        }
        return courseNumbers;
    }

}
