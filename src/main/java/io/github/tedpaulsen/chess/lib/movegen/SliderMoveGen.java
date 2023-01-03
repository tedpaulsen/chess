package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.Move;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class SliderMoveGen {

    public List<Move> generateMovesFromTransform(
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
            move = transform.apply(move);
        }

        return moves;
    }
}
