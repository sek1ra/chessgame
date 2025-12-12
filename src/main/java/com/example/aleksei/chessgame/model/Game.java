package com.example.aleksei.chessgame.model;

public class Game {
    private boolean isGameActive;
    private boolean isWhiteMove;
    private Board board;

    public Game() {
        isGameActive = false;
        isWhiteMove = true;
        board = new Board();
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public void setGameActive(boolean isGameActive) {
        this.isGameActive = isGameActive;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }    

    public void run() {
        isGameActive = true;
    }

    public boolean isWhiteMove() {
        return isWhiteMove;
    }

    public void setWhiteMove(boolean isWhiteMove) {
        this.isWhiteMove = isWhiteMove;
    }

    public void switchTurn() {
        isWhiteMove = !isWhiteMove;
    }

    public boolean isKingCheck() {
        
        return false;
    }
}
