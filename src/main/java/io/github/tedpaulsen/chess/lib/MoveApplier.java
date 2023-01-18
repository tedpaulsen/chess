package io.github.tedpaulsen.chess.lib;

import java.util.function.Function;

public class MoveApplier {

    private static final long WHITE_QUEEN_SIDE_ROOK = 1L;
    private static final long WHITE_KING_SIDE_ROOK = 1L << 7;
    private static final long BLACK_QUEEN_SIDE_ROOK = 1L << 56;
    private static final long BLACK_KING_SIDE_ROOK = 1L << 63;

    public BoardRepresentation applyMove(BoardRepresentation currBoard, Move moveToApply) {
        BoardRepresentation.BoardRepresentationBuilder builder = currBoard.toBuilder();

        applyCastlingChanges(currBoard, moveToApply, builder);
        setEnPassantSquare(currBoard, moveToApply, builder);
        removeCapturedPiece(currBoard, moveToApply, builder);
        removeEnPassantCapturedPawn(currBoard, moveToApply, builder);
        setPromotedPawn(currBoard, moveToApply, builder);

        // only move the piece to its destination if not a pawn promotion. pawn promotion
        // is the only move where the piece moving is actually removed from the board.
        if (
            !moveToApply.getMoveKind().equals(Move.Kind.PAWN_PROMOTE_TO_QUEEN) &&
            !moveToApply.getMoveKind().equals(Move.Kind.PAWN_PROMOTE_TO_ROOK) &&
            !moveToApply.getMoveKind().equals(Move.Kind.PAWN_PROMOTE_TO_BISHOP) &&
            !moveToApply.getMoveKind().equals(Move.Kind.PAWN_PROMOTE_TO_KNIGHT)
        ) {
            return builder
                .build()
                .set(
                    moveToApply.getPiece(),
                    currBoard.getPieces(moveToApply.getPiece()).move(moveToApply.getFrom(), moveToApply.getTo())
                );
        } else {
            return builder.build();
        }
    }

    /**
     * If the move being applied is a capture, we must remove the captured piece.
     * @param currBoard the current board (before the move)
     * @param moveToApply the move we're applying
     * @param builder board builder object for the new board state
     */
    private void removeCapturedPiece(
        BoardRepresentation currBoard,
        Move moveToApply,
        BoardRepresentation.BoardRepresentationBuilder builder
    ) {
        if (moveToApply.getSide().is(Side.WHITE)) {
            if (!currBoard.getBlackPieces().intersect(moveToApply.getTo()).isEmpty()) {
                // move is a capture, so remove the piece we captured
                builder.blackPawns(currBoard.getBlackPawns().intersect(~moveToApply.getTo().getValue()));
                builder.blackKnights(currBoard.getBlackKnights().intersect(~moveToApply.getTo().getValue()));
                builder.blackBishops(currBoard.getBlackBishops().intersect(~moveToApply.getTo().getValue()));
                builder.blackRooks(currBoard.getBlackRooks().intersect(~moveToApply.getTo().getValue()));
                builder.blackQueens(currBoard.getBlackQueens().intersect(~moveToApply.getTo().getValue()));
                builder.blackKing(currBoard.getBlackKing().intersect(~moveToApply.getTo().getValue()));
            }
        } else {
            if (!currBoard.getWhitePieces().intersect(moveToApply.getTo()).isEmpty()) {
                // remove any white pieces we may have captured
                builder.whitePawns(currBoard.getWhitePawns().intersect(~moveToApply.getTo().getValue()));
                builder.whiteKnights(currBoard.getWhiteKnights().intersect(~moveToApply.getTo().getValue()));
                builder.whiteBishops(currBoard.getWhiteBishops().intersect(~moveToApply.getTo().getValue()));
                builder.whiteRooks(currBoard.getWhiteRooks().intersect(~moveToApply.getTo().getValue()));
                builder.whiteQueens(currBoard.getWhiteQueens().intersect(~moveToApply.getTo().getValue()));
                builder.whiteKing(currBoard.getWhiteKing().intersect(~moveToApply.getTo().getValue()));
            }
        }
    }

    /**
     * If the move we're applying affects castling availability in any way, we must update the castling flags
     * @param currBoard the current board (before the move)
     * @param moveToApply the move we're applying
     * @param builder board builder object for the new board state
     */
    private void applyCastlingChanges(
        BoardRepresentation currBoard,
        Move moveToApply,
        BoardRepresentation.BoardRepresentationBuilder builder
    ) {
        String currCastlingAvailability = currBoard.getCastlingInformation();
        // update castling availability flags if king moved
        if (moveToApply.getPiece() == 'K') {
            builder.castlingInformation(currCastlingAvailability.replace("K", "").replace("Q", ""));
        } else if (moveToApply.getPiece() == 'k') {
            builder.castlingInformation(currCastlingAvailability.replace("k", "").replace("q", ""));
        }

        // update castling availability flags if rook moved
        if (moveToApply.getPiece() == 'R') {
            if (WHITE_QUEEN_SIDE_ROOK == moveToApply.getFrom().getValue()) {
                builder.castlingInformation(currCastlingAvailability.replace("Q", ""));
            } else if (WHITE_KING_SIDE_ROOK == moveToApply.getFrom().getValue()) {
                builder.castlingInformation(currCastlingAvailability.replace("K", ""));
            }
        } else if (moveToApply.getPiece() == 'r') {
            if (BLACK_QUEEN_SIDE_ROOK == moveToApply.getFrom().getValue()) {
                builder.castlingInformation(currCastlingAvailability.replace("q", ""));
            } else if (BLACK_KING_SIDE_ROOK == moveToApply.getFrom().getValue()) {
                builder.castlingInformation(currCastlingAvailability.replace("k", ""));
            }
        }

        // update castling availability flags if we castled and update rook location
        boolean movingSideIsWhite = moveToApply.getSide().is(Side.WHITE);
        if (Move.Kind.KING_SIDE_CASTLE.equals(moveToApply.getMoveKind())) {
            builder.castlingInformation(
                currCastlingAvailability
                    .replace(movingSideIsWhite ? "K" : "k", "")
                    .replace(movingSideIsWhite ? "Q" : "q", "")
            );
            if (movingSideIsWhite) {
                builder.whiteRooks(
                    currBoard.getWhiteRooks().intersect(~WHITE_KING_SIDE_ROOK).union(moveToApply.getTo().shiftRight(1))
                );
            } else {
                builder.blackRooks(
                    currBoard.getBlackRooks().intersect(~BLACK_KING_SIDE_ROOK).union(moveToApply.getTo().shiftRight(1))
                );
            }
        } else if (Move.Kind.QUEEN_SIDE_CASTLE.equals(moveToApply.getMoveKind())) {
            builder.castlingInformation(
                currCastlingAvailability
                    .replace(movingSideIsWhite ? "K" : "k", "")
                    .replace(movingSideIsWhite ? "Q" : "q", "")
            );
            if (movingSideIsWhite) {
                builder.whiteRooks(
                    currBoard.getWhiteRooks().intersect(~WHITE_QUEEN_SIDE_ROOK).union(moveToApply.getTo().shiftLeft(1))
                );
            } else {
                builder.blackRooks(
                    currBoard.getBlackRooks().intersect(~BLACK_QUEEN_SIDE_ROOK).union(moveToApply.getTo().shiftLeft(1))
                );
            }
        }
    }

    /**
     * If the move we're applying is a pawn double-advance, we must set the available en-passant square
     * @param currBoard the current board (before the move)
     * @param moveToApply the move we're applying
     * @param builder board builder object for the new board state
     */
    private void setEnPassantSquare(
        BoardRepresentation currBoard,
        Move moveToApply,
        BoardRepresentation.BoardRepresentationBuilder builder
    ) {
        boolean movingSideIsWhite = moveToApply.getSide().is(Side.WHITE);

        // we must set the en-passant flag
        BitBoard enPassantSquare = new BitBoard(0L);
        if (Move.Kind.PAWN_DOUBLE_ADVANCE.equals(moveToApply.getMoveKind())) {
            enPassantSquare = movingSideIsWhite ? moveToApply.getTo().shiftRight(8) : moveToApply.getTo().shiftLeft(8);
        }
        builder.enPassantSquare(enPassantSquare);
    }

