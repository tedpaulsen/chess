package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
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
                moves.add(new Move(pieceCode, pawn, pawn.shiftLeft(8).mask(empties)));
                // left capture
                moves.add(new Move(pieceCode, pawn, pawn.notAFile().shiftLeft(7).mask(enemyPieces)));
                // right capture
                moves.add(new Move(pieceCode, pawn, pawn.notHFile().shiftLeft(9).mask(enemyPieces)));
                // double advance
                moves.add(
                    new Move(pieceCode, pawn, pawn.rank2().shiftLeft(8).mask(empties).shiftLeft(8).mask(empties))
                );
                // TODO: en-passant capture
            } else {
                // advance
                moves.add(new Move(pieceCode, pawn, pawn.shiftRight(8).mask(empties)));
                // left capture
                moves.add(new Move(pieceCode, pawn, pawn.notHFile().shiftRight(7).mask(enemyPieces)));
                // right capture
                moves.add(new Move(pieceCode, pawn, pawn.notAFile().shiftRight(9).mask(enemyPieces)));
                // double advance
                moves.add(
                    new Move(pieceCode, pawn, pawn.rank7().shiftRight(8).mask(empties).shiftRight(8).mask(empties))
                );
                // TODO: en-passant capture
            }
        }

        return moves.stream().filter(m -> !m.getTo().isEmpty()).toList();
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
