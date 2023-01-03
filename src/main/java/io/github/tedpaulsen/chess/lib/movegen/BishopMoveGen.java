package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class BishopMoveGen extends SliderMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'B' : 'b';
        BitBoard bishops = side.is(Side.WHITE) ? board.getWhiteBishops() : board.getBlackBishops();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard bishop : bishops.toSingletons()) {
            BitBoard friendlyPieces =
                (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~bishop.getValue());

            Stream<Function<BitBoard, BitBoard>> transforms = Stream
                .of(diagonalTransforms(friendlyPieces))
                .flatMap(Collection::stream);

            moves.addAll(
                transforms
                    .map(transform -> generateMovesFromTransform(piece, bishop, transform, enemyPieces))
                    .flatMap(Collection::stream)
                    .toList()
            );
        }

        return moves;
    }
}
