package com.hungphan.studentapplication.controller;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public class RemainingSlotHandler implements Handler<BridgeEvent> {

    private final EventBus eventBus;

    public RemainingSlotHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void handle(BridgeEvent event) {
        if (event.type() == BridgeEventType.SOCKET_CREATED) {
            System.out.println("A socket was created");
        }

        if (event.type() == BridgeEventType.SEND) {
            System.out.println("Receive: " + event.getRawMessage().getJsonObject("body"));
        }

        event.complete(true);
    }

}
