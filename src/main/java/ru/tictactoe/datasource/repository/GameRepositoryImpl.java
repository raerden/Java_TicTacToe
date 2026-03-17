package ru.tictactoe.datasource.repository;

import org.springframework.stereotype.Repository;
import ru.tictactoe.datasource.model.GameData;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository  // Аннотация, чтобы Spring управлял этим компонентом
public class GameRepositoryImpl implements GameRepository {

    private final ConcurrentHashMap<UUID, GameData> games = new ConcurrentHashMap<>();

    @Override
    public void save(GameData game) {
        games.put(game.getId(), game);
    }

    @Override
    public GameData load(UUID id) {
        // Возвращаем игру или null, если не найдена
        return games.get(id);
    }

    @Override
    public void remove(UUID id) {
        // Удаляем игру из мапы
        games.remove(id);
    }

    @Override
    public boolean exists(UUID id) {
        // Проверяем, есть ли игра с таким ID
        return games.containsKey(id);
    }
}