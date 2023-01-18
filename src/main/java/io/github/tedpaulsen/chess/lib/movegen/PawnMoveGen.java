package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Masks;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'P' : 'p';
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();
        BitBoard empties = board.getEmpties();
        BitBoard pawns = side.is(Side.WHITE) ? board.getWhitePawns() : board.getBlackPawns();

        for (BitBoard pawn : pawns.toSingletons()) {
            if (side.is(Side.WHITE)) {
                // advance
                addMove(new Move(pieceCode, pawn, pawn.shiftLeft(8).mask(empties)), moves);
                // left capture
                addMove(new Move(pieceCode, pawn, pawn.notAFile().shiftLeft(7).mask(enemyPieces)), moves);
                // left en passant capture
                addMove(
                    new Move(
                        pieceCode,
                        pawn,
                        pawn.notHFile().shiftLeft(7).mask(board.getEnPassantSquare()),
                        Move.Kind.EN_PASSANT_CAPTURE
                    ),
                    moves
                );
                // right capture
                addMove(new Move(pieceCode, pawn, pawn.notHFile().shiftLeft(9).mask(enemyPieces)), moves);
                // right en passant capture
                addMove(
                    new Move(
                        pieceCode,
                        pawn,
                        pawn.notHFile().shiftLeft(9).mask(board.getEnPassantSquare()),
                        Move.Kind.EN_PASSANT_CAPTURE
                    ),
                    moves
                );
                // double advance
                addMove(
                    new Move(
                        pieceCode,
                        pawn,
                        pawn.rank2().shiftLeft(8).mask(empties).shiftLeft(8).mask(empties),
                        Move.Kind.PAWN_DOUBLE_ADVANCE
                    ),
                    moves
                );
            } else {
                // advance
                addMove(new Move(pieceCode, pawn, pawn.shiftRight(8).mask(empties)), moves);
                // left capture
                addMove(new Move(pieceCode, pawn, pawn.notHFile().shiftRight(7).mask(enemyPieces)), moves);
                // left en passant capture
                addMove(
                    new Move(
                        pieceCode,
                        pawn,
                        pawn.notHFile().shiftRight(7).mask(board.getEnPassantSquare()),
                        Move.Kind.EN_PASSANT_CAPTURE
                    ),
                    moves
                );
                // right capture
                addMove(new Move(pieceCode, pawn, pawn.notAFile().shiftRight(9).mask(enemyPieces)), moves);
                // right en passant capture
                addMove(
                    new Move(
                        pieceCode,
                        pawn,
                        pawn.notAFile().shiftRight(9).mask(board.getEnPassantSquare()),
                        Move.Kind.EN_PASSANT_CAPTURE
                    ),
                    moves
                );
                // double advance
                addMove(
                    new Move(
                        pieceCode,
                        pawn,
                        pawn.rank7().shiftRight(8).mask(empties).shiftRight(8).mask(empties),
                        Move.Kind.PAWN_DOUBLE_ADVANCE
                    ),
                    moves
                );
            }
        }

        return moves;
    }

    private void addMove(Move moveToAdd, Collection<Move> moves) {
        if (moveToAdd.getTo().isEmpty()) {
            return;
        }

        if (!moveToAdd.getTo().intersect(Masks.RANK_8 | Masks.RANK_1).isEmpty()) {
            // promoting a pawn actually results in 4 possible moves depending on promotion choice
            moves.add(moveToAdd.toBuilder().moveKind(Move.Kind.PAWN_PROMOTE_TO_QUEEN).build());
            moves.add(moveToAdd.toBuilder().moveKind(Move.Kind.PAWN_PROMOTE_TO_ROOK).build());
            moves.add(moveToAdd.toBuilder().moveKind(Move.Kind.PAWN_PROMOTE_TO_BISHOP).build());
            moves.add(moveToAdd.toBuilder().moveKind(Move.Kind.PAWN_PROMOTE_TO_KNIGHT).build());
        } else {
            moves.add(moveToAdd);
        }
    }

    @Override
    public BitBoard getSquaresAttacked(Side side, BoardRepresentation board) {
        if (side.is(Side.WHITE)) {
            return get(side, board).shiftLeft(9).union(board.getWhitePawns().shiftLeft(7));
        } else {
            return get(side, board).shiftRight(9).union(board.getWhitePawns().shiftRight(7));
        }
    }

    @Override
    public BitBoard get(Side side, BoardRepresentation board) {
        return side.is(Side.WHITE) ? board.getWhitePawns() : board.getBlackPawns();
    }
}
