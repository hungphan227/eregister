package com.hungphan.eregister.actor;

import akka.actor.AbstractActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungphan.eregister.VertxSingleton;
import com.hungphan.eregister.actor.message.RequestUpdateClientRemainingSlotsMessage;
import com.hungphan.eregister.config.Actor;
import com.hungphan.eregister.model.Course;
import com.hungphan.eregister.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

// unused
@Actor
public class UpdateClientRemainingSlotsActor extends AbstractActor {

    public static AtomicBoolean requestUpdate = new AtomicBoolean(false);

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(RequestUpdateClientRemainingSlotsMessage.class, message -> {
            if (requestUpdate.getAndSet(false)) {
                List<Course> courses = courseRepository.findAll();
                ObjectMapper objectMapper = new ObjectMapper();
                String stringCourses = objectMapper.writeValueAsString(courses);
                VertxSingleton.getInstance().eventBus().publish("out", stringCourses);
            }
        }).build();
    }

}
