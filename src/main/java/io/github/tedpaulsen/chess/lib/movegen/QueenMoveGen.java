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

public class QueenMoveGen extends SliderMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'Q' : 'q';
        BitBoard queens = side.is(Side.WHITE) ? board.getWhiteQueens() : board.getBlackQueens();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard queen : queens.toSingletons()) {
            BitBoard friendlyPieces =
                (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~queen.getValue());

            Stream<Function<BitBoard, BitBoard>> transforms = Stream
                .of(verticalAndHorizontalTransforms(friendlyPieces), diagonalTransforms(friendlyPieces))
                .flatMap(Collection::stream);

            moves.addAll(
                transforms
                    .map(transform -> generateMovesFromTransform(piece, queen, transform, enemyPieces))
                    .flatMap(Collection::stream)
                    .toList()
            );
        }

        return moves;
    }
}
