package com.hungphan.eregister.actor.message;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import com.hungphan.eregister.message.HttpResponseMessage;

public class CourseMessage {

    private Long courseId;
    private String studentNumber;
    private DeferredResult<ResponseEntity<HttpResponseMessage>> httpResult;

    public CourseMessage(Long courseId, String studentNumber, DeferredResult<ResponseEntity<HttpResponseMessage>> httpResult) {
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

    public DeferredResult<ResponseEntity<HttpResponseMessage>> getHttpResult() {
        return httpResult;
    }

    public void setHttpResult(DeferredResult<ResponseEntity<HttpResponseMessage>> httpResult) {
        this.httpResult = httpResult;
    }
}
