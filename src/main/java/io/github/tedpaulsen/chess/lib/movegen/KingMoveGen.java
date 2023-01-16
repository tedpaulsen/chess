package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KingMoveGen implements MoveGen {

    @Override
    public List<Move> generateMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'K' : 'k';
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard king = side.is(Side.WHITE) ? board.getWhiteKing() : board.getBlackKing();

        // normal moves
        moves.add(new Move(pieceCode, king, king.shiftLeft(9).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftLeft(8).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftLeft(7).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftLeft(1).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(1).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(7).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(8).mask(~friendlyPieces.getValue())));
        moves.add(new Move(pieceCode, king, king.shiftRight(9).mask(~friendlyPieces.getValue())));

        // castling
        kingSideCastle(side, board).ifPresent(moves::add);
        queenSideCastle(side, board).ifPresent(moves::add);

        return moves.stream().filter(m -> !m.getTo().isEmpty()).toList();
    }

    private Optional<Move> kingSideCastle(Side side, BoardRepresentation board) {
        String piece = side.is(Side.WHITE) ? "K" : "k";

        if (!board.getCastlingInformation().contains(piece)) {
            return Optional.empty();
        }

        BitBoard king = board.getKing(side);
        BitBoard rook = board.getKingSideRook(side);
        BitBoard empties = board.getEmpties();

        if (rook.isEmpty()) {
            return Optional.empty();
        }

        BitBoard dest = king.shiftLeft(1).mask(empties).shiftLeft(1).mask(empties);
        return dest.isEmpty() ? Optional.empty() : Optional.of(new Move(piece.charAt(0), king, dest));
    }

    private Optional<Move> queenSideCastle(Side side, BoardRepresentation board) {
        char piece = side.is(Side.WHITE) ? 'K' : 'k';
        String castleCode = side.is(Side.WHITE) ? "Q" : "q";

        if (!board.getCastlingInformation().contains(castleCode)) {
            return Optional.empty();
        }

        BitBoard king = board.getKing(side);
        BitBoard rook = board.getKingSideRook(side);
        BitBoard empties = board.getEmpties();

        if (rook.isEmpty()) {
            return Optional.empty();
        }

        BitBoard dest = king.shiftRight(1).mask(empties).shiftRight(1).mask(empties);
        return dest.isEmpty() ? Optional.empty() : Optional.of(new Move(piece, king, dest));
    }

    @Override
    public BitBoard getSquaresAttacked(Side side, BoardRepresentation board) {
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard king = get(side, board);
        return king
            .shiftLeft(9)
            .union(king.shiftLeft(8))
            .union(king.shiftLeft(7))
            .union(king.shiftLeft(1))
            .union(king.shiftRight(1))
            .union(king.shiftRight(7))
            .union(king.shiftRight(8))
            .union(king.shiftRight(9))
            .intersect(~friendlyPieces.getValue());
    }

    @Override
    public BitBoard get(Side side, BoardRepresentation board) {
        return side.is(Side.WHITE) ? board.getWhiteKing() : board.getBlackKing();
    }
}
