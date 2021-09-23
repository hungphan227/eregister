package com.hungphan.studentapplication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.hungphan.studentapplication.Utils;
import com.hungphan.studentapplication.VertxSingleton;
import com.hungphan.studentapplication.message.Commands;
import com.hungphan.studentapplication.message.SentWebSocketMessage;
import com.hungphan.studentapplication.message.SentWebSocketMessageType;

public class RedisMessageListener implements MessageListener {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageListener.class);
    
    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        LOGGER.info("Receive message from redis: " + message.toString());
        String courseId = message.toString();
        try {
            VertxSingleton.getInstance().eventBus().publish("remainingSlots", Utils.convertFromObjectToJson(
                    new SentWebSocketMessage(SentWebSocketMessageType.COMMAND, Commands.GET_COURSE, courseId)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
