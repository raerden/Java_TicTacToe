package ru.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // запускает встроенный сервер Tomcat (по умолчанию на порту 8080)
        // http://localhost:8080/
        SpringApplication.run(Application.class, args);
    }
}