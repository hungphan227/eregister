package com.hungphan.studentapplication;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.hungphan.studentapplication.actor.ActorSystemSingleton;
import com.hungphan.studentapplication.actor.CourseActor;
import com.hungphan.studentapplication.config.SpringExtension;
import com.hungphan.studentapplication.controller.RemainingSlotController;

import akka.actor.ActorSystem;
import akka.routing.ConsistentHashingPool;
import io.vertx.core.Vertx;

@SpringBootApplication
public class Application {

    @Autowired
    private RemainingSlotController remainingSlotController;
    
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);

        ActorSystem system = ActorSystemSingleton.getInstance();
        SpringExtension.getInstance().get(system).initialize(context);
        system.actorOf(new ConsistentHashingPool(8).props(SpringExtension.getInstance().get(system).props(CourseActor.class)), CourseActor.class.getSimpleName());
//        system.actorOf(SpringExtension.getInstance().get(system).props(UpdateClientRemainingSlotsActor.class), UpdateClientRemainingSlotsActor.class.getSimpleName());
    }

    @PostConstruct
    public void postConstruct() {
        Vertx vertx = VertxSingleton.getInstance();
        vertx.deployVerticle(remainingSlotController);
        System.out.println("Initialization finished");
    }
    
//    @Profile("first")
//    @Bean
//    CommandLineRunner initDatabase(StudentRepository studentRepository, CourseRepository courseRepository) {
//        return args -> {
//            studentRepository.save(new Student("harry", "1", "Harry Potter", 20));
//            studentRepository.save(new Student("peter", "1", "Peter Pevensia", 22));
//            studentRepository.save(new Student("gandalf", "1", "Gandalf The White", 100));
//            
//            courseRepository.save(new Course("magic", "Magic", 1));
//            courseRepository.save(new Course("sword", "Sword", 2));
//            courseRepository.save(new Course("archery", "Archery", 3));
//            courseRepository.save(new Course("strategy", "Strategy", 4));
//            courseRepository.save(new Course("leadership", "Leadership", 5));
//            courseRepository.save(new Course("transformation", "Transformation", 6));
//        };
//    }
    
}
