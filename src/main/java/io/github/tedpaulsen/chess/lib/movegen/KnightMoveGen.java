package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.List;

public class KnightMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'N' : 'n';
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard knights = side.is(Side.WHITE) ? board.getWhiteKnights() : board.getBlackKnights();

        for (BitBoard knight : knights.toSingletons()) {
            // NNE
            moves.add(new Move(pieceCode, knight, knight.notHFile().shiftLeft(17).mask(~friendlyPieces.getValue())));
            // NEE
            moves.add(
                new Move(pieceCode, knight, knight.notGFile().notHFile().shiftLeft(10).mask(~friendlyPieces.getValue()))
            );
            // SEE
            moves.add(
                new Move(pieceCode, knight, knight.notGFile().notHFile().shiftRight(6).mask(~friendlyPieces.getValue()))
            );
            // SSE
            moves.add(new Move(pieceCode, knight, knight.notHFile().shiftRight(15).mask(~friendlyPieces.getValue())));
            // NNW
            moves.add(new Move(pieceCode, knight, knight.notAFile().shiftLeft(15).mask(~friendlyPieces.getValue())));
            // NWW
            moves.add(
                new Move(pieceCode, knight, knight.notAFile().notBFile().shiftLeft(6).mask(~friendlyPieces.getValue()))
            );
            // SWW
            moves.add(
                new Move(
                    pieceCode,
                    knight,
                    knight.notAFile().notBFile().shiftRight(10).mask(~friendlyPieces.getValue())
                )
            );
            // SSW
            moves.add(new Move(pieceCode, knight, knight.notAFile().shiftRight(17).mask(~friendlyPieces.getValue())));
        }

        return moves;
    }
}
