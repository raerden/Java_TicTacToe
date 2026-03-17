package ru.tictactoe.datasource.model;

import java.util.UUID;

public class GameData {
    private UUID id;
    private BoardData board;
    private int currentPlayer;
    private int winner;
    private boolean gameOver;

    public GameData() {
    }

    public GameData(UUID id, BoardData board, int currentPlayer, int winner, boolean gameOver) {
        this.id = id;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.gameOver = gameOver;
    }

    // Геттеры и сеттеры
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public BoardData getBoard() { return board; }
    public void setBoard(BoardData board) { this.board = board; }

    public int getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(int currentPlayer) { this.currentPlayer = currentPlayer; }

    public int getWinner() { return winner; }
    public void setWinner(int winner) { this.winner = winner; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
}