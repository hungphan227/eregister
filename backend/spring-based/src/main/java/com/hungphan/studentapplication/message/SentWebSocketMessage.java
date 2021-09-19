package com.hungphan.studentapplication.message;

public class SentWebSocketMessage {

    private String type;
    private String content;
    private String extraData;
    
    public SentWebSocketMessage(String type, String content) {
        this.type = type;
        this.content = content;
    }
    
    public SentWebSocketMessage(String type, String content, String extraData) {
        this.type = type;
        this.content = content;
        this.extraData = extraData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

}
