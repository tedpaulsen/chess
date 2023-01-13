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

public class RookMoveGen extends SliderMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'R' : 'r';
        BitBoard rooks = side.is(Side.WHITE) ? board.getWhiteRooks() : board.getBlackRooks();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard rook : rooks.toSingletons()) {
            BitBoard friendlyPieces =
                (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~rook.getValue());

            Stream<Function<BitBoard, BitBoard>> transforms = Stream
                .of(verticalAndHorizontalTransforms(friendlyPieces))
                .flatMap(Collection::stream);

            moves.addAll(
                transforms
                    .map(transform -> generateMovesFromTransform(piece, rook, transform, enemyPieces))
                    .flatMap(Collection::stream)
                    .toList()
            );
        }

        return moves;
    }

    @Override
    public BitBoard getSquaresAttacked(Side side, BoardRepresentation board) {
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        Stream<Function<BitBoard, BitBoard>> transforms = Stream
            .of(verticalAndHorizontalTransforms(friendlyPieces))
            .flatMap(Collection::stream);

        BitBoard acc = new BitBoard(0L);
        for (BitBoard rook : get(side, board).toSingletons()) {
            acc.union(
                transforms
                    .map(transform -> generateTargetSquaresFromTransform(rook, transform, enemyPieces))
                    .reduce(new BitBoard(0L), BitBoard::union)
            );
        }
        return acc;
    }

    @Override
    public BitBoard get(Side side, BoardRepresentation board) {
        return side.is(Side.WHITE) ? board.getWhiteRooks() : board.getBlackRooks();
    }
}
