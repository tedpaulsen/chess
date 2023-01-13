package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoveGen extends SliderMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'B' : 'b';
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard bishop : get(side, board).toSingletons()) {
            BitBoard friendlyPieces =
                (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~bishop.getValue());

            moves.addAll(
                diagonalTransforms(friendlyPieces)
                    .stream()
                    .map(transform -> generateMovesFromTransform(piece, bishop, transform, enemyPieces))
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

        BitBoard acc = new BitBoard(0L);
        for (BitBoard bishop : get(side, board).toSingletons()) {
            acc =
                acc.union(
                    diagonalTransforms(friendlyPieces)
                        .stream()
                        .map(transform -> generateTargetSquaresFromTransform(bishop, transform, enemyPieces))
                        .reduce(new BitBoard(0L), BitBoard::union)
                );
        }
        return acc;
    }

    @Override
    public BitBoard get(Side side, BoardRepresentation board) {
        return side.is(Side.WHITE) ? board.getWhiteBishops() : board.getBlackBishops();
    }
}
