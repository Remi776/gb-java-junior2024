package ru.gb.lesson5.hw;

import ru.gb.lesson5.AbstractRequest;

public class DisconnectRequest extends AbstractRequest {
    public static final String TYPE = "disconnectRequest";

    public DisconnectRequest() {
        setType(TYPE);
    }
}
