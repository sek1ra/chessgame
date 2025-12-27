package com.example.aleksei.chessgame.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Game {
    private boolean isGameActive;
    private boolean isWhiteMove;
    private Board board;
    private GameStatus status;
    private final Deque<MoveRecord> moves = new ArrayDeque<>();

    public Game() {
        isGameActive = true;
        isWhiteMove = true;
        board = new Board();
        status = GameStatus.RUNNING;
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public void setGameActive(boolean isGameActive) {
        this.isGameActive = isGameActive;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void run() {
        isGameActive = true;
        status = GameStatus.RUNNING;
    }

    public boolean isWhiteMove() {
        return isWhiteMove;
    }

    public void setWhiteMove(boolean isWhiteMove) {
        this.isWhiteMove = isWhiteMove;
    }

    public void switchTurn() {
        isWhiteMove = !isWhiteMove;
    }

    public boolean isKingCheck() {
        
        return false;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    private MoveRecord initMoveRecord(Cell to) {
        Cell from = board.getActiveCell();
        if (from == null) return null;

        Piece[][] b = board.getSettledPieces();
        Piece mover = b[from.row()][from.col()];
        if (mover == null) return null;

        // normal capture
        Piece captured = b[to.row()][to.col()];
        Cell capturedCell = (captured != null) ? to : null;

        MoveRecord record = new MoveRecord(
            from, to,
            mover, mover.isWasMoved(),
            captured, capturedCell
        );

        int dr = to.row() - from.row();
        int dc = to.col() - from.col();

        // --- EN PASSANT candidate (pawn diagonally into empty square) ---
        // IMPORTANT: legality is already checked by board.getLegalMoves + board.move.
        if (mover instanceof Pawn
                && Math.abs(dc) == 1
                && Math.abs(dr) == 1
                && captured == null) {

            // victim pawn is on from.row() and to.col()
            Piece victim = b[from.row()][to.col()];
            if (victim instanceof Pawn && victim.isWhite() != mover.isWhite()) {
                record.wasEnPassant = true;
                record.enPassantPawn = victim;
                record.enPassantPawnCell = new Cell(from.row(), to.col());
            }
        }

        // --- CASTLING candidate (king moves 2 columns) ---
        if (mover instanceof King && Math.abs(dc) == 2) {
            int row = from.row();
            int rookFromCol = (dc > 0) ? 7 : 0;
            int rookToCol   = (dc > 0) ? to.col() - 1 : to.col() + 1;

            Piece rook = b[row][rookFromCol];
            if (rook instanceof Rook && rook.isWhite() == mover.isWhite()) {
                record.wasCastling = true;
                record.rookPiece = rook;
                record.rookFrom = new Cell(row, rookFromCol);
                record.rookTo   = new Cell(row, rookToCol);
                record.rookWasMoved = rook.isWasMoved();
            }
        }

        // --- PROMOTION candidate (pawn reaches last rank) ---
        if (mover instanceof Pawn && (to.row() == 0 || to.row() == 7)) {
            record.wasPromotion = true;
            record.pawnBeforePromotion = mover;
        }

        return record;
    }


    public MoveResult tryMove(Cell to) {
        MoveRecord record = initMoveRecord(to);
        if (record == null) {
            return new MoveResult(false, status, null, null);
        }

        if (!board.move(to)) {
            return new MoveResult(false, status, null, null);
        }

        // promotion: AFTER move we can capture the new piece
        if (record.wasPromotion) {
            record.promotedTo = board.getSettledPieces()[to.row()][to.col()];
        }

        moves.push(record);

        board.setActiveCell(null);
        switchTurn();

        boolean sideToMove = isWhiteMove;
        boolean hasMoves = board.hasAnyLegalMoves(sideToMove);
        boolean inCheck = board.isKingCheck(board.getSettledPieces(), sideToMove);

        Boolean winnerIsWhite = null;

        if (!hasMoves) {
            if (inCheck) {
                status = GameStatus.CHECKMATE;
                isGameActive = false;
                winnerIsWhite = !sideToMove; // winner is the side that is NOT to move
            } else {
                status = GameStatus.STALEMATE;
                isGameActive = false;
            }
        } else {
            status = inCheck ? GameStatus.CHECK : GameStatus.RUNNING;
        }

        String moveText = formatMove(record);
        return new MoveResult(true, status, winnerIsWhite, moveText);
    }

    public static String formatMove(MoveRecord r) {

        // 1. Castling
        if (r.wasCastling) {
            // король идёт e->g или e->c
            return r.to.col() == 6 ? "O-O" : "O-O-O";
        }

        StringBuilder sb = new StringBuilder();

        // 2. Piece letter (pawn = empty)
        sb.append(pieceLetter(r.movedPiece));

        // 3. Capture
        if (r.capturedPiece != null || r.wasEnPassant) {
            // для пешки указываем файл
            if (r.movedPiece instanceof Pawn) {
                sb.append(fileChar(r.from.col()));
            }
            sb.append("x");
        }

        // 4. Destination square
        sb.append(cellName(r.to));

        // 5. En passant suffix (опционально)
        if (r.wasEnPassant) {
            sb.append(" e.p.");
        }

        // 6. Promotion
        if (r.wasPromotion && r.promotedTo != null) {
            sb.append("=").append(pieceLetter(r.promotedTo));
        }

        return sb.toString();
    }

    private static String pieceLetter(Piece p) {
        if (p instanceof King)   return "K";
        if (p instanceof Queen)  return "Q";
        if (p instanceof Rook)   return "R";
        if (p instanceof Bishop) return "B";
        if (p instanceof Knight) return "N";
        return ""; // pawn
    }

    private static String cellName(Cell c) {
        return "" + fileChar(c.col()) + rankChar(c.row());
    }

    private static char fileChar(int col) {
        return (char) ('a' + col);
    }

    private static char rankChar(int row) {
        return (char) ('8' - row);
    }

}
