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
            BitBoard target = bishop.notHFile().notRank8().shiftLeft(9).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, bishop, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask h file and rank 8 to ensure we don't wrap around, then slide NE, then mask friendly pieces
                target = target.notHFile().notRank8().shiftLeft(9).mask(~friendlyPieces.getValue());
            }
            // NW
            target = bishop.notAFile().notRank8().shiftLeft(7).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, bishop, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask a file and rank 8 to ensure we don't wrap around, then slide NW, then mask friendly pieces
                target = target.notAFile().notRank8().shiftLeft(7).mask(~friendlyPieces.getValue());
            }
            // SE
            target = bishop.notHFile().notRank1().shiftRight(7).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, bishop, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask h file and rank 1 to ensure we don't wrap around, then slide SE, then mask friendly pieces
                target = target.notHFile().notRank1().shiftRight(7).mask(~friendlyPieces.getValue());
            }
            // SW
            target = bishop.notAFile().notRank1().shiftRight(9).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, bishop, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask h file and rank 1 to ensure we don't wrap around, then slide SE, then mask friendly pieces
                target = target.notAFile().notRank1().shiftRight(9).mask(~friendlyPieces.getValue());
            }
        }

        return moves;
    }

    public List<Move> generateRookMoves(Side side, BoardRepresentation board) {
        List<Move> moves = new ArrayList<>();

        char piece = side.is(Side.WHITE) ? 'R' : 'r';
        BitBoard rooks = side.is(Side.WHITE) ? board.getWhiteRooks() : board.getBlackRooks();
        BitBoard enemyPieces = side.is(Side.WHITE) ? board.getBlackPieces() : board.getWhitePieces();

        for (BitBoard rook : rooks.toSingletons()) {
            BitBoard friendlyPieces =
                (side.is(Side.WHITE) ? board.getWhitePieces() : board.getBlackPieces()).mask(~rooks.getValue());
            // N
            BitBoard target = rook.notRank8().shiftLeft(8).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, target, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask rank 8 to ensure we don't move off board, then slide N, then mask friendly pieces
                target = target.notRank8().shiftLeft(8).mask(~friendlyPieces.getValue());
            }

            // E
            target = rook.notHFile().shiftLeft(1).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, target, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask H file to ensure we don't move off board, then slide E, then mask friendly pieces
                target = target.notHFile().shiftLeft(1).mask(~friendlyPieces.getValue());
            }

            // S
            target = rook.notRank1().shiftRight(8).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, target, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask rank 1 to ensure we don't move off board, then slide S, then mask friendly pieces
                target = target.notRank1().shiftRight(8).mask(~friendlyPieces.getValue());
            }

            // W
            target = rook.notAFile().shiftRight(1).mask(~friendlyPieces.getValue());
            while (!target.empty()) {
                moves.add(new Move(piece, target, target));
                if (!enemyPieces.mask(target).empty()) {
                    // we've hit an enemy piece, break here
                    break;
                }
                // mask A file to ensure we don't move off board, then slide W, then mask friendly pieces
                target = target.notAFile().shiftRight(1).mask(~friendlyPieces.getValue());
            }
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
