package ru.tictactoe.domain.service;

import org.springframework.stereotype.Service;
import ru.tictactoe.domain.model.Board;
import ru.tictactoe.domain.model.Game;
import ru.tictactoe.domain.exception.GameNotFoundException;
import ru.tictactoe.domain.model.Move;
import ru.tictactoe.domain.model.ZeroCross;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameServiceImpl implements GameService {

    private final ConcurrentHashMap<UUID, Game> games = new ConcurrentHashMap<>();

    public Game createGame() {
        // Новая игра: пустая доска, ход игрока (1), нет победителя, игра не окончена
        Game game = new Game(
                UUID.randomUUID(),
                new Board(new int[3][3]),
                1,      // currentPlayer = 1 (игрок начинает)
                0,      // 0 - ничья, 1 - победил игрок, 2 - победил комп
                false   // gameOver = false
        );

        games.put(game.getId(), game);
        return game;
    }

    public Game getGame(UUID id) {
        Game game = games.get(id);
        if (game == null) {
            throw new GameNotFoundException(id);
        }
        return game;
    }

    public Move makeMove(Game game) {

        // TODO: Здесь будет логика:
        // 1. Проверить, что игра не окончена
        // 2. Проверить, что сейчас ход игрока (currentPlayer == 1)
        // 3. Проверить, что клетка пуста
        // 4. Поставить крестик (1) в указанную клетку
        // 5. Проверить победу/ничью
        // 6. Если игра не закончена, передать ход компьютеру (currentPlayer = 2)
        // 7. Сделать ход компьютера
        // 8. Снова проверить победу

        return new Move(1,1, ZeroCross.CROSS);
    }
}