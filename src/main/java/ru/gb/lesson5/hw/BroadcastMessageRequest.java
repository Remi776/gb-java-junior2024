package ru.gb.lesson5.hw;

import ru.gb.lesson5.AbstractRequest;

import java.lang.reflect.Type;

public class BroadcastMessageRequest extends AbstractRequest {
    public static final String TYPE = "broadcastMessage";
    private String message;

    public BroadcastMessageRequest() {
        setType(TYPE);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
