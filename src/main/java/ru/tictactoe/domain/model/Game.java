package ru.tictactoe.domain.model;

import java.util.UUID;

public class Game {
    private UUID id;
    private Board board; // int[][] matrix
    private int currentPlayer; // чей сейчас ход
    private int winner; //0 - ничья, 1 - игрок1, 2 - игрок2
    private boolean gameOver;

    public Game() {
    }

    public Game(UUID id, Board board, int currentPlayer, int winner, boolean gameOver) {
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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isGameOver() {return gameOver; }

    public void setGameOver(boolean gameOver) {this.gameOver = gameOver;}

    public int getCurrentPlayer() {return currentPlayer;}

    public void setCurrentPlayer(int player) {this.currentPlayer = currentPlayer;}

    public int getWinner() {return winner;}

    public void setWinner(int winner) {this.winner = winner;}

}
