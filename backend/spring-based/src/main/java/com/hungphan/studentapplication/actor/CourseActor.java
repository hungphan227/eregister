package com.hungphan.studentapplication.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungphan.studentapplication.Constants;
import com.hungphan.studentapplication.Utils;
import com.hungphan.studentapplication.VertxSingleton;
import com.hungphan.studentapplication.actor.message.CourseMessage;
import com.hungphan.studentapplication.actor.message.RequestUpdateClientRemainingSlotsMessage;
import com.hungphan.studentapplication.config.Actor;
import com.hungphan.studentapplication.dto.CourseDto;
import com.hungphan.studentapplication.message.Commands;
import com.hungphan.studentapplication.message.HttpResponseMessage;
import com.hungphan.studentapplication.message.SentWebSocketMessage;
import com.hungphan.studentapplication.message.SentWebSocketMessageType;
import com.hungphan.studentapplication.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

@Actor
public class CourseActor extends AbstractActor {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CourseMessage.class, courseMessage -> {
            Long courseId = courseMessage.getCourseId();
            String studentNumber = courseMessage.getStudentNumber();
            DeferredResult<ResponseEntity<HttpResponseMessage>> result = courseMessage.getHttpResult();
            try {
                CourseDto courseDto = courseService.joinCourse(courseId, studentNumber);
                if (courseDto != null) {
                    result.setResult(ResponseEntity.status(HttpStatus.OK).body(new HttpResponseMessage("Completed!")));
                    redisTemplate.convertAndSend("getCourse", courseId.toString());
                    VertxSingleton.getInstance().eventBus().publish("remainingSlots", Utils.convertFromObjectToJson(
                            new SentWebSocketMessage(SentWebSocketMessageType.COMMAND, Commands.GET_COURSE, courseId.toString())));
                } else {
                    result.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpResponseMessage("Over limit!")));
                }
            } catch (DataIntegrityViolationException exception) {
                result.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpResponseMessage("You has already registered")));
            } catch (Exception exception) {
                result.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HttpResponseMessage("Unknown error")));
            }
        }).build();
    }

}
