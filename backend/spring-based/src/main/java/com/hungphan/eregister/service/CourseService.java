package com.hungphan.eregister.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungphan.eregister.dto.HoldingCreditRequestDto;
import com.hungphan.eregister.dto.HoldingCreditResponseDto;
import com.hungphan.eregister.model.PendingRestApiCallCouple;
import com.hungphan.eregister.repository.PendingRestApiCallCoupleRepository;
import com.hungphan.eregister.restclient.UserService;
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CourseService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentCourseRelationRepository studentCourseRelationRepository;

    @Autowired
    private PendingRestApiCallCoupleRepository pendingRestApiCallCoupleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PendingRestApiCallCoupleService pendingRestApiCallCoupleService;

    @Autowired
    private RestClient elasticClient;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManagerFactory emf;

    @Transactional
    public CourseDto joinCourse(Long courseId, String studentId) {
        int numberOfStudentsInTheCourse = studentCourseRelationRepository.countNumberOfStudentInOneCourse(courseId);
        Course course = courseRepository.findById(courseId).get();
        int remainingSlots = course.getCourseLimit() - numberOfStudentsInTheCourse;
        if (remainingSlots > 0) {
            String requestId = UUID.randomUUID().toString();
            pendingRestApiCallCoupleService.createPendingRestApiCallCoupleThenCommit(PendingRestApiCallCouple.builder()
                    .requestId(requestId).className("userService").firstMethodName("holdCredit").build());
            studentCourseRelationRepository.save(new StudentCourseRelation(studentId, courseId));
            LOGGER.info("Save new StudentCourseRelation student {} course {} into database", studentId, course.getCourseNumber());

            userService.holdCredit(new HoldingCreditRequestDto(requestId, course.getPrice(), studentId, "Register a course"));
            PendingRestApiCallCouple pendingRestApiCallCouple = pendingRestApiCallCoupleRepository.findByRequestId(requestId);
            pendingRestApiCallCouple.setSecondMethodName("useCredit");
            pendingRestApiCallCoupleRepository.save(pendingRestApiCallCouple);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                public void afterCompletion(int status){
                    if (TransactionSynchronization.STATUS_COMMITTED == status) {
                        userService.useCredit(requestId);
                        pendingRestApiCallCoupleService.deletePendingRestApiCallCoupleThenCommit(requestId);
                        return;
                    }
                    if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
                        pendingRestApiCallCoupleService.updateSecondMethodNameOfPendingRestApiCallCoupleThenCommit(requestId, "releaseCredit");
                        userService.releaseCredit(requestId);
                        pendingRestApiCallCoupleService.deletePendingRestApiCallCoupleThenCommit(requestId);
                    }
                }
            });

            return new CourseDto(course.getId(), course.getCourseNumber(), course.getCourseName(), course.getCourseLimit(),
                    course.getTeacher(),course.getDescription(),remainingSlots - 1, course.getImage());
        }
        return null;
    }

    public void checkPendingRestApiCalls() {
        List<PendingRestApiCallCouple> pendingRestApiCallCouples = pendingRestApiCallCoupleRepository.findAll();
        for (PendingRestApiCallCouple pendingRestApiCallCouple : pendingRestApiCallCouples) {
            Instant currentTime = Instant.now();
            currentTime = currentTime.minus(15, ChronoUnit.MINUTES);
            if (pendingRestApiCallCouple.getCreatedTime().compareTo(currentTime)>=0) continue;
            if ("useCredit".equals(pendingRestApiCallCouple.getSecondMethodName())) {
                userService.useCredit(pendingRestApiCallCouple.getRequestId());
                pendingRestApiCallCoupleRepository.delete(pendingRestApiCallCouple);
                continue;
            }
            if (pendingRestApiCallCouple.getSecondMethodName() == null || "releaseCredit".equals(pendingRestApiCallCouple.getSecondMethodName())) {
                userService.releaseCredit(pendingRestApiCallCouple.getRequestId());
                pendingRestApiCallCoupleRepository.delete(pendingRestApiCallCouple);
                continue;
            }
        }
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
