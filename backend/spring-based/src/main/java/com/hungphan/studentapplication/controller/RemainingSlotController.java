package com.hungphan.studentapplication.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hungphan.studentapplication.service.CourseService;

@Component
public class RemainingSlotController extends AbstractVerticle {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RemainingSlotController.class);
    
    @Override
    public void start() throws Exception {
//        HttpServer httpServer = vertx.createHttpServer();
//
//        Router router = Router.router(vertx);
//
//        SockJSHandlerOptions options = new SockJSHandlerOptions()
//                .setHeartbeatInterval(2000);
//
//        SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options);
//
//        router.route("/remainingSlots").handler(sockJSHandler.socketHandler(sockJSSocket -> {
//            sockJSSocket.handler(buffer -> {
//                System.out.println("I received some bytes: " + buffer.length());
//            });
//        }));
//
//        httpServer.requestHandler(router).listen(9997);

        Router router = Router.router(vertx);

        router.route("/websocket/*").handler(eventBusHandler());
        router.route().handler(staticHandler());

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(9997);

        LOGGER.info("Websocket server started!!!");
    }

    private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("remainingSlots"))
                .addInboundPermitted(new PermittedOptions().setAddressRegex("in"));

        EventBus eventBus = vertx.eventBus();

        RemainingSlotHandler counterHandler = new RemainingSlotHandler(eventBus);

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        return sockJSHandler.bridge(options, counterHandler);
    }

    private StaticHandler staticHandler() {
        return StaticHandler.create()
                .setCachingEnabled(false);
    }

}
