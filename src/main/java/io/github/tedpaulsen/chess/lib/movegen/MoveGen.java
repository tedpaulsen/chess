package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.List;

public interface MoveGen {
    List<Move> generateMoves(Side side, BoardRepresentation board);

    /**
     * Returns a bitboard with 1s at the locations currently empty or containing enemy pieces where,
     * if an opposing king were present, it would be in check.
     */
    BitBoard getSquaresAttacked(Side side, BoardRepresentation board);

    /**
     * Returns a bitboard representing the location of the pieces governed by the current MoveGen
     */
    BitBoard get(Side side, BoardRepresentation board);
}
