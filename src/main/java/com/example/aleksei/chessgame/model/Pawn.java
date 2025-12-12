package com.example.aleksei.chessgame.model;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean isJumped;

    public Pawn(boolean isWhite) {
        super(isWhite);
        isJumped = false;
    }

    @Override
    public List<Cell> getAttackedCells(Piece[][] pieces, int row, int col) {
        List<Cell> result = new ArrayList<>();

        int direction = isWhite() ? -1 : 1;

        int r = row + direction;

        if (r >= 0 && r < 8 && col - 1 >= 0) {
            result.add(new Cell(r, col - 1));
        }

        if (r >= 0 && r < 8 && col + 1 < 8) {
            result.add(new Cell(r, col + 1));
        }

        return result;
    }


    public boolean isJumped() {
        return isJumped;
    }

    public void setIsJumped(boolean pIsJumped) {
        isJumped = pIsJumped;
    }

    @Override
    public List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col) {
        List<Cell> availMovies = new ArrayList<Cell>();
        int startCol = col;
        int startRow = row;

        int direction = 1;
        if (isWhite()) {
            direction = -1;
        }

        if (Math.abs(startRow + 1*direction) < 7 && pieces[startRow + 1*direction][startCol] == null) {
            availMovies.add(new Cell(startRow + 1*direction, startCol));
        }
        if (!isWasMoved()) {
            if (Math.abs(startRow + 2*direction) < 7 && pieces[startRow + 2*direction][startCol] == null && pieces[startRow + direction][startCol] == null) {
                availMovies.add(new Cell(startRow + 2*direction, startCol));
            }
        }

        if (
            Math.abs(startRow + 1*direction) < 7 && 
            startCol+1 < 7 && 
            pieces[startRow + 1*direction][startCol+1] != null && 
            pieces[startRow + 1*direction][startCol+1].isWhite() != isWhite()
        ) {
            availMovies.add(new Cell(startRow + 1*direction, startCol+1));
        }

        if (
            Math.abs(startRow + 1*direction) < 7 && 
            startCol-1 >= 0 && 
            pieces[startRow + 1*direction][startCol-1] != null && 
            pieces[startRow + 1*direction][startCol-1].isWhite() != isWhite()
        ) {
            availMovies.add(new Cell(startRow + 1*direction, startCol-1));
        }

        return availMovies;
    }

    @Override
    public String getSymbol() {
        return "P";
    }

    @Override
    public String getImagePath() {
        if (isWhite()) {
            return "/com/example/aleksei/chessgame/images/w_pawn.png";
        } else {
            return "/com/example/aleksei/chessgame/images/b_pawn.png";
        }
    }
}
