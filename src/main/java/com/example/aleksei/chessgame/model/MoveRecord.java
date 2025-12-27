package com.example.aleksei.chessgame.model;

public class MoveRecord {

    // base
    public final Cell from;
    public final Cell to;

    public final Piece movedPiece;
    public final boolean movedPieceWasMoved;

    public final Piece capturedPiece;   // normal capture on "to" (can be null)
    public final Cell capturedCell;     // usually == to (or null)

    // castling
    public boolean wasCastling;
    public Piece rookPiece;
    public Cell rookFrom;
    public Cell rookTo;
    public boolean rookWasMoved;

    // en passant
    public boolean wasEnPassant;
    public Piece enPassantPawn;
    public Cell enPassantPawnCell;

    // promotion
    public boolean wasPromotion;
    public Piece pawnBeforePromotion;
    public Piece promotedTo;

    public MoveRecord(
            Cell from,
            Cell to,
            Piece movedPiece,
            boolean movedPieceWasMoved,
            Piece capturedPiece,
            Cell capturedCell
    ) {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.movedPieceWasMoved = movedPieceWasMoved;
        this.capturedPiece = capturedPiece;
        this.capturedCell = capturedCell;
    }
}
