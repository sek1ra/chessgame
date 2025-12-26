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

    /**
     * Initializes the board to the standard chess starting position.
     * Clears the board and places all pieces in their initial squares.
     */
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

    /**
     * Returns the current board state.
     *
     * @return 2D array representing pieces on the board
     */
    public Piece[][] getSettledPieces() {
        return settledPieces;
    }

    /**
     * Creates a shallow copy of the given board position.
     * Piece objects themselves are not cloned.
     *
     * @param board source board
     * @return copied board array
     */
    private Piece[][] copyBoard(Piece[][] board) {
        Piece[][] copy = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            System.arraycopy(board[r], 0, copy[r], 0, 8);
        }
        return copy;
    }

    /**
     * Simulates a move on a copied board without modifying the real game state.
     * Supports special moves such as en passant and castling.
     * Does not update piece state flags (wasMoved, isJumped).
     *
     * @param pieces source board
     * @param from starting cell
     * @param to target cell
     * @return new board position after the simulated move
     */
    private Piece[][] makePseudoMove(Piece[][] pieces, Cell from, Cell to) {
        Piece[][] copy = copyBoard(pieces);
        Piece mover = copy[from.row()][from.col()];
        if (mover == null) return copy;

        int dr = to.row() - from.row();
        int dc = to.col() - from.col();

        // EN PASSANT: пешка идёт по диагонали в пустую клетку -> убрать пешку "сзади"
        if (mover instanceof Pawn && Math.abs(dc) == 1 && Math.abs(dr) == 1 && copy[to.row()][to.col()] == null) {
            Piece victim = copy[from.row()][to.col()];
            if (victim instanceof Pawn && victim.isWhite() != mover.isWhite()) {
                copy[from.row()][to.col()] = null;
            }
        }


        // CASTLING: король идёт на 2 клетки -> двигаем ладью
        if (mover instanceof King && Math.abs(dc) == 2) {
            int rookFromCol = (dc > 0) ? 7 : 0;
            int rookToCol   = (dc > 0) ? to.col() - 1 : to.col() + 1;

            Piece rook = copy[from.row()][rookFromCol];
            copy[from.row()][rookToCol] = rook;
            copy[from.row()][rookFromCol] = null;
        }

        copy[to.row()][to.col()] = mover;
        copy[from.row()][from.col()] = null;
        return copy;
    }

    /**
     * Executes a legal move from the active cell to the given target cell.
     * Handles special moves (en passant, castling) and updates piece state.
     *
     * @param cell target cell
     * @return true if the move was executed, false otherwise
     */
    public boolean move(Cell cell) {
        if (activeCell == null) return false;

        List<Cell> legalMoves = getLegalMoves(activeCell);
        if (!legalMoves.contains(cell)) return false;

        Piece mover = settledPieces[activeCell.row()][activeCell.col()];
        if (mover == null) return false;

        int fromR = activeCell.row();
        int fromC = activeCell.col();
        int toR = cell.row();
        int toC = cell.col();
        int dr = toR - fromR;
        int dc = toC - fromC;

        // перед ходом сбрасываем en-passant флаги
        resetJumpFlagsExcept();

        // EN PASSANT execution: пешка диагональю в пустую клетку
        if (mover instanceof Pawn && Math.abs(dc) == 1 && Math.abs(dr) == 1 && settledPieces[toR][toC] == null) {
            // убрать пешку противника, стоящую рядом на исходной строке
            Piece victim = settledPieces[fromR][toC];
            if (victim instanceof Pawn && victim.isWhite() != mover.isWhite()) {
                settledPieces[fromR][toC] = null;
            }
        }

        // CASTLING execution: король на 2 клетки
        if (mover instanceof King && Math.abs(dc) == 2) {
            int rookFromCol = (dc > 0) ? 7 : 0;
            int rookToCol   = (dc > 0) ? toC - 1 : toC + 1;

            Piece rook = settledPieces[fromR][rookFromCol];
            if (rook instanceof Rook && rook.isWhite() == mover.isWhite()) {
                settledPieces[fromR][rookToCol] = rook;
                settledPieces[fromR][rookFromCol] = null;
                rook.setWasMoved(true);
            }
        }


        // Pawn jump flag (двойной шаг)
        if (mover instanceof Pawn pawn) {
            pawn.setIsJumped(Math.abs(dr) == 2);
        }

        mover.setWasMoved(true);

        // основной перенос
        settledPieces[toR][toC] = mover;
        settledPieces[fromR][fromC] = null;

        return true;
    }

    /**
     * Resets the en passant availability flag (isJumped) for all pawns.
     * Called before executing a new move.
     */
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

    /**
     * Prints the given board state to the console.
     * Intended for debugging purposes.
     *
     * @param board board state to print
     */
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

    /**
     * Determines whether the specified king is currently in check.
     *
     * @param pieces board position to evaluate
     * @param isWhiteKingChecked true to check the white king, false for the black king
     * @return true if the king is in check, false otherwise
     */
    public boolean isKingCheck(Piece[][] pieces, boolean isWhiteKingChecked) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = pieces[row][col];
                if (p instanceof King && p.isWhite() == isWhiteKingChecked) {
                    return isCellAttacked(pieces, new Cell(row, col), !isWhiteKingChecked);
                }
            }
        }
        return false;
    }

    /**
     * Checks whether a given cell is attacked by any piece of the specified color.
     *
     * @param pieces board position
     * @param cell target cell
     * @param attackedByWhite true to check attacks by white pieces, false by black pieces
     * @return true if the cell is attacked, false otherwise
     */
    private boolean isCellAttacked(Piece[][] pieces, Cell cell, boolean attackedByWhite) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece attacker = pieces[row][col];
                if (attacker != null && attacker.isWhite() == attackedByWhite ) {
                    List<Cell> attackedCells = attacker.getAttackedCells(pieces, row, col);

                    if (attackedCells != null && attackedCells.contains(cell)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns all legal moves for the piece located at the given cell.
     * Filters pseudo-legal moves by simulating them and removing moves
     * that leave the player's king in check.
     * Includes special moves such as en passant and castling.
     *
     * @param cell source cell
     * @return list of legal destination cells
     */
    public List<Cell> getLegalMoves(Cell cell) {
        Piece pieceToCheck = settledPieces[cell.row()][cell.col()];
        if (pieceToCheck == null) return List.of();

        List<Cell> legalMoves = new ArrayList<>();
        boolean sideWhite = pieceToCheck.isWhite();

        // 1) обычные ходы через псевдоходы + фильтр шаха
        for (Cell to : pieceToCheck.getPsevdoAvailableMoves(settledPieces, cell.row(), cell.col())) {
            Piece[][] pseudo = makePseudoMove(settledPieces, cell, to);
            if (!isKingCheck(pseudo, sideWhite)) {
                legalMoves.add(to);
            }
        }

        // 2) EN PASSANT (как отдельные кандидаты, но тоже фильтруем через isKingCheck)
        if (pieceToCheck instanceof Pawn) {
            int direction = sideWhite ? -1 : 1;
            int fromRow = cell.row();
            int fromCol = cell.col();
            int toRow = fromRow + direction;

            // row bounds
            if (toRow >= 0 && toRow < 8) {

                // рекомендованная проверка "правильной линии"
                // белые берут en passant с row==3, чёрные с row==4 (если 0 сверху)
                boolean correctEpRank = sideWhite ? (fromRow == 3) : (fromRow == 4);

                if (correctEpRank) {
                    // LEFT
                    int leftCol = fromCol - 1;
                    if (leftCol >= 0) {
                        Piece left = settledPieces[fromRow][leftCol];
                        if (left instanceof Pawn pawn &&
                            pawn.isJumped() &&
                            pawn.isWhite() != sideWhite &&               // враг
                            settledPieces[toRow][leftCol] == null) {     // диагональ пустая
                            Cell to = new Cell(toRow, leftCol);
                            Piece[][] pseudo = makePseudoMove(settledPieces, cell, to);
                            if (!isKingCheck(pseudo, sideWhite)) {
                                legalMoves.add(to);
                            }
                        }
                    }

                    // RIGHT
                    int rightCol = fromCol + 1;
                    if (rightCol < 8) {
                        Piece right = settledPieces[fromRow][rightCol];
                        if (right instanceof Pawn pawn &&
                            pawn.isJumped() &&
                            pawn.isWhite() != sideWhite &&
                            settledPieces[toRow][rightCol] == null) {
                            Cell to = new Cell(toRow, rightCol);
                            Piece[][] pseudo = makePseudoMove(settledPieces, cell, to);
                            if (!isKingCheck(pseudo, sideWhite)) {
                                legalMoves.add(to);
                            }
                        }
                    }
                }
            }
        }

        // 3) CASTLING (пока добавим оба направления для белых/чёрных)
        if (pieceToCheck instanceof King king && !king.isWasMoved()) {
            int row = sideWhite ? 7 : 0;

            // король должен быть на стартовой клетке
            if (cell.row() == row && cell.col() == 4) {

                // король не должен быть под шахом сейчас
                if (!isCellAttacked(settledPieces, new Cell(row, 4), !sideWhite)) {

                    // Kingside: e -> g (col 4 -> 6), rook h -> f
                    if (settledPieces[row][5] == null && settledPieces[row][6] == null) {
                        Piece rook = settledPieces[row][7];
                        if (rook instanceof Rook r && !r.isWasMoved() && rook.isWhite() == sideWhite) {
                            if (!isCellAttacked(settledPieces, new Cell(row, 5), !sideWhite) &&
                                !isCellAttacked(settledPieces, new Cell(row, 6), !sideWhite)) {
                                // плюс шах-проверка через симуляцию (на всякий случай)
                                Cell to = new Cell(row, 6);
                                Piece[][] pseudo = makePseudoMove(settledPieces, cell, to);
                                if (!isKingCheck(pseudo, sideWhite)) {
                                    legalMoves.add(to);
                                }
                            }
                        }
                    }

                    // Queenside: e -> c (col 4 -> 2), rook a -> d
                    if (settledPieces[row][1] == null && settledPieces[row][2] == null && settledPieces[row][3] == null) {
                        Piece rook = settledPieces[row][0];
                        if (rook instanceof Rook r && !r.isWasMoved() && rook.isWhite() == sideWhite) {
                            if (!isCellAttacked(settledPieces, new Cell(row, 3), !sideWhite) &&
                                !isCellAttacked(settledPieces, new Cell(row, 2), !sideWhite)) {
                                Cell to = new Cell(row, 2);
                                Piece[][] pseudo = makePseudoMove(settledPieces, cell, to);
                                if (!isKingCheck(pseudo, sideWhite)) {
                                    legalMoves.add(to);
                                }
                            }
                        }
                    }
                }
            }
        }

        return legalMoves;
    }

}