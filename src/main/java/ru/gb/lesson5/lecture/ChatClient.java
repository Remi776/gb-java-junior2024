package ru.gb.lesson5.lecture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChatClient {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        String clientLogin = "User_" + UUID.randomUUID();

        try (Socket server = new Socket("localhost", 8888)) {
            System.out.println("Connected to server!");
            try (Scanner in = new Scanner(server.getInputStream());
                 PrintWriter out = new PrintWriter(server.getOutputStream(), true)) {

                String loginRequest = createLoginRequest(clientLogin);
                out.println(loginRequest);

                String loginResponseString = in.nextLine();
                if (!checkLoginResponse(loginResponseString)) {
                    System.out.println("Failed to connect to the server");
                    return;
                }

//                String msgFromServer = loginResponseString;
//                System.out.println("Message from server: " + msgFromServer);
//
//                while (true) {
//                    out.println("Message from client + [" + clientLogin + "]");
//                    for (int i = 0; i < 10_000; i++) {
//                        out.println("[" + clientLogin + "] " + ThreadLocalRandom.current().nextInt(1000));
//                        Thread.sleep(100);
//                    }
//                    out.println("exit");
//                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Disconnected from server.");
    }


    private static boolean checkLoginResponse(String loginResponse) {
        try {
            LoginResponse response = objectMapper.reader().readValue(loginResponse, LoginResponse.class);
            return response.isConnected();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private static String createLoginRequest(String login) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(login);
        try {
            return objectMapper.writeValueAsString(loginRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

