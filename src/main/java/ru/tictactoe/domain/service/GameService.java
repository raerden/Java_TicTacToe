package ru.tictactoe.domain.service;

import ru.tictactoe.domain.model.Game;
import ru.tictactoe.domain.model.Move;

import java.util.UUID;

public interface GameService {
    Game createGame();
    Game getGame(UUID id);
    Move makeMove(Game game);
}
