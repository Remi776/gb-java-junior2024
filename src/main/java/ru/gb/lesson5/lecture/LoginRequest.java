package ru.gb.lesson5.lecture;

/**
 * {
 *     "type": "login",
 *     "login": "admin"
 * }
 */
public class LoginRequest {

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
