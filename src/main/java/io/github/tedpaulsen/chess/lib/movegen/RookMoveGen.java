package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.List;

public class RookMoveGen extends SliderMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'R' : 'r';
        BitBoard rooks = side.is(Side.WHITE) ? board.getWhiteRooks() : board.getBlackRooks();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard rook : rooks.toSingletons()) {
            BitBoard friendlyPieces =
                (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~rooks.getValue());

            // N
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    rook,
                    b -> b.notRank8().shiftLeft(8).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
            // E
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    rook,
                    b -> b.notHFile().shiftLeft(1).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
            // S
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    rook,
                    b -> b.notRank1().shiftRight(8).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
            // W
            moves.addAll(
                generateMovesFromTransform(
                    piece,
                    rook,
                    b -> b.notAFile().shiftRight(1).mask(~friendlyPieces.getValue()),
                    enemyPieces
                )
            );
        }

        return moves;
    }
}
