package com.hungphan.eregister.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public class RemainingSlotHandler implements Handler<BridgeEvent> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RemainingSlotHandler.class);

    private final EventBus eventBus;

    public RemainingSlotHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void handle(BridgeEvent event) {
        if (event.type() == BridgeEventType.SOCKET_CREATED) {
            LOGGER.info("A web socket was created");
        }

        if (event.type() == BridgeEventType.SEND) {
            LOGGER.info("Receive from web socket client: {}", event.getRawMessage().getJsonObject("body"));
        }

        event.complete(true);
    }

}
