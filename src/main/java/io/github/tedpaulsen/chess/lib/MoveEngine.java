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

    public List<Move> generatePawnMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char pieceCode = side.is(Side.WHITE) ? 'P' : 'p';
        BitBoard friendlyPieces = side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();
        BitBoard empties = new BitBoard(~friendlyPieces.getValue() | ~enemyPieces.getValue());
        BitBoard pawns = side.is(Side.WHITE) ? board.getWhitePawns() : board.getBlackPawns();

        for (BitBoard pawn : pawns.toSingletons()) {
            if (side.is(Side.WHITE)) {
                // advance
                addIfValid(moves, friendlyPieces, new Move(pieceCode, pawn, pawn.shiftLeft(8).mask(empties)));
                // left capture
                addIfValid(moves, friendlyPieces, new Move(pieceCode, pawn, pawn.notAFile().shiftLeft(7)));
                // right capture
                addIfValid(moves, friendlyPieces, new Move(pieceCode, pawn, pawn.notHFile().shiftLeft(9)));
                // double advance
                addIfValid(
                    moves,
                    friendlyPieces,
                    new Move(pieceCode, pawn, pawn.rank2().shiftLeft(8).mask(empties).shiftLeft(8).mask(empties))
                );
                // TODO: en-passant capture
            } else {
                // advance
                addIfValid(moves, friendlyPieces, new Move(pieceCode, pawn, pawn.shiftRight(8).mask(empties)));
                // left capture
                addIfValid(moves, friendlyPieces, new Move(pieceCode, pawn, pawn.notAFile().shiftRight(7)));
                // right capture
                addIfValid(moves, friendlyPieces, new Move(pieceCode, pawn, pawn.notHFile().shiftRight(9)));
                // double advance
                addIfValid(
                    moves,
                    friendlyPieces,
                    new Move(pieceCode, pawn, pawn.rank7().shiftRight(8).mask(empties).shiftRight(8).mask(empties))
                );
                // TODO: en-passant capture
            }
        }

        return moves;
    }

    public List<Move> generateBishopMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'B' : 'b';
        BitBoard bishops = side.is(Side.WHITE) ? board.getWhiteBishops() : board.getBlackBishops();
        BitBoard friendlyPieces =
            (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~bishops.getValue());
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard bishop : bishops.toSingletons()) {
            // NE
            long loc = bishop.getValue();
            do {
                loc = loc << 9;

                if (friendlyPieces.mask(loc).getValue() != 0) {
                    // we've found a friendly piece along the ray
                    break;
                } else if (enemyPieces.mask(loc).getValue() != 0) {
                    // we've found an enemy piece we can capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                    break;
                } else {
                    // a silent move
                    // TODO: add some flag to denote a silent move vs a capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                }
            } while ((loc & Masks.RANK_8) == 0 && (loc & Masks.H_FILE) == 0);
            // NW
            loc = bishop.getValue();
            do {
                loc = loc << 7;

                if (friendlyPieces.mask(loc).getValue() != 0) {
                    // we've found a friendly piece along the ray
                    break;
                } else if (enemyPieces.mask(loc).getValue() != 0) {
                    // we've found an enemy piece we can capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                    break;
                } else {
                    // a silent move
                    // TODO: add some flag to denote a silent move vs a capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                }
            } while ((loc & Masks.RANK_8) == 0 && (loc & Masks.A_FILE) == 0);
            // SE
            loc = bishop.getValue();
            do {
                loc = loc >> 7;

                if (friendlyPieces.mask(loc).getValue() != 0) {
                    // we've found a friendly piece along the ray
                    break;
                } else if (enemyPieces.mask(loc).getValue() != 0) {
                    // we've found an enemy piece we can capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                    break;
                } else {
                    // a silent move
                    // TODO: add some flag to denote a silent move vs a capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                }
            } while ((loc & Masks.RANK_1) == 0 && (loc & Masks.H_FILE) == 0);
            // SW
            loc = bishop.getValue();
            do {
                loc = loc >> 9;

                if (friendlyPieces.mask(loc).getValue() != 0) {
                    // we've found a friendly piece along the ray
                    break;
                } else if (enemyPieces.mask(loc).getValue() != 0) {
                    // we've found an enemy piece we can capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                    break;
                } else {
                    // a silent move
                    // TODO: add some flag to denote a silent move vs a capture
                    moves.add(new Move(piece, bishop, new BitBoard(loc)));
                }
            } while ((loc & Masks.RANK_1) == 0 && (loc & Masks.A_FILE) == 0);
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
