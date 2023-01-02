package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.Move;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class SliderMoveGen {

    List<Move> generateMovesFromTransform(
        char piece,
        BitBoard startingLocation,
        Function<BitBoard, BitBoard> transform,
        BitBoard enemyPieces
    ) {
        List<Move> moves = new ArrayList<>();
        BitBoard move = transform.apply(startingLocation);

        while (!move.empty()) {
            moves.add(new Move(piece, startingLocation, move));
            if (!enemyPieces.mask(move).empty()) {
                // we've hit an enemy piece, break here
                break;
            }
            // mask a file and rank 8 to ensure we don't wrap around, then slide NW, then mask friendly pieces
            move = transform.apply(move);
        }

        return moves;
    }
}
