package com.example.aleksei.chessgame.model;

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
}