package com.hungphan.studentapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import java.math.BigDecimal;

@Entity
public class Student {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "STUDENT_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "STUDENT_GEN", sequenceName = "STUDENT_SEQ", allocationSize = 1)
    private Long id;
    
    private String studentNumber;
    private String password;
    
    private String name;
    private Integer age;

    // avoid this "No default constructor for entity"
    public Student() {
    }

    public Student(String studentNumber, String password, String name, Integer age) {
        super();
        this.studentNumber = studentNumber;
        this.password = password;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age +
                '}';
    }

}
