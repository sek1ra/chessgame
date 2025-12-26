package com.example.aleksei.chessgame.model;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Cell> getAttackedCells(Piece[][] pieces, int row, int col) {
        return getPsevdoAvailableMoves(pieces, row, col);
    }

    @Override
    public List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col) {
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
        return getAttackedRayCells(row, col, dirs, pieces);
    }

    @Override
    public String getSymbol() {
        return "Q";
    }

    @Override
    public String getImagePath() {
        if (isWhite()) {
            return "/com/example/aleksei/chessgame/images/w_queen.png";
        } else {
            return "/com/example/aleksei/chessgame/images/b_queen.png";
        }
    }    
}
