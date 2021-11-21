package com.hungphan.eregister.controller;

import java.util.List;

import akka.actor.ActorRef;
import akka.routing.ConsistentHashingRouter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PutMapping("/join-course/{courseId}")
    private DeferredResult<ResponseEntity<HttpResponseMessage>> joinCourse(@PathVariable Long courseId) {
        DeferredResult<ResponseEntity<HttpResponseMessage>> result = new DeferredResult<>();
        String studentNumber = Utils.getCurrentUser();
        ActorRef courseActor = ActorSystemSingleton.getInstance().actorFor(Constants.GUARDIAN_ACTOR_NAME + CourseActor.class.getSimpleName());
        courseActor.tell(new ConsistentHashingRouter.ConsistentHashableEnvelope(new CourseMessage(courseId, studentNumber, result), courseId), ActorRef.noSender());
        return result;
    }

}
