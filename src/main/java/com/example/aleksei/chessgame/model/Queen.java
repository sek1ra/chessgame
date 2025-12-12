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
        List<Cell> availMovies = new ArrayList<Cell>();
        int startCol = col;
        int startRow = row; 

        // Go left
        for (int colToCheck = startCol - 1; colToCheck >= 0; colToCheck--) {
            if (pieces[startRow][colToCheck] == null) {
                availMovies.add(new Cell(startRow, colToCheck));
            } else {
                if (pieces[startRow][colToCheck].isWhite() != isWhite()) {
                    availMovies.add(new Cell(startRow, colToCheck));
                }
                break; // Прерываем цикл после встречи с любой фигурой
            }
        }

        // Go right
        for (int colToCheck = startCol + 1; colToCheck < 8; colToCheck++) {
            if (pieces[startRow][colToCheck] == null) {
                availMovies.add(new Cell(startRow, colToCheck));
            } else {
                if (pieces[startRow][colToCheck].isWhite() != isWhite()) {
                    availMovies.add(new Cell(startRow, colToCheck));
                }
                break;
            }
        }

        // Go up
        for (int rowToCheck = startRow - 1; rowToCheck >= 0; rowToCheck--) {
            if (pieces[rowToCheck][startCol] == null) {
                availMovies.add(new Cell(rowToCheck, startCol));
            } else {
                if (pieces[rowToCheck][startCol].isWhite() != isWhite()) {
                    availMovies.add(new Cell(rowToCheck, startCol));
                }
                break;
            }
        }

        // Go down
        for (int rowToCheck = startRow + 1; rowToCheck < 8; rowToCheck++) {
            if (pieces[rowToCheck][startCol] == null) {
                availMovies.add(new Cell(rowToCheck, startCol));
            } else {
                if (pieces[rowToCheck][startCol].isWhite() != isWhite()) {
                    availMovies.add(new Cell(rowToCheck, startCol));
                }
                break;
            }
        }

        // Go left top
        int colToCheck = startCol - 1;
        int rowToCheck = startRow - 1;
        while (colToCheck >= 0 && rowToCheck >= 0) {
            if (pieces[rowToCheck][colToCheck] == null) {
                availMovies.add(new Cell(rowToCheck, colToCheck));
                System.out.println(rowToCheck + ", " + colToCheck);
            } else {
                if (pieces[rowToCheck][colToCheck].isWhite() != isWhite()) {
                    availMovies.add(new Cell(rowToCheck, colToCheck));
                }
                break;
            }
            colToCheck--;rowToCheck--;
        }

        // Go right top
        colToCheck = startCol + 1;
        rowToCheck = startRow - 1;
        while (colToCheck <= 7 && rowToCheck >= 0) {
            if (pieces[rowToCheck][colToCheck] == null) {
                availMovies.add(new Cell(rowToCheck, colToCheck));
                System.out.println(rowToCheck + ", " + colToCheck);
            } else {
                if (pieces[rowToCheck][colToCheck].isWhite() != isWhite()) {
                    availMovies.add(new Cell(rowToCheck, colToCheck));
                }
                break;
            }
            colToCheck++;rowToCheck--;
        }

        // Go right bottom
        colToCheck = startCol + 1;
        rowToCheck = startRow + 1;
        while (colToCheck <= 7 && rowToCheck <= 7) {
            if (pieces[rowToCheck][colToCheck] == null) {
                availMovies.add(new Cell(rowToCheck, colToCheck));
                System.out.println(rowToCheck + ", " + colToCheck);
            } else {
                if (pieces[rowToCheck][colToCheck].isWhite() != isWhite()) {
                    availMovies.add(new Cell(rowToCheck, colToCheck));
                }
                break;
            }
            colToCheck++;rowToCheck++;
        }

        // Go left bottom
        colToCheck = startCol - 1;
        rowToCheck = startRow + 1;
        while (colToCheck >= 0 && rowToCheck <= 7) {
            if (pieces[rowToCheck][colToCheck] == null) {
                availMovies.add(new Cell(rowToCheck, colToCheck));
                System.out.println(rowToCheck + ", " + colToCheck);
            } else {
                if (pieces[rowToCheck][colToCheck].isWhite() != isWhite()) {
                    availMovies.add(new Cell(rowToCheck, colToCheck));
                }
                break;
            }
            colToCheck--;rowToCheck++;
        }

        return availMovies;
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
