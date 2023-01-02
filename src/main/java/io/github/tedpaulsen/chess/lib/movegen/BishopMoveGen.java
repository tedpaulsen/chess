package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.List;

public class BishopMoveGen extends SliderMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'B' : 'b';
        BitBoard bishops = side.is(Side.WHITE) ? board.getWhiteBishops() : board.getBlackBishops();
        BitBoard friendlyPieces =
            (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~bishops.getValue());
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard bishop : bishops.toSingletons()) {
            // NE
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    bishop,
                    b -> b.notHFile().notRank8().shiftLeft(9).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
            // NW
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    bishop,
                    b -> b.notAFile().notRank8().shiftLeft(7).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
            // SE
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    bishop,
                    b -> b.notHFile().notRank1().shiftRight(7).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
            // SW
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    bishop,
                    b -> b.notAFile().notRank1().shiftRight(9).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
        }

        return moves;
    }
}
