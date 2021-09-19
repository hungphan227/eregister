package com.hungphan.studentapplication.message;

public class HttpResponseMessage {
    
    private String message;
    
    public HttpResponseMessage(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
