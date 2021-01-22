package com.hungphan.studentapplication.controller;

public class Test {

    public static void main(String[] args) {
        new Thread(() -> {
            number();
        }).start();

        number();
    }

    static void number () {
        System.out.println("a");
        System.out.println("b");
        System.out.println("c");
    }

}
