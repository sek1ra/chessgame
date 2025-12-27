package com.example.aleksei.chessgame.model;

public class Game {
    private boolean isGameActive;
    private boolean isWhiteMove;
    private Board board;
    private GameStatus status;

    public Game() {
        isGameActive = true;
        isWhiteMove = true;
        board = new Board();
        status = GameStatus.RUNNING;
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
        status = GameStatus.RUNNING;
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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public MoveResult tryMove(Cell to) {
        if (!board.move(to)) {
            return new MoveResult(false, status, null);
        }

        board.setActiveCell(null);
        switchTurn();

        boolean sideToMove = isWhiteMove;
        boolean hasMoves = board.hasAnyLegalMoves(sideToMove);
        boolean inCheck = board.isKingCheck(board.getSettledPieces(), sideToMove);

        Boolean winnerIsWhite = null;

        if (!hasMoves) {
            if (inCheck) {
                status = GameStatus.CHECKMATE;
                isGameActive = false;
                winnerIsWhite = !sideToMove; // winner is the side that is NOT to move
            } else {
                status = GameStatus.STALEMATE;
                isGameActive = false;
            }
        } else {
            status = inCheck ? GameStatus.CHECK : GameStatus.RUNNING;
        }

        return new MoveResult(true, status, winnerIsWhite);
    }

}
