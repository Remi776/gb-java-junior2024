package ru.gb.lesson5.lecture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatServer {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    // Socket - абстракция, к которой можно подключиться
    // ip-address + port - socket
    // network - сеть - набор соединенных устройств
    // ip-address - это адрес устройства в какой-то сети
    // 8080 - http
    // 443 - https
    // 35 - smtp
    // 21 - ftp
    // 5432 - стандартный порт postgres
    // клиент подключается к серверу

    /**
     * Порядок взаимодействия:
     * 1. Клиент подключается к серверу
     * 2. Клиент посылает сообщение, в котором указан логин. Если на сервере уже есть подключеный клиент с таким логином, то соедение разрывается
     * 3. Клиент может посылать 3 типа команд:
     * 3.1 list - получить логины других клиентов
     * <p>
     * 3.2 send @login message - отправить личное сообщение с содержимым message другому клиенту с логином login
     * 3.3 send message - отправить сообщение всем с содержимым message
     */

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server has been started.");

            System.out.println("Waiting for the client connection.");
            mainLoop:
            while (true) {
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket client;
        private String clientLogin;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {

            System.out.println("New client is connected!");
            try (Scanner in = new Scanner(client.getInputStream());
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                String loginRequest = in.nextLine();
                this.clientLogin = readLoginFromLoginRequest(loginRequest);

                String loginResponse = createLoginResponse(true);
                out.println(loginResponse);

                clientLoop:
                while (true) {
                    String msgFromClient = in.nextLine();
                    System.out.println("New message from client: " + msgFromClient);

                    if ("exit".equals(loginRequest)) {
                        System.out.println("Client disconnected.");
                        break clientLoop;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error during connection to the client: " + e.getMessage());
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    System.err.println("Error during disconnection from the server: " + e.getMessage());
                }
            }
        }

        private String readLoginFromLoginRequest(String loginRequestString) throws IOException {
            LoginRequest request = objectMapper.reader().readValue(loginRequestString, LoginRequest.class);
            return request.getLogin();
        }

        private String createLoginResponse(boolean success) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setConnected(success);
            try {
                return objectMapper.writer().writeValueAsString(loginResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
