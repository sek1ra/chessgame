package com.example.aleksei.chessgame.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Game {
    private boolean isGameActive;
    private boolean isWhiteMove;
    private Board board;
    private GameStatus status;
    private final Deque<MoveRecord> moves = new ArrayDeque<>();
    private final Deque<MoveRecord> redo = new ArrayDeque<>();

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

        capturePrevJumpedPawn(record);

        return record;
    }


    public MoveResult tryMove(Cell to) {
        MoveRecord record = initMoveRecord(to);
        if (record == null) return new MoveResult(false, status, null, null);

        if (!board.move(to)) return new MoveResult(false, status, null, null);

        if (record.wasPromotion) {
            record.promotedTo = board.getSettledPieces()[to.row()][to.col()];
        }

        moves.push(record);
        redo.clear();

        board.setActiveCell(null);
        switchTurn();

        boolean sideToMove = isWhiteMove(); // лучше через метод
        boolean hasMoves = board.hasAnyLegalMoves(sideToMove);
        boolean inCheck = board.isKingCheck(board.getSettledPieces(), sideToMove);

        Boolean winnerIsWhite = null;

        if (!hasMoves) {
            if (inCheck) {
                status = GameStatus.CHECKMATE;
                isGameActive = false;
                winnerIsWhite = !sideToMove;
            } else {
                status = GameStatus.STALEMATE;
                isGameActive = false;
            }
        } else {
            status = inCheck ? GameStatus.CHECK : GameStatus.RUNNING;
        }

        return new MoveResult(true, status, winnerIsWhite, formatMove(record));
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

    private void capturePrevJumpedPawn(MoveRecord r) {
        Piece[][] b = board.getSettledPieces();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece p = b[row][col];
                if (p instanceof Pawn pawn && pawn.isJumped()) {
                    r.prevJumpedPawn = pawn;
                    r.prevJumpedPawnCell = new Cell(row, col);
                    return;
                }
            }
        }
    }

    public MoveRecord undoLastMove() {
        System.out.println("UNDO pressed, moves size = " + moves.size());
        if (moves.isEmpty()) return null;

        MoveRecord r = moves.pop();
        redo.push(r);

        Piece[][] b = board.getSettledPieces();

        int fromR = r.from.row();
        int fromC = r.from.col();
        int toR   = r.to.row();
        int toC   = r.to.col();

        // 1) jumped flags: очистить и восстановить состояние "до хода"
        clearAllJumpedFlags(b);
        if (r.prevJumpedPawn != null) {
            r.prevJumpedPawn.setIsJumped(true);
        }

        // 2) Вычисляем фигуру, которую надо поставить обратно на from
        // promotion: возвращаем пешку (pawnBeforePromotion)
        Piece pieceBack = r.wasPromotion ? r.pawnBeforePromotion : r.movedPiece;

        // 3) Убираем фигуру с клетки to (там может стоять promotedTo и т.п.)
        b[toR][toC] = null;

        // 4) Ставим ходившую фигуру обратно на from и восстанавливаем wasMoved
        b[fromR][fromC] = pieceBack;
        if (pieceBack != null) {
            pieceBack.setWasMoved(r.movedPieceWasMoved);
        }

        // 5) Вернуть обычное взятие
        if (r.capturedPiece != null && r.capturedCell != null) {
            b[r.capturedCell.row()][r.capturedCell.col()] = r.capturedPiece;
        }

        // 6) Вернуть en passant жертву
        if (r.wasEnPassant && r.enPassantPawn != null && r.enPassantPawnCell != null) {
            b[r.enPassantPawnCell.row()][r.enPassantPawnCell.col()] = r.enPassantPawn;
        }

        // 7) Вернуть ладью при рокировке
        if (r.wasCastling && r.rookPiece != null && r.rookFrom != null && r.rookTo != null) {
            b[r.rookTo.row()][r.rookTo.col()] = null;
            b[r.rookFrom.row()][r.rookFrom.col()] = r.rookPiece;
            r.rookPiece.setWasMoved(r.rookWasMoved);
        }

        // 8) ход назад
        switchTurn();
        board.setActiveCell(null);
        setGameActive(true);

        boolean sideToMove = isWhiteMove();
        boolean inCheck = board.isKingCheck(board.getSettledPieces(), sideToMove);
        setStatus(inCheck ? GameStatus.CHECK : GameStatus.RUNNING);

        return r;
    }

    private void clearAllJumpedFlags(Piece[][] b) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = b[r][c];
                if (p instanceof Pawn pawn) pawn.setIsJumped(false);
            }
        }
    }


}
