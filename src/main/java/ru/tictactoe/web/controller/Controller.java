package ru.tictactoe.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.tictactoe.datasource.repository.GameRepository;
import ru.tictactoe.domain.model.Game;
import ru.tictactoe.domain.model.Move;
import ru.tictactoe.domain.service.GameService;
import ru.tictactoe.web.mapper.WebMapper;
import ru.tictactoe.web.model.GameDto;

import java.util.Arrays;
import java.util.UUID;

@RestController // Аннотация говорит Spring, что этот класс будет обрабатывать HTTP запросы и возвращать данные (не HTML страницы)
public class Controller {
    private final GameRepository gameRepository;
    private final GameService gameService;
    private final WebMapper webMapper;

    public Controller(GameRepository gameRepository, GameService gameService, WebMapper webMapper) {
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.webMapper = webMapper;
    }

    @GetMapping("/")    //  связывает метод с корневым URL (http://localhost:8080/)
    public String hello() {
        return "Крестики-нолики запущены!";
    }

    @PostMapping("/game/{id}")
    public GameDto makeMoveRequest(
            @PathVariable ("id") UUID id, // Spring сам сконвертирует строку в UUID
            @RequestBody GameDto gameDto // Spring сам распарсит JSON в объект
    ) {

        if (gameDto.getId() == null || !id.equals(gameDto.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Game ID in path and body must be the same"
            );
        }


        Game currentGame = webMapper.toDomain(gameDto);
        Move move = gameService.makeMove(currentGame);
        currentGame.setMove(move);
        GameDto updatedGameDto = webMapper.toDTO(currentGame);

        // 1. Найти игру по id (пока нет БД, можно хранить в памяти)
        // 2. Вызвать сервис с логикой (GameService), передав id и, например, координаты хода из moveRequest
        // 3. Сервис обновит игру
        // 4. Маппером сконвертировать обновленный объект Game из домена в GameDto
        // 5. Вернуть GameDto. Spring сам превратит его в JSON для ответа.
        System.out.println("Получили ход для игры: " + id);
        System.out.println("Хочет походить игрок: " + gameDto.getCurrentPlayer());
        System.out.println("Состояние доски: " + Arrays.deepToString(gameDto.getBoard().getMatrix()));
         return updatedGameDto;
//        return gameDto; // Пока заглушка
    }

    @GetMapping("/game")
    public GameDto createGame() {
        // Просто создаем новую игру без всяких параметров
        System.out.println("Запрос на создание новой игры");
        Game game = gameService.createGame();
        return webMapper.toDTO(game);
    }

    //Вернуть состояние игры если такая сохранена
    @GetMapping("/game/{id}")
    public GameDto getGame(@PathVariable("id") UUID gameId) {
        Game game = gameService.getGame(gameId);
        return webMapper.toDTO(game);
    }
}