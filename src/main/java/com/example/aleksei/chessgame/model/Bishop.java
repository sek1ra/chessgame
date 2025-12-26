package com.example.aleksei.chessgame.model;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }
    @Override
    public List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col) {
        int[][] dirs = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
        return getAttackedRayCells(row, col, dirs, pieces);
    }

    @Override
    public String getSymbol() {
        return "B";
    }

    @Override
    public String getImagePath() {
        if (isWhite()) {
            return "/com/example/aleksei/chessgame/images/w_bishop.png";
        } else {
            return "/com/example/aleksei/chessgame/images/b_bishop.png";
        }
    }
    @Override
    public List<Cell> getAttackedCells(Piece[][] pieces, int row, int col) {
        return getPsevdoAvailableMoves(pieces, row, col);
    }
}
