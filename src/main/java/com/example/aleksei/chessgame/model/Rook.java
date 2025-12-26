package com.example.aleksei.chessgame.model;

import java.util.List;

public class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Cell> getAttackedCells(Piece[][] pieces, int row, int col) {
        return getPsevdoAvailableMoves(pieces, row, col);
    }

    @Override
    public List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col) {
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return getAttackedRayCells(row, col, dirs, pieces);
    }

    @Override
    public String getSymbol() {
        return "R";
    }

    @Override
    public String getImagePath() {
        if (isWhite()) {
            return "/com/example/aleksei/chessgame/images/w_rook.png";
        } else {
            return "/com/example/aleksei/chessgame/images/b_rook.png";
        }
    }
}
