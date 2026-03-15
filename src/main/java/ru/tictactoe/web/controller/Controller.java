package ru.tictactoe.web.controller;

import org.springframework.web.bind.annotation.*;
import ru.tictactoe.web.model.GameDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@RestController // Аннотация говорит Spring, что этот класс будет обрабатывать HTTP запросы и возвращать данные (не HTML страницы)
public class Controller {

    @GetMapping("/")    //  связывает метод с корневым URL (http://localhost:8080/)
    public String hello() {
        return "Крестики-нолики запущены!";
    }

    @PostMapping("/game/{id}")
    public GameDto makeMoveRequest(
//            @PathVariable UUID id, // Spring сам сконвертирует строку в UUID
            @PathVariable String id, // Spring сам сконвертирует строку в UUID
            @RequestBody GameDto moveRequest // Spring сам распарсит JSON в объект
    ) {
        // 1. Найти игру по id (пока нет БД, можно хранить в памяти)
        // 2. Вызвать сервис с логикой (GameService), передав id и, например, координаты хода из moveRequest
        // 3. Сервис обновит игру
        // 4. Маппером сконвертировать обновленный объект Game из домена в GameDto
        // 5. Вернуть GameDto. Spring сам превратит его в JSON для ответа.
        System.out.println("Получили ход для игры: " + id);
        System.out.println("Хочет походить игрок: " + moveRequest.getCurrentPlayer());
        System.out.println("Состояние доски: " + Arrays.deepToString(moveRequest.getBoard().getMatrix()));
        // return updatedGameDto;
        return moveRequest; // Пока заглушка
    }

}