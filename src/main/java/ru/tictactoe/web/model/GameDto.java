package ru.tictactoe.web.model;

import java.util.UUID;

public class GameDto {
    private UUID id;
    private BoardDto board; // int[][] matrix
    private int currentPlayer;
    private int winner; //0 - ничья, 1 - игрок1, 2 - игрок2
    private boolean gameOver;

    public GameDto() {
    }

    public GameDto(UUID id, BoardDto board, int currentPlayer, int winner, boolean gameOver) {
        this.id = id;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.gameOver = gameOver;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BoardDto getBoard() {
        return board;
    }

    public void setBoard(BoardDto board) {
        this.board = board;
    }

    public boolean isGameOver() {return gameOver; }

    public void setGameOver(boolean gameOver) {this.gameOver = gameOver;}

    public int getCurrentPlayer() {return currentPlayer;}

    public void setCurrentPlayer(int currentPlayer) {this.currentPlayer = currentPlayer;}

    public int getWinner() {return winner;}

    public void setWinner(int winner) {this.winner = winner;}
}
