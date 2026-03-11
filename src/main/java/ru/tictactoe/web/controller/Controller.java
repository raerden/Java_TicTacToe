package ru.tictactoe.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // говорит Spring, что этот класс будет обрабатывать HTTP запросы и возвращать данные (не HTML страницы)
public class Controller {

    @GetMapping("/")    //  связывает метод с корневым URL (http://localhost:8080/)
    public String hello() {
        return "Крестики-нолики запущены!";
    }
}