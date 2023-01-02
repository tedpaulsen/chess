package io.github.tedpaulsen.chess.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveEngine {

    public List<Move> generateKingMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'K' : 'k';
        BitBoard king = side.is(Side.WHITE) ? board.getWhiteKing() : board.getBlackKing();

        addIfValid(moves, new Move(pieceCode, king, king.shiftLeft(9)));
        addIfValid(moves, new Move(pieceCode, king, king.shiftLeft(8)));
        addIfValid(moves, new Move(pieceCode, king, king.shiftLeft(7)));
        addIfValid(moves, new Move(pieceCode, king, king.shiftLeft(1)));
        addIfValid(moves, new Move(pieceCode, king, king.shiftRight(1)));
        addIfValid(moves, new Move(pieceCode, king, king.shiftRight(7)));
        addIfValid(moves, new Move(pieceCode, king, king.shiftRight(8)));
        addIfValid(moves, new Move(pieceCode, king, king.shiftRight(9)));

        return moves;
    }

    public List<Move> generateKnightMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'N' : 'n';
        BitBoard knights = side.is(Side.WHITE) ? board.getWhiteKnights() : board.getBlackKnights();

        for (BitBoard knight : knights.toSingletons()) {
            // NNE
            addIfValid(moves, new Move(pieceCode, knight, knight.notHFile().shiftLeft(17)));
            // NEE
            addIfValid(moves, new Move(pieceCode, knight, knight.notGFile().notHFile().shiftLeft(10)));
            // SEE
            addIfValid(moves, new Move(pieceCode, knight, knight.notGFile().notHFile().shiftRight(6)));
            // SSE
            addIfValid(moves, new Move(pieceCode, knight, knight.notHFile().shiftRight(15)));
            // NNW
            addIfValid(moves, new Move(pieceCode, knight, knight.notAFile().shiftLeft(15)));
            // NWW
            addIfValid(moves, new Move(pieceCode, knight, knight.notAFile().notBFile().shiftLeft(6)));
            // SWW
            addIfValid(moves, new Move(pieceCode, knight, knight.notAFile().notBFile().shiftRight(10)));
            // SSW
            addIfValid(moves, new Move(pieceCode, knight, knight.notAFile().shiftRight(17)));
        }

        return moves;
    }

    public void addIfValid(Collection<Move> moves, Move toAdd) {
        if (toAdd.getTo().getValue() != 0) {
            // destination is on the board
            moves.add(toAdd);
        }
    }
    // TODO: add methods for other pieces

}
