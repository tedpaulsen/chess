package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.List;

public class KingMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'K' : 'k';
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard king = side.is(Side.WHITE) ? board.getWhiteKing() : board.getBlackKing();

        moves.add(new Move(pieceCode, king, king.shiftLeft(9).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftLeft(8).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftLeft(7).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftLeft(1).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(1).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(7).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(8).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(9).mask(~friendlyPieces.getValue())));

        return moves.stream().filter(m -> !m.getTo().empty()).toList();
    }
}
