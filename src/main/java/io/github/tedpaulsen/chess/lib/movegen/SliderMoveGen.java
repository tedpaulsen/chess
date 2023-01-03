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

    public List<Function<BitBoard, BitBoard>> diagonalTransforms(BitBoard friendlyPieces) {
        return List.of(
            b -> b.notHFile().notRank8().shiftLeft(9).mask(~friendlyPieces.getValue()),
            b -> b.notAFile().notRank8().shiftLeft(7).mask(~friendlyPieces.getValue()),
            b -> b.notHFile().notRank1().shiftRight(7).mask(~friendlyPieces.getValue()),
            b -> b.notAFile().notRank1().shiftRight(9).mask(~friendlyPieces.getValue())
        );
    }

    public List<Function<BitBoard, BitBoard>> verticalAndHorizontalTransforms(BitBoard friendlyPieces) {
        return List.of(
            b -> b.notRank8().shiftLeft(8).mask(~friendlyPieces.getValue()),
            b -> b.notHFile().shiftLeft(1).mask(~friendlyPieces.getValue()),
            b -> b.notRank1().shiftRight(8).mask(~friendlyPieces.getValue()),
            b -> b.notAFile().shiftRight(1).mask(~friendlyPieces.getValue())
        );
    }
}
