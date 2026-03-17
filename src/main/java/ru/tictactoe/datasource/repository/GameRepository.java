package ru.tictactoe.datasource.repository;

import ru.tictactoe.datasource.model.GameData;
import java.util.UUID;

public interface GameRepository {
    void save(GameData game);
    GameData load(UUID id);
    void remove(UUID id);
    boolean exists(UUID id);
}