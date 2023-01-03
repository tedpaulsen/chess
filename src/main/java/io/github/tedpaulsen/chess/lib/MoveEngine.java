package io.github.tedpaulsen.chess.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MoveEngine {

    public List<Move> generateKingMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'K' : 'k';
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard king = side.is(Side.WHITE) ? board.getWhiteKing() : board.getBlackKing();

        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftLeft(9)));
        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftLeft(8)));
        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftLeft(7)));
        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftLeft(1)));
        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftRight(1)));
        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftRight(7)));
        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftRight(8)));
        addIfValid(moves, friendlyPieces, new Move(pieceCode, king, king.shiftRight(9)));

        return moves;
    }

    public List<Move> generateKnightMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'N' : 'n';
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard knights = side.is(Side.WHITE) ? board.getWhiteKnights() : board.getBlackKnights();

        for (BitBoard knight : knights.toSingletons()) {
            // NNE
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notHFile().shiftLeft(17)));
            // NEE
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notGFile().notHFile().shiftLeft(10)));
            // SEE
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notGFile().notHFile().shiftRight(6)));
            // SSE
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notHFile().shiftRight(15)));
            // NNW
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notAFile().shiftLeft(15)));
            // NWW
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notAFile().notBFile().shiftLeft(6)));
            // SWW
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notAFile().notBFile().shiftRight(10)));
            // SSW
            addIfValid(moves, friendlyPieces, new Move(pieceCode, knight, knight.notAFile().shiftRight(17)));
        }

        return moves;
    }

    public void addIfValid(Collection<Move> moves, BitBoard friendlyPieces, Move toAdd) {
        // mask friendly pieces
        toAdd = new Move(toAdd.getPiece(), toAdd.getFrom(), toAdd.getTo().mask(~friendlyPieces.getValue()));

        if (toAdd.getTo().getValue() == 0) {
            // move is not on the board
            // or is a move onto a square occupied by a friendly piece
            return;
        }

        moves.add(toAdd);
    }
}
