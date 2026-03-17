package ru.tictactoe.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tictactoe.datasource.mapper.GameDataMapper;
import ru.tictactoe.datasource.model.GameData;
import ru.tictactoe.datasource.repository.GameRepository;
import ru.tictactoe.domain.model.Board;
import ru.tictactoe.domain.model.Game;
import ru.tictactoe.domain.exception.GameNotFoundException;
import ru.tictactoe.domain.model.Move;
import ru.tictactoe.domain.model.ZeroCross;

import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GameDataMapper gameDataMapper;  // для конвертации

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, GameDataMapper gameDataMapper) {
        this.gameRepository = gameRepository;
        this.gameDataMapper = gameDataMapper;
    }

    public Game createGame() {
        // Новая игра: пустая доска, ход игрока (1), нет победителя, игра не окончена
        Game game = new Game(
                UUID.randomUUID(),
                new Board(new int[3][3]),
                1,      // currentPlayer = 1 (игрок начинает)
                0,      // 0 - ничья, 1 - победил игрок, 2 - победил комп
                false   // gameOver = false
        );

        GameData gameData = gameDataMapper.toData(game);
        gameRepository.save(gameData);

        return game;
    }

    public Game getGame(UUID id) {
        // Загружаем через репозиторий
        GameData gameData = gameRepository.load(id);
        if (gameData == null) {
            throw new GameNotFoundException(id);
        }
        // Конвертируем обратно в доменную модель
        return gameDataMapper.toDomain(gameData);
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