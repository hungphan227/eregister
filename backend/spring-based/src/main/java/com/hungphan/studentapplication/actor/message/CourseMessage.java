package com.hungphan.studentapplication.actor.message;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public class CourseMessage {

    private Long courseId;
    private String studentNumber;
    private DeferredResult<ResponseEntity<String>> httpResult;

    public CourseMessage(Long courseId, String studentNumber, DeferredResult<ResponseEntity<String>> httpResult) {
        this.courseId = courseId;
        this.studentNumber = studentNumber;
        this.httpResult = httpResult;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public DeferredResult<ResponseEntity<String>> getHttpResult() {
        return httpResult;
    }

    public void setHttpResult(DeferredResult<ResponseEntity<String>> httpResult) {
        this.httpResult = httpResult;
    }
}
