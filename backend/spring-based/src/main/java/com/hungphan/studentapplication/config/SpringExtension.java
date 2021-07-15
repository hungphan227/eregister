package com.hungphan.studentapplication.config;

import akka.actor.*;
import org.springframework.context.ApplicationContext;

public class SpringExtension extends AbstractExtensionId<SpringExtension.SpringExt> {

    private static SpringExtension instance = new SpringExtension();

    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    public static SpringExtension getInstance() {
        return instance;
    }

    public static class SpringExt implements Extension {

        private static ApplicationContext applicationContext;

        public void initialize(ApplicationContext applicationContext) {
            SpringExt.applicationContext = applicationContext;
        }

        public Props props(Class<? extends AbstractActor> actorBeanClass) {
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanClass);
        }

        public Props props(Class<? extends AbstractActor> actorBeanClass, Object... parameters) {
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanClass, parameters);
        }

    }

}
