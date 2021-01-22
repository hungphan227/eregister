package com.hungphan.studentapplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.hungphan.studentapplication.model.Course;
import com.hungphan.studentapplication.model.Student;
import com.hungphan.studentapplication.repository.CourseRepository;
import com.hungphan.studentapplication.repository.StudentRepository;

@SpringBootApplication
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Profile("first")
    @Bean
    CommandLineRunner initDatabase(StudentRepository studentRepository, CourseRepository courseRepository) {
        return args -> {
            studentRepository.save(new Student("harry", "1", "Harry Potter", 20));
            studentRepository.save(new Student("peter", "1", "Peter Pevensia", 22));
            studentRepository.save(new Student("gandalf", "1", "Gandalf The White", 100));
            
            courseRepository.save(new Course("magic", "Magic", 1));
            courseRepository.save(new Course("sword", "Sword", 2));
            courseRepository.save(new Course("archery", "Archery", 3));
            courseRepository.save(new Course("strategy", "Strategy", 4));
            courseRepository.save(new Course("leadership", "Leadership", 5));
            courseRepository.save(new Course("transformation", "Transformation", 6));
        };
    }
    
}
