package com.hungphan.eregister;

import javax.annotation.PostConstruct;

import com.hungphan.eregister.model.Course;
import com.hungphan.eregister.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

import com.hungphan.eregister.actor.ActorSystemSingleton;
import com.hungphan.eregister.actor.CourseActor;
import com.hungphan.eregister.config.SpringExtension;
import com.hungphan.eregister.controller.RemainingSlotController;

import akka.actor.ActorSystem;
import akka.routing.ConsistentHashingPool;
import io.vertx.core.Vertx;

@SpringBootApplication
public class Application {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    private RemainingSlotController remainingSlotController;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
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
        LOGGER.info("Spring initialization finished");
        
//        redisTemplate.opsForValue().set("loda", "hello world");
//        System.out.println("Value of key loda: " + redisTemplate.opsForValue().get("loda"));
    }
    
    @Profile("init")
    @Bean
    CommandLineRunner initDatabase(CourseRepository courseRepository) {
        return args -> {
            courseRepository.deleteAll();
            courseRepository.save(new Course("DSA", "Data Structure and Algorithms", 6, "Edsger Dijkstra", "data-structure-and-algorithm.png"));
            courseRepository.save(new Course("OOP", "Object Oriented Programming", 4, "James Gosling", "object-oriented-programming.png"));
            courseRepository.save(new Course("Database", "Database Systems", 5, "Edgar Codd", "database.png"));
            courseRepository.save(new Course("OS", "Operating System", 3, "Linux Torvard", "operating-system.jpeg"));
            courseRepository.save(new Course("Network", "Computer Network", 1, "Tim Berners-Lee", "computer-network.jpeg"));
            courseRepository.save(new Course("CA", "Computer Architecture", 2, "Alan Turing", "computer-architecture.png"));
        };
    }

    @Profile("stress-test")
    @Bean
    CommandLineRunner initDatabase2(CourseRepository courseRepository) {
        return args -> {
            courseRepository.deleteAll();
            courseRepository.save(new Course("DSA", "Data Structure and Algorithms", 60, "Edsger Dijkstra", ""));
            courseRepository.save(new Course("OOP", "Object Oriented Programming", 40, "James Gosling", ""));
            courseRepository.save(new Course("Database", "Database Systems", 50, "Edgar Codd", ""));
            courseRepository.save(new Course("OS", "Operating System", 30, "Linux Torvard", ""));
            courseRepository.save(new Course("Network", "Computer Network", 10, "Tim Berners-Lee", ""));
            courseRepository.save(new Course("CA", "Computer Architecture", 20, "Alan Turing", ""));
        };
    }
    
}
