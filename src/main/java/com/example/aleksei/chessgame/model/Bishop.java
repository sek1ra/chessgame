package com.example.aleksei.chessgame.model;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }
    @Override
    public List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col) {
        List<Cell> availMovies = new ArrayList<Cell>();
        int startCol = col;
        int startRow = row; 
        // Go left top
        col--;
        row--;
        while (col >= 0 && row >= 0) {
            if (pieces[row][col] == null) {
                availMovies.add(new Cell(row, col));
            } else {
                if (pieces[row][col].isWhite() != isWhite()) {
                    availMovies.add(new Cell(row, col));
                }
                break;
            }
            col--;row--;
        }

        // Go right top
        col = startCol + 1;
        row = startRow - 1;
        while (col <= 7 && row >= 0) {
            if (pieces[row][col] == null) {
                availMovies.add(new Cell(row, col));
                System.out.println(row + ", " + col);
            } else {
                if (pieces[row][col].isWhite() != isWhite()) {
                    availMovies.add(new Cell(row, col));
                }
                break;
            }
            col++;row--;
        }

        // Go right bottom
        col = startCol + 1;
        row = startRow + 1;
        while (col <= 7 && row <= 7) {
            if (pieces[row][col] == null) {
                availMovies.add(new Cell(row, col));
                System.out.println(row + ", " + col);
            } else {
                if (pieces[row][col].isWhite() != isWhite()) {
                    availMovies.add(new Cell(row, col));
                }
                break;
            }
            col++;row++;
        }

        // Go left bottom
        col = startCol - 1;
        row = startRow + 1;
        while (col >= 0 && row <= 7) {
            if (pieces[row][col] == null) {
                availMovies.add(new Cell(row, col));
                System.out.println(row + ", " + col);
            } else {
                if (pieces[row][col].isWhite() != isWhite()) {
                    availMovies.add(new Cell(row, col));
                }
                break;
            }
            col--;row++;
        }

        return availMovies;
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
