package com.example.aleksei.chessgame.model;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Cell> getAttackedCells(Piece[][] pieces, int row, int col) {
        return getPsevdoAvailableMoves(pieces, row, col);
    }

    @Override
    public List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col) {
        List<Cell> availMovies = new ArrayList<Cell>();
        int startCol = col;
        int startRow = row; 

        int[][] cellsToCheck = {
            {-2, -1},
            {-2, 1},
            {1, -2},
            {-1, -2},
            {2, 1},
            {2, -1},
            {-1, 2},
            {1, 2}
        };
        
        for (int i = 0; i < 8; i++) {
            int rowToCheck = startRow + cellsToCheck[i][0];
            int colToCheck = startCol +  + cellsToCheck[i][1];
            if (rowToCheck >= 0 && colToCheck >= 0 && rowToCheck <= 7 && colToCheck <= 7 ) {
                if (pieces[rowToCheck][colToCheck] == null || isWhite() != pieces[rowToCheck][colToCheck].isWhite()) {
                    availMovies.add(new Cell(rowToCheck, colToCheck));
                }
            }
        }

        return availMovies;
    }

    @Override
    public String getSymbol() {
        return "N";
    }


    @Override
    public String getImagePath() {
        if (isWhite()) {
            return "/com/example/aleksei/chessgame/images/w_knight.png";
        } else {
            return "/com/example/aleksei/chessgame/images/b_knight.png";
        }
    }
}
