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
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();
        BitBoard empties = new BitBoard(~friendlyPieces.getValue() & ~enemyPieces.getValue());
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

        return moves.stream().filter(m -> !m.getTo().empty()).toList();
    }
}
