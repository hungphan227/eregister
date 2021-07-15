package com.hungphan.studentapplication.actor;

import akka.actor.AbstractActor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungphan.studentapplication.VertxSingleton;
import com.hungphan.studentapplication.actor.message.RequestUpdateClientRemainingSlotsMessage;
import com.hungphan.studentapplication.config.Actor;
import com.hungphan.studentapplication.model.Course;
import com.hungphan.studentapplication.repository.CourseRepository;
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
