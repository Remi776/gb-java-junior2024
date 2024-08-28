package ru.gb.lesson5.hw;

import ru.gb.lesson5.AbstractRequest;

public class UsersRequest extends AbstractRequest {
    public static final String TYPE = "usersRequest";
    public UsersRequest() {
        setType(TYPE);
    }
}
