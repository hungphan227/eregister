package com.hungphan.studentapplication.controller;

import java.util.List;

import akka.actor.ActorRef;
import akka.routing.ConsistentHashingRouter;
import com.hungphan.studentapplication.Constants;
import com.hungphan.studentapplication.Utils;
import com.hungphan.studentapplication.actor.ActorSystemSingleton;
import com.hungphan.studentapplication.actor.CourseActor;
import com.hungphan.studentapplication.actor.message.CourseMessage;
import com.hungphan.studentapplication.dto.CourseDto;
import com.hungphan.studentapplication.message.HttpResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hungphan.studentapplication.model.Course;
import com.hungphan.studentapplication.repository.CourseRepository;
import com.hungphan.studentapplication.service.CourseService;
import org.springframework.web.context.request.async.DeferredResult;

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

    @PutMapping("/join-course/{courseId}")
    private DeferredResult<ResponseEntity<HttpResponseMessage>> joinCourse(@PathVariable Long courseId) {
        DeferredResult<ResponseEntity<HttpResponseMessage>> result = new DeferredResult<>();
        String studentNumber = Utils.getCurrentUser();
        ActorRef courseActor = ActorSystemSingleton.getInstance().actorFor(Constants.GUARDIAN_ACTOR_NAME + CourseActor.class.getSimpleName());
        courseActor.tell(new ConsistentHashingRouter.ConsistentHashableEnvelope(new CourseMessage(courseId, studentNumber, result), courseId), ActorRef.noSender());
        return result;
    }

}
