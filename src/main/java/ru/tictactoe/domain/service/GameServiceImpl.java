package ru.tictactoe.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.tictactoe.datasource.mapper.GameDataMapper;
import ru.tictactoe.datasource.model.GameData;
import ru.tictactoe.datasource.repository.GameRepository;
import ru.tictactoe.domain.model.Board;
import ru.tictactoe.domain.model.Game;
import ru.tictactoe.domain.exception.GameNotFoundException;
import ru.tictactoe.domain.model.Move;
import ru.tictactoe.domain.model.ZeroCross;

import java.util.UUID;

public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GameDataMapper gameDataMapper;  // для конвертации
    private static final int PLAYER = 1;    // крестик (максимизирующий игрок)
    private static final int COMPUTER = 2;  // нолик (минимизирующий игрок)
    private static final int EMPTY = 0;

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

    @Override
    public boolean validateGameState(UUID gameId, Game proposedGame) {
        //Загружаем сохраненную иргу
        Game savedGame = getGame(gameId);

        // Сравниваем доски игры
        int[][] savedMatrix = savedGame.getBoard().getMatrix();
        int[][] proposedMatrix = proposedGame.getBoard().getMatrix();

        int changes = 0;
        int changedRow = -1;
        int changedCol = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (savedMatrix[i][j] != proposedMatrix[i][j]) {
                    changes++;
                    changedRow = i;
                    changedCol = j;
                }
            }
        }

        // Должна измениться ровно одна клетка (ход игрока)
        if (changes != 1) {
            System.out.println("Изменены более одной клетки");
            return false;
        }

        // Измененная клетка была пустой
        if (savedMatrix[changedRow][changedCol] != 0) {
            System.out.println("Измененнная клетка не была пустой");
            return false;
        }

        // Игрок поставил крестик (1)
        if (proposedMatrix[changedRow][changedCol] != 1) {
            System.out.println("Игрок поставил не 1 (крестик)");
            return false;
        }

        return true;
    }

    private void printBoard(Game game) {
        int[][] matrix = game.getBoard().getMatrix();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.printf("%d ", matrix[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Основной метод, который делает ход за текущего игрока
     */
    @Override
    public Game makeMove(Game game) {
        // 1. Проверяем, что игра не окончена
        if (game.isGameOver()) {
            throw new IllegalStateException("Game is already over");
        }

        // 2. Получаем текущего игрока
        int currentPlayer = game.getCurrentPlayer();

        System.out.println("Получили доску");
        printBoard(game);

        // 3. Находим лучший ход с помощью минимакса
        Move bestMove = findBestMove(game, currentPlayer);

        // 4. Применяем ход
        if (bestMove != null) {
            game.setMove(bestMove);
        }

        System.out.println("Сгенерили лучший ход");
        printBoard(game);

        // 5. Проверяем победу
        int winner = checkWinner(game.getBoard().getMatrix());
        if (winner != EMPTY) {
            game.setGameOver(true);
            game.setWinner(winner);
            return game;
        }

        // 6. Проверяем ничью (все клетки заполнены)
        if (isBoardFull(game.getBoard().getMatrix())) {
            game.setGameOver(true);
            game.setWinner(0); // ничья
            return game;
        }

        // 7. Если игра не закончена, меняем игрока
        game.setCurrentPlayer(currentPlayer == PLAYER ? COMPUTER : PLAYER);

        // Сохраняем новое состояние игры
        gameRepository.save(gameDataMapper.toData(game));

        return game;
    }

    /**
     * Находит лучший ход для указанного игрока
     */
    private Move findBestMove(Game game, int player) {
        int[][] board = game.getBoard().getMatrix();
        int bestScore;
        Move bestMove = null;

        // Инициализируем bestScore в зависимости от игрока
        if (player == PLAYER) {
            bestScore = Integer.MIN_VALUE;  // игрок ищет максимум
        } else {
            bestScore = Integer.MIN_VALUE;  // КОМПЬЮТЕР ТОЖЕ ИЩЕТ МАКСИМУМ!
        }

        // Перебираем все пустые клетки
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    // Делаем пробный ход
                    board[i][j] = player;

                    if (checkWinner(board) == player) {
                        System.out.println("Найден выигрышный ход (" + i + "," + j + ")!");
                        board[i][j] = EMPTY;  // отменяем ход перед возвратом
                        return new Move(i, j, player == PLAYER ? ZeroCross.CROSS : ZeroCross.ZERO);
                    }

                    // Рекурсивно оцениваем результат
                    // Передаем следующего игрока
                    int nextPlayer = (player == PLAYER) ? COMPUTER : PLAYER;
                    int score = minimax(board, 0, nextPlayer);

                    System.out.println("Оценка хода (" + i + "," + j + "): " + score);

                    // Отменяем пробный ход
                    board[i][j] = EMPTY;

                    // ОБА игрока ищут МАКСИМУМ
                    if (score > bestScore) {
                        bestScore = score;
                        if (player == PLAYER) {
                            bestMove = new Move(i, j, ZeroCross.CROSS);
                        } else {
                            bestMove = new Move(i, j, ZeroCross.ZERO);
                        }
                    }
                }
            }
        }

        return bestMove;
    }

    /**
     * Рекурсивный минимакс
     */
    private int minimax(int[][] board, int depth, int player) {
        // Проверяем терминальные состояния
        int winner = checkWinner(board);

        if (winner == COMPUTER) return 10 - depth;  // победа компьютера
        if (winner == PLAYER) return -10 + depth;   // победа игрока
        if (isBoardFull(board)) return 0;  // ничья

        if (player == PLAYER) {
            // Ход игрока - он МАКСИМИЗИРУЕТ свою выгоду
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER;
                        int score = minimax(board, depth + 1, COMPUTER);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            // Ход компьютера - он МИНИМИЗИРУЕТ выгоду игрока
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = COMPUTER;
                        int score = minimax(board, depth + 1, PLAYER);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    /**
     * Проверяет победителя
     */
    private int checkWinner(int[][] board) {
        // Проверка строк
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }

        // Проверка столбцов
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != EMPTY && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return board[0][j];
            }
        }

        // Проверка диагоналей
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

        return EMPTY; // нет победителя
    }

    /**
     * Проверяет, заполнена ли доска
     */
    private boolean isBoardFull(int[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}