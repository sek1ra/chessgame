package com.example.aleksei.chessgame.controller;

import java.util.List;

import com.example.aleksei.chessgame.model.Board;
import com.example.aleksei.chessgame.model.Cell;
import com.example.aleksei.chessgame.model.Game;
import com.example.aleksei.chessgame.model.Piece;
import com.example.aleksei.chessgame.view.BoardView;

public class GameController {
    private final Board board;
    private final Game game;
    private final BoardView view;

    public GameController(Game game, BoardView view) {
        this.game = game;
        this.board = this.game.getBoard();
        this.view = view;
        this.view.draw(board.getSettledPieces());
        view.setOnCellClicked(this::handleCellClick);
    }

    public void handleCellClick(Cell clickedCell) {
        Piece clickedPiece = board.getPieceByCell(clickedCell);
        //clicked on piece the same color as current move color
        if (clickedPiece != null && clickedPiece.isWhite() == game.isWhiteMove()) {
            board.setActiveCell(clickedCell);
            //List<Cell> availMovies = clickedPiece.getPsevdoAvailableMoves(board.getSettledPieces(), clickedCell.row(), clickedCell.col());
            List<Cell> availMovies = board.getLegalMoves(clickedCell);
            view.highlightMoves(availMovies);
        } else {//clicked on empty field or piece of the opponent
            if (board.move(clickedCell)) {
                view.draw(board.getSettledPieces());
                game.switchTurn();
                board.setActiveCell(null);
            }
            view.clearSelection();
        }
    }
}
