package com.hungphan.eregister.controller;

import java.util.List;

import akka.actor.ActorRef;
import akka.routing.ConsistentHashingRouter;

import com.hungphan.eregister.dto.Jwt;
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

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/courses")
    List<CourseDto> getAll() {
        return courseService.getAllCoursesWithRemainingSlots();
    }
    
    @GetMapping("/course/{courseId}")
    CourseDto getCourse(@PathVariable Long courseId) {
        return courseService.getCourseWithRemainingSlots(courseId);
    }

    @GetMapping("/course/search/{searchString}")
    ResponseEntity<List<CourseDto>> searchCourses(@PathVariable String searchString) {
        List<CourseDto> list = null;
        try {
            list = courseService.searchCourses(searchString);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(list);
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PutMapping("/join-course/{courseId}")
    DeferredResult<ResponseEntity<HttpResponseMessage>> joinCourse(@PathVariable Long courseId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        DeferredResult<ResponseEntity<HttpResponseMessage>> result = new DeferredResult<>();
        try {
            Jwt jwt = Utils.decodeJwt(token);
            String studentNumber = jwt.getSub();
            ActorRef courseActor = ActorSystemSingleton.getInstance().actorFor(Constants.GUARDIAN_ACTOR_NAME + CourseActor.class.getSimpleName());
            courseActor.tell(new ConsistentHashingRouter.ConsistentHashableEnvelope(new CourseMessage(courseId, studentNumber, result), courseId), ActorRef.noSender());
        } catch (Exception exception) {
            result.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HttpResponseMessage("Unknown error")));
        }
        return result;
    }

}
