package io.github.tedpaulsen.chess.lib;

public class MoveApplier {

    public BoardRepresentation applyMove(BoardRepresentation currBoard, Move moveToApply) {
        var boardBuilder = currBoard.toBuilder();
        boolean movingSideIsWhite = moveToApply.getSide().is(Side.WHITE);

        if (movingSideIsWhite) {
            if (!currBoard.getBlackPieces().intersect(moveToApply.getTo()).isEmpty()) {
                // move is a capture, so remove the piece we captured
                boardBuilder.blackPawns(currBoard.getBlackPawns().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.blackKnights(currBoard.getBlackKnights().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.blackBishops(currBoard.getBlackBishops().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.blackRooks(currBoard.getBlackRooks().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.blackQueens(currBoard.getBlackQueens().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.blackKing(currBoard.getBlackKing().intersect(~moveToApply.getTo().getValue()));
            }
        } else {
            if (!currBoard.getWhitePieces().intersect(moveToApply.getTo()).isEmpty()) {
                // remove any white pieces we may have captured
                boardBuilder.whitePawns(currBoard.getWhitePawns().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.whiteKnights(currBoard.getWhiteKnights().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.whiteBishops(currBoard.getWhiteBishops().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.whiteRooks(currBoard.getWhiteRooks().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.whiteQueens(currBoard.getWhiteQueens().intersect(~moveToApply.getTo().getValue()));
                boardBuilder.whiteKing(currBoard.getWhiteKing().intersect(~moveToApply.getTo().getValue()));
            }
        }

        // we must set the en-passant flag
        BitBoard enPassantSquare = new BitBoard(0L);
        if (Move.Kind.PAWN_DOUBLE_ADVANCE.equals(moveToApply.getMoveKind())) {
            enPassantSquare = movingSideIsWhite ? moveToApply.getTo().shiftRight(8) : moveToApply.getTo().shiftLeft(8);
        }
        boardBuilder.enPassantSquare(enPassantSquare);

        // we must remove the captured en-passant pawn
        if (Move.Kind.EN_PASSANT_CAPTURE.equals(moveToApply.getMoveKind())) {
            if (movingSideIsWhite) {
                boardBuilder.blackPawns(
                    currBoard.getBlackPawns().intersect(~currBoard.getEnPassantSquare().getValue())
                );
            } else {
                boardBuilder.whitePawns(
                    currBoard.getWhitePawns().intersect(~currBoard.getEnPassantSquare().getValue())
                );
            }
        }

        BitBoard updatedFriendlyPieces = currBoard
            .getPieces(moveToApply.getPiece())
            .move(moveToApply.getFrom(), moveToApply.getTo());

        return boardBuilder.build().set(moveToApply.getPiece(), updatedFriendlyPieces);
    }
}
