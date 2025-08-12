package com.stream.app.spring_stream_backend.playload;

public class CustomMessage {

    private String message;

    private boolean success = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public CustomMessage(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public CustomMessage() {
    }
}
