package com.hungphan.eregister.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungphan.eregister.Constants;
import com.hungphan.eregister.Utils;
import com.hungphan.eregister.VertxSingleton;
import com.hungphan.eregister.actor.message.CourseMessage;
import com.hungphan.eregister.actor.message.RequestUpdateClientRemainingSlotsMessage;
import com.hungphan.eregister.config.Actor;
import com.hungphan.eregister.dto.CourseDto;
import com.hungphan.eregister.message.Commands;
import com.hungphan.eregister.message.HttpResponseMessage;
import com.hungphan.eregister.message.SentWebSocketMessage;
import com.hungphan.eregister.message.SentWebSocketMessageType;
import com.hungphan.eregister.service.CourseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

@Actor
public class CourseActor extends AbstractActor {
    
    private static Logger LOGGER = LoggerFactory.getLogger(CourseActor.class);
    
    @Value("${redis.enabled}")
    private boolean isRedisEnable;

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
                    if (isRedisEnable) {
                        redisTemplate.convertAndSend("getCourse", courseId.toString());
                    } else {
                        VertxSingleton.getInstance().eventBus().publish("remainingSlots", Utils.convertFromObjectToJson(
                                new SentWebSocketMessage(SentWebSocketMessageType.COMMAND, Commands.GET_COURSE, courseId.toString())));
                    }
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
