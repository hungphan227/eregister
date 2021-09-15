package com.hungphan.studentapplication.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungphan.studentapplication.Constants;
import com.hungphan.studentapplication.VertxSingleton;
import com.hungphan.studentapplication.actor.message.CourseMessage;
import com.hungphan.studentapplication.actor.message.RequestUpdateClientRemainingSlotsMessage;
import com.hungphan.studentapplication.config.Actor;
import com.hungphan.studentapplication.dto.CourseDto;
import com.hungphan.studentapplication.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

@Actor
public class CourseActor extends AbstractActor {

    @Autowired
    private CourseService courseService;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CourseMessage.class, courseMessage -> {
            Long courseId = courseMessage.getCourseId();
            String studentNumber = courseMessage.getStudentNumber();
            DeferredResult<ResponseEntity<String>> result = courseMessage.getHttpResult();
            try {
                CourseDto courseDto = courseService.joinCourse(courseId, studentNumber);
                if (courseDto != null) {
                    result.setResult(ResponseEntity.ok("Completed!"));
                    ObjectMapper objectMapper = new ObjectMapper();
                    VertxSingleton.getInstance().eventBus().publish("out", objectMapper.writeValueAsString(courseDto));
                } else {
                    result.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Over limit!"));
                }
            } catch (DataIntegrityViolationException exception) {
                result.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You has already registered"));
            } catch (Exception exception) {
                result.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error"));
            }
        }).build();
    }

}
