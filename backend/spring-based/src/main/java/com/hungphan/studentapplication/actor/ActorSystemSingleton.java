package com.hungphan.studentapplication.actor;

import akka.actor.ActorSystem;

public class ActorSystemSingleton {

    private static ActorSystem system = ActorSystem.create("actor-system");

    private ActorSystemSingleton () {}

    public static ActorSystem getInstance() {
        return system;
    }

}
