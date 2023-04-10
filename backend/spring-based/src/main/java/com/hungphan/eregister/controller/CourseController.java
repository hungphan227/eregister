package com.hungphan.eregister.controller;

import java.util.List;

import akka.actor.ActorRef;
import akka.routing.ConsistentHashingRouter;

import com.hungphan.eregister.dto.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import com.hungphan.eregister.Constants;
import com.hungphan.eregister.Utils;
import com.hungphan.eregister.actor.ActorSystemSingleton;
import com.hungphan.eregister.actor.CourseActor;
import com.hungphan.eregister.actor.message.CourseMessage;
import com.hungphan.eregister.dto.CourseDto;
import com.hungphan.eregister.message.HttpResponseMessage;
import com.hungphan.eregister.model.Course;
import com.hungphan.eregister.repository.CourseRepository;
import com.hungphan.eregister.service.CourseService;

import javax.ws.rs.QueryParam;

@RestController
public class CourseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    List<CourseDto> getAll() {
        LOGGER.info("start getAll method");
        return courseService.getAllCoursesWithRemainingSlots();
    }
    
    @GetMapping("/course/{courseId}")
    CourseDto getCourse(@PathVariable Long courseId) {
        LOGGER.info("start getCourse method with courseId {}", courseId);
        return courseService.getCourseWithRemainingSlots(courseId);
    }

    @GetMapping("/course/search/{searchString}")
    ResponseEntity<List<CourseDto>> searchCourses(@PathVariable String searchString) {
        LOGGER.info("start searchCourses method with searchString {}", searchString);
        List<CourseDto> list = null;
        try {
            list = courseService.searchCourses(searchString);
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/course/get-courses-of-student")
    ResponseEntity<List<Course>> getCoursesOfStudent(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        LOGGER.info("start getCoursesOfStudent method");
        List<Course> list = null;
        Jwt jwt = Utils.decodeJwt(token);
        String username = jwt.getSub();
        list = courseRepository.getCoursesByStudentId(username);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PutMapping("/join-course/{courseId}")
    DeferredResult<ResponseEntity<HttpResponseMessage>> joinCourse(@PathVariable Long courseId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        LOGGER.info("start joinCourse method with courseId {}, token {}", courseId, token);
        DeferredResult<ResponseEntity<HttpResponseMessage>> result = new DeferredResult<>();
        try {
            Jwt jwt = Utils.decodeJwt(token);
            String username = jwt.getSub();
            ActorRef courseActor = ActorSystemSingleton.getInstance().actorFor(Constants.GUARDIAN_ACTOR_NAME + CourseActor.class.getSimpleName());
            courseActor.tell(new ConsistentHashingRouter.ConsistentHashableEnvelope(new CourseMessage(courseId, username, result), courseId), ActorRef.noSender());
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            result.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HttpResponseMessage("Unknown error")));
        }
        return result;
    }

    @DeleteMapping("/course/cancel/{courseId}")
    ResponseEntity<HttpResponseMessage> cancelCourseRegistration(@PathVariable(value = "courseId") Long courseId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        LOGGER.info("start cancelCourseRegistration method with courseId {} and token {}", courseId, token);
        try {
            Jwt jwt = Utils.decodeJwt(token);
            String username = jwt.getSub();
            if (courseService.cancelCourseRegistration(courseId, username)) return ResponseEntity.status(HttpStatus.OK).body(new HttpResponseMessage("Succeed"));
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HttpResponseMessage("Unknown error"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpResponseMessage("Fail"));
    }

}