    /**
     * If the move we're applying is an en-passant capture, we must remove the captured pawn
     * @param currBoard the current board (before the move)
     * @param moveToApply the move we're applying
     * @param builder board builder object for the new board state
     */
    private void removeEnPassantCapturedPawn(
        BoardRepresentation currBoard,
        Move moveToApply,
        BoardRepresentation.BoardRepresentationBuilder builder
    ) {
        if (Move.Kind.EN_PASSANT_CAPTURE.equals(moveToApply.getMoveKind())) {
            if (moveToApply.getSide().is(Side.WHITE)) {
                builder.blackPawns(
                    currBoard.getBlackPawns().intersect(~currBoard.getEnPassantSquare().shiftRight(8).getValue())
                );
            } else {
                builder.whitePawns(
                    currBoard.getWhitePawns().intersect(~currBoard.getEnPassantSquare().shiftLeft(8).getValue())
                );
            }
        }
    }

    /**
     * If the move we're applying is a pawn promotion, we must replace the pawn with the promoted piece
     * @param currBoard the current board (before the move)
     * @param moveToApply the move we're applying
     * @param builder board builder object for the new board state
     */
    private void setPromotedPawn(
        BoardRepresentation currBoard,
        Move moveToApply,
        BoardRepresentation.BoardRepresentationBuilder builder
    ) {
        Move.Kind kind = moveToApply.getMoveKind();
        boolean isWhiteMove = Side.WHITE.is(moveToApply.getSide());

        if (
            Move.Kind.PAWN_PROMOTE_TO_QUEEN.equals(kind) ||
            Move.Kind.PAWN_PROMOTE_TO_ROOK.equals(kind) ||
            Move.Kind.PAWN_PROMOTE_TO_BISHOP.equals(kind) ||
            Move.Kind.PAWN_PROMOTE_TO_KNIGHT.equals(kind)
        ) {
            // the builder function to set the bitboard for the promoted piece of choice
            Function<BitBoard, BoardRepresentation.BoardRepresentationBuilder> setter =
                switch (kind) {
                    case PAWN_PROMOTE_TO_QUEEN -> isWhiteMove ? builder::whiteQueens : builder::blackQueens;
                    case PAWN_PROMOTE_TO_ROOK -> isWhiteMove ? builder::whiteRooks : builder::blackRooks;
                    case PAWN_PROMOTE_TO_BISHOP -> isWhiteMove ? builder::whiteBishops : builder::blackBishops;
                    case PAWN_PROMOTE_TO_KNIGHT -> isWhiteMove ? builder::whiteKnights : builder::blackKnights;
                    default -> throw new IllegalArgumentException("Unexpected move kind");
                };

            // the current bitboard for the promoted piece of choice
            BitBoard current =
                switch (kind) {
                    case PAWN_PROMOTE_TO_QUEEN -> isWhiteMove ? currBoard.getWhiteQueens() : currBoard.getBlackQueens();
                    case PAWN_PROMOTE_TO_ROOK -> isWhiteMove ? currBoard.getWhiteRooks() : currBoard.getBlackRooks();
                    case PAWN_PROMOTE_TO_BISHOP -> isWhiteMove
                        ? currBoard.getWhiteBishops()
                        : currBoard.getBlackBishops();
                    case PAWN_PROMOTE_TO_KNIGHT -> isWhiteMove
                        ? currBoard.getWhiteKnights()
                        : currBoard.getBlackKnights();
                    default -> throw new IllegalArgumentException("Unexpected move kind");
                };

            // add the chosen promoted piece to its appropriate bitboard
            setter.apply(current.union(moveToApply.getTo()));

            // remove the promoted pawn
            if (isWhiteMove) {
                builder.whitePawns(currBoard.getWhitePawns().intersect(~moveToApply.getFrom().getValue()));
            } else {
                builder.blackPawns(currBoard.getBlackPawns().intersect(~moveToApply.getFrom().getValue()));
            }
        }
    }
}
