package com.example.aleksei.chessgame.controller;

import java.util.List;

import com.example.aleksei.chessgame.model.Board;
import com.example.aleksei.chessgame.model.Cell;
import com.example.aleksei.chessgame.model.Game;
import com.example.aleksei.chessgame.model.GameStatus;
import com.example.aleksei.chessgame.model.MoveRecord;
import com.example.aleksei.chessgame.model.Piece;
import com.example.aleksei.chessgame.view.BoardView;
import com.example.aleksei.chessgame.model.MoveResult;

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
        if (!game.isGameActive()) {
            return;
        }
        Piece clickedPiece = board.getPieceByCell(clickedCell);
        //clicked on piece the same color as current move color
        if (clickedPiece != null && clickedPiece.isWhite() == game.isWhiteMove()) {
            board.setActiveCell(clickedCell);
            //List<Cell> availMovies = clickedPiece.getPsevdoAvailableMoves(board.getSettledPieces(), clickedCell.row(), clickedCell.col());
            List<Cell> availMovies = board.getLegalMoves(clickedCell);
            view.highlightMoves(availMovies);
        } else {//clicked on empty field or piece of the opponent
            if (board.getActiveCell() == null) {
                view.clearSelection();
                return;
            }

            MoveResult moveResult = game.tryMove(clickedCell);
            if (moveResult.moved()) {
                view.draw(board.getSettledPieces());

                if (moveResult.moveText() != null) {
                    view.addMove(moveResult.moveText());
                }

                if (moveResult.status() == GameStatus.CHECKMATE) {
                    String message = "CHECKMATE! ";
                    if (moveResult.winnerIsWhite()) {
                        message += "Winner is White";
                    } else {
                        message += "Winner is Black";
                    }
                    view.showAlert("Warning!", message);
                } else if (moveResult.status() == GameStatus.STALEMATE) {
                    String message = "STALEMATE!";
                    view.showAlert("Warning!", message);
                }
            }
            view.clearSelection();
        }
    }
}
