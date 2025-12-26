package com.example.aleksei.chessgame.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private boolean isWhite;
    private boolean isCaptured;
    private boolean wasMoved;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        isCaptured = false;
        wasMoved = false;
    }
    
    public boolean isWhite() {
        return isWhite;
    }

    public boolean getWhite() {
        return this.isWhite;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    public boolean isWasMoved() {
        return wasMoved;
    }

    public void setWasMoved(boolean wasMoved) {
        this.wasMoved = wasMoved;
    }

    
    @Override
    public String toString() {
        return getClass().getName() + " [isWhite=" + isWhite + ", isCaptured=" + isCaptured + ", wasMoved=" + wasMoved + "]";
    }

    public abstract List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col);
    public abstract List<Cell> getAttackedCells(Piece[][] pieces, int row, int col);
    public abstract String getSymbol();
    public abstract String getImagePath();
    public List<Cell> getAttackedRayCells(int row, int col, int[][] dirs, Piece[][] pieces) {
        List<Cell> availableMoves = new ArrayList<>();

        for (int[] dir : dirs) {
            int r = row + dir[0];
            int c = col + dir[1];

            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece currPiece = pieces[r][c];

                if (currPiece == null) {
                    availableMoves.add(new Cell(r, c));
                } else {
                    if (currPiece.isWhite() != isWhite()) {
                        availableMoves.add(new Cell(r, c));
                    }
                    break;
                }

                r += dir[0];
                c += dir[1];
            }
        }

        return availableMoves;
    }
}