package com.example.aleksei.chessgame.model;

public record MoveRecord(
    Cell from,
    Cell to,

    Piece movedPiece,
    boolean movedPieceWasMoved,

    Piece capturedPiece,
    Cell capturedCell,

    // castling
    boolean wasCastling,
    Piece rookPiece,
    Cell rookFrom,
    Cell rookTo,
    boolean rookWasMoved,

    // en passant
    boolean wasEnPassant,
    Piece enPassantPawn,
    Cell enPassantPawnCell,

    // promotion
    boolean wasPromotion,
    Piece pawnBeforePromotion,
    Piece promotedTo
) {}
