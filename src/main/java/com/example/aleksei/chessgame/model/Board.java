package com.example.aleksei.chessgame.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] settledPieces = new Piece[8][8];
    private Cell activeCell;

    public Board() {
        initSettlePieces();
    }

    public Cell getActiveCell() {
        return activeCell;
    }

    public void setActiveCell(Cell activeCell) {
        this.activeCell = activeCell;
    }

    public Piece getPieceByCell(Cell cell) {
        return settledPieces[cell.row()][cell.col()];
    }

    public void initSettlePieces() {
        //reset board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                settledPieces[i][j] = null;
            }
        }

        //set Pawn
        for (int i = 0; i < 8; i++) {
            settledPieces[6][i] = new Pawn(true);
            settledPieces[1][i] = new Pawn(false);
        }

        settledPieces[7][0] = new Rook(true);
        settledPieces[0][0] = new Rook(false);
        settledPieces[7][7] = new Rook(true);
        settledPieces[0][7] = new Rook(false);

        settledPieces[7][1] = new Knight(true);
        settledPieces[0][1] = new Knight(false);
        settledPieces[7][6] = new Knight(true);
        settledPieces[0][6] = new Knight(false);

        settledPieces[7][2] = new Bishop(true);
        settledPieces[0][2] = new Bishop(false);
        settledPieces[7][5] = new Bishop(true);
        settledPieces[0][5] = new Bishop(false);

        settledPieces[7][3] = new Queen(true);
        settledPieces[0][3] = new Queen(false);
        settledPieces[7][4] = new King(true);
        settledPieces[0][4] = new King(false);
    }

    public Piece[][] getSettledPieces() {
        return settledPieces;
    }

    private Piece[][] copyBoard(Piece[][] board) {
        Piece[][] copy = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            System.arraycopy(board[r], 0, copy[r], 0, 8);
        }
        return copy;
    }

    private Piece[][] makePseudoMove(Piece[][] pieces, Cell from, Cell to) {
        Piece[][] copy = copyBoard(pieces);
        if (copy[from.row()][from.col()] != null) {
            copy[to.row()][to.col()] = copy[from.row()][from.col()];
            copy[from.row()][from.col()] = null;
        }
        return copy;
    }
    public boolean move(Cell cell) {
        if (activeCell == null) {
            return false;
        }

        List<Cell> availableMoves = getLegalMoves(activeCell);
        for (Cell aCell : availableMoves) {
            System.out.println(aCell);
        }

        //Check if piece is pawn and it can take another piece on PROHODE TODO review this comment
        //Check if king can make ROKEROVKA TODO review this comment
        //if pawn pawn can take another piece on PROHODE TODO review this comment
        Piece pieceToCheck = settledPieces[activeCell.row()][activeCell.col()];
        if (pieceToCheck instanceof Pawn) {
            int direction = pieceToCheck.isWhite() ? -1 : 1;
            int horisontalDirection = cell.col() - activeCell.col();
            if (
                activeCell.col() - 1 > -1 && 
                settledPieces[activeCell.row()][activeCell.col() + horisontalDirection] != null &&
                settledPieces[activeCell.row()][activeCell.col() + horisontalDirection] instanceof Pawn pawnToCheck &&
                pawnToCheck.isJumped()
            ) {
                settledPieces[activeCell.row() + direction][activeCell.col() + horisontalDirection] = pieceToCheck;
                settledPieces[activeCell.row()][activeCell.col() + horisontalDirection] = null;
                settledPieces[activeCell.row()][activeCell.col()] = null;
                return true;
            }
        }

        // Check if king can make ROKEROVKA TODO review this comment
        //For white
        if (pieceToCheck instanceof King && pieceToCheck.isWhite() && !pieceToCheck.isWasMoved() )  {
            //Check left
            if (settledPieces[7][5] == null && settledPieces[7][6] == null) {
                if (settledPieces[7][7] instanceof Rook rookToCheck && !rookToCheck.isWasMoved()) {
                    if (
                        !isCellAttacked(settledPieces, new Cell(7, 5), false)
                        &&
                        !isCellAttacked(settledPieces, new Cell(7, 6), false)
                    ) {

                        //legalMoves.add(new Cell(7, 6));
                    }
                }
            }
        }

        if (availableMoves.contains(cell)) {
            //if there is a piece
            printBoard(settledPieces);
            if (settledPieces[cell.row()][cell.col()] != null) {
                if (settledPieces[cell.row()][cell.col()].isWhite() == settledPieces[activeCell.row()][activeCell.col()].isWhite()) {
                    return false;
                }
            }

            resetJumpFlagsExcept();
            if (settledPieces[activeCell.row()][activeCell.col()] instanceof Pawn pawn ) {
                pawn.setIsJumped(Math.abs(activeCell.row() - cell.row()) == 2);
            }

            settledPieces[activeCell.row()][activeCell.col()].setWasMoved(true);
            settledPieces[cell.row()][cell.col()] = settledPieces[activeCell.row()][activeCell.col()];
            settledPieces[activeCell.row()][activeCell.col()] = null;
            return true;
        }

        return false;
    }

    private void resetJumpFlagsExcept() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = settledPieces[r][c];
                if (p instanceof Pawn pawn ) {//&& p != current
                    pawn.setIsJumped(false);
                }
            }
        }
    }

    void printBoard(Piece[][] board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == null) {
                    System.out.print("__ ");
                } else {
                    System.out.print(board[row][col].getSymbol() + ( board[row][col].isWhite() ? "W" : "B" ) + " ");
                }                
            }
            System.out.println("");
        }
    }

    public boolean isKingCheck(Piece[][] pieces, boolean isWhiteKingChecked) {
        Cell cellToCheck = null;
        //looking for king to check
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (
                    pieces[row][col] != null && 
                    pieces[row][col] instanceof King && 
                    pieces[row][col].isWhite() == isWhiteKingChecked
                ) {
                    cellToCheck = new Cell(row, col);
                }
            }
        }

        if (cellToCheck != null) {
            return isCellAttacked(pieces, cellToCheck, !isWhiteKingChecked);
        }
        return false;
    }

    private boolean isCellAttacked(Piece[][] pieces, Cell cell, boolean attackedByWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece attacker = pieces[row][col];
                if (attacker != null && attacker.isWhite() == attackedByWhite ) {
                    List<Cell> attackedCells = attacker.getAttackedCells(pieces, row, col);
System.out.println("attackedCells  by r=" + row + ", c=" + col + " = " + attackedCells);
                    if (attackedCells != null && attackedCells.contains(cell)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
    @param cell - cell for check
    @return List of cells available for legal move

    this function get all available movies
    filter it:
    1. If king get check for self king after move
    */
    public List<Cell> getLegalMoves(Cell cell) {
        Piece pieceToCheck = settledPieces[cell.row()][cell.col()];
        if (pieceToCheck == null) {
            return null;
        }

        List<Cell> legalMoves = new ArrayList<>();

        //If king get check for self king after move
        List<Cell> availMovies = pieceToCheck.getPsevdoAvailableMoves(settledPieces, cell.row(), cell.col());
        for (Cell availCell : availMovies) {
            Piece[][] pseudoPieces = makePseudoMove(settledPieces, cell, new Cell(availCell.row(), availCell.col()));
            printBoard(pseudoPieces);
            if (!isKingCheck(pseudoPieces, pseudoPieces[availCell.row()][availCell.col()].isWhite())) {
                legalMoves.add(availCell);
            }
        }

        //if pawn pawn can take another piece on PROHODE TODO review this comment
        if (pieceToCheck instanceof Pawn currentPawn) {
            int direction = pieceToCheck.isWhite() ? -1 : 1;
            if (
                cell.col() - 1 > -1 && 
                settledPieces[cell.row()][cell.col() - 1] != null &&
                settledPieces[cell.row()][cell.col() - 1] instanceof Pawn pawnToCheck &&
                pawnToCheck.isJumped()
            ) {
                legalMoves.add(new Cell(cell.row() + direction, cell.col() - 1));
            }
            if (
                cell.col() + 1 < 8 && 
                settledPieces[cell.row()][cell.col() + 1] != null &&
                settledPieces[cell.row()][cell.col() + 1] instanceof Pawn pawnToCheck &&
                pawnToCheck.isJumped()
            ) {
                legalMoves.add(new Cell(cell.row() + direction, cell.col() + 1));
            }
        }


        // Check if king can make ROKEROVKA TODO review this comment
        //For white
        if (pieceToCheck instanceof King && pieceToCheck.isWhite() && !pieceToCheck.isWasMoved() )  {
            //Check left
            if (settledPieces[7][5] == null && settledPieces[7][6] == null) {
                if (settledPieces[7][7] instanceof Rook rookToCheck && !rookToCheck.isWasMoved()) {
                    if (
                        !isCellAttacked(settledPieces, new Cell(7, 5), false)
                        &&
                        !isCellAttacked(settledPieces, new Cell(7, 6), false)
                    ) {
                        legalMoves.add(new Cell(7, 6));
                    }
                }
            }
        }
        System.out.println("legalMoves: " + legalMoves);
        return legalMoves;
    }
}