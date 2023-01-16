package io.github.tedpaulsen.chess.lib;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BoardRepresentation {

    BitBoard whitePawns;
    BitBoard whiteKnights;
    BitBoard whiteBishops;
    BitBoard whiteRooks;
    BitBoard whiteQueens;
    BitBoard whiteKing;
    BitBoard blackPawns;
    BitBoard blackKnights;
    BitBoard blackBishops;
    BitBoard blackRooks;
    BitBoard blackQueens;
    BitBoard blackKing;

    @Builder.Default
    BitBoard enPassantSquare = new BitBoard(0L);

    @Builder.Default
    String castlingInformation = "KQkq";

    public BitBoard getWhitePieces() {
        return new BitBoard(
            whitePawns.getValue() |
            whiteKnights.getValue() |
            whiteBishops.getValue() |
            whiteRooks.getValue() |
            whiteQueens.getValue() |
            whiteKing.getValue()
        );
    }

    public BitBoard getBlackPieces() {
        return new BitBoard(
            blackPawns.getValue() |
            blackKnights.getValue() |
            blackBishops.getValue() |
            blackRooks.getValue() |
            blackQueens.getValue() |
            blackKing.getValue()
        );
    }

    public BitBoard getEmpties() {
        return new BitBoard(~getBlackPieces().getValue() & ~getWhitePieces().getValue());
    }

    public BitBoard getPawns(Side side) {
        return side.is(Side.WHITE) ? whitePawns : blackPawns;
    }

    public BitBoard getKnights(Side side) {
        return side.is(Side.WHITE) ? whiteKnights : blackKnights;
    }

    public BitBoard getBishops(Side side) {
        return side.is(Side.WHITE) ? whiteBishops : blackBishops;
    }

    public BitBoard getRooks(Side side) {
        return side.is(Side.WHITE) ? whiteRooks : blackRooks;
    }

    public BitBoard getQueens(Side side) {
        return side.is(Side.WHITE) ? whiteQueens : blackQueens;
    }

    public BitBoard getKing(Side side) {
        return side.is(Side.WHITE) ? whiteKing : blackKing;
    }

    public BitBoard getKingSideRook(Side side) {
        return side.is(Side.WHITE)
            ? getWhiteRooks().mask(1L << 7) // h1
            : getBlackRooks().mask(1L << 63); // h8
    }

    public BitBoard getQueenSideRook(Side side) {
        return side.is(Side.WHITE)
            ? getWhiteRooks().mask(1L) // a1
            : getBlackRooks().mask(1L << 56); // a8
    }

    public BitBoard getPieces(char pieceCode) {
        return switch (pieceCode) {
            case 'K' -> getWhiteKing();
            case 'k' -> getBlackKing();
            case 'Q' -> getWhiteQueens();
            case 'q' -> getBlackQueens();
            case 'R' -> getWhiteRooks();
            case 'r' -> getBlackRooks();
            case 'B' -> getWhiteBishops();
            case 'b' -> getBlackBishops();
            case 'N' -> getWhiteKnights();
            case 'n' -> getBlackKnights();
            case 'P' -> getWhitePawns();
            case 'p' -> getBlackPawns();
            default -> throw new IllegalArgumentException("Invalid pieceCode");
        };
    }

    public BoardRepresentation set(char piece, BitBoard updatedPieces) {
        return switch (piece) {
            case 'K' -> this.toBuilder().whiteKing(updatedPieces).build();
            case 'k' -> this.toBuilder().blackKing(updatedPieces).build();
            case 'Q' -> this.toBuilder().whiteQueens(updatedPieces).build();
            case 'q' -> this.toBuilder().blackQueens(updatedPieces).build();
            case 'R' -> this.toBuilder().whiteRooks(updatedPieces).build();
            case 'r' -> this.toBuilder().blackRooks(updatedPieces).build();
            case 'B' -> this.toBuilder().whiteBishops(updatedPieces).build();
            case 'b' -> this.toBuilder().blackBishops(updatedPieces).build();
            case 'N' -> this.toBuilder().whiteKnights(updatedPieces).build();
            case 'n' -> this.toBuilder().blackKnights(updatedPieces).build();
            case 'P' -> this.toBuilder().whitePawns(updatedPieces).build();
            case 'p' -> this.toBuilder().blackPawns(updatedPieces).build();
            default -> throw new IllegalArgumentException("Invalid piece");
        };
    }

    public static BoardRepresentation initial() {
        // spotless:off
        BitBoard whitePawns   = new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_11111111_00000000L);
        BitBoard whiteKnights = new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_01000010L);
        BitBoard whiteBishops = new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00100100L);
        BitBoard whiteRooks   = new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_10000001L);
        BitBoard whiteQueens  = new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00001000L);
        BitBoard whiteKing    = new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00010000L);
        BitBoard blackPawns   = new BitBoard(0b00000000_11111111_00000000_00000000_00000000_00000000_00000000_00000000L);
        BitBoard blackKnights = new BitBoard(0b01000010_00000000_00000000_00000000_00000000_00000000_00000000_00000000L);
        BitBoard blackBishops = new BitBoard(0b00100100_00000000_00000000_00000000_00000000_00000000_00000000_00000000L);
        BitBoard blackRooks   = new BitBoard(0b10000001_00000000_00000000_00000000_00000000_00000000_00000000_00000000L);
        BitBoard blackQueens  = new BitBoard(0b00001000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L);
        BitBoard blackKing    = new BitBoard(0b00010000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L);
        // spotless:on
        return BoardRepresentation
            .builder()
            .whitePawns(whitePawns)
            .whiteKnights(whiteKnights)
            .whiteBishops(whiteBishops)
            .whiteRooks(whiteRooks)
            .whiteQueens(whiteQueens)
            .whiteKing(whiteKing)
            .blackPawns(blackPawns)
            .blackKnights(blackKnights)
            .blackBishops(blackBishops)
            .blackRooks(blackRooks)
            .blackQueens(blackQueens)
            .blackKing(blackKing)
            .build();
    }

    public static BoardRepresentation fromFen(String fen) {
        StringBuilder wP = new StringBuilder();
        StringBuilder wN = new StringBuilder();
        StringBuilder wB = new StringBuilder();
        StringBuilder wR = new StringBuilder();
        StringBuilder wQ = new StringBuilder();
        StringBuilder wK = new StringBuilder();
        StringBuilder bP = new StringBuilder();
        StringBuilder bN = new StringBuilder();
        StringBuilder bB = new StringBuilder();
        StringBuilder bR = new StringBuilder();
        StringBuilder bQ = new StringBuilder();
        StringBuilder bK = new StringBuilder();

        String[] fenParts = fen.trim().split(" ");

        String[] ranks = fenParts[0].split("/");
        for (int r = 0; r < ranks.length; r++) {
            for (int sq = ranks[r].length() - 1; sq >= 0; sq--) {
                char c = ranks[r].charAt(sq);
                if (c < 57) {
                    // digit
                    int empties = c - '0';
                    wP.append("0".repeat(empties));
                    wN.append("0".repeat(empties));
                    wB.append("0".repeat(empties));
                    wR.append("0".repeat(empties));
                    wQ.append("0".repeat(empties));
                    wK.append("0".repeat(empties));
                    bP.append("0".repeat(empties));
                    bN.append("0".repeat(empties));
                    bB.append("0".repeat(empties));
                    bR.append("0".repeat(empties));
                    bQ.append("0".repeat(empties));
                    bK.append("0".repeat(empties));
                } else if (c == 'P') {
                    wP.append("1");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'p') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("1");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'N') {
                    wP.append("0");
                    wN.append("1");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'n') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("1");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'B') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("1");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'b') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("1");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'R') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("1");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'r') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("1");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'Q') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("1");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'q') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("1");
                    bK.append("0");
                } else if (c == 'K') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("1");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("0");
                } else if (c == 'k') {
                    wP.append("0");
                    wN.append("0");
                    wB.append("0");
                    wR.append("0");
                    wQ.append("0");
                    wK.append("0");
                    bP.append("0");
                    bN.append("0");
                    bB.append("0");
                    bR.append("0");
                    bQ.append("0");
                    bK.append("1");
                }
            }
        }

        BoardRepresentation.BoardRepresentationBuilder builder = BoardRepresentation
            .builder()
            .whitePawns(new BitBoard(new BigInteger(wP.toString(), 2).longValue()))
            .whiteKnights(new BitBoard(new BigInteger(wN.toString(), 2).longValue()))
            .whiteBishops(new BitBoard(new BigInteger(wB.toString(), 2).longValue()))
            .whiteRooks(new BitBoard(new BigInteger(wR.toString(), 2).longValue()))
            .whiteQueens(new BitBoard(new BigInteger(wQ.toString(), 2).longValue()))
            .whiteKing(new BitBoard(new BigInteger(wK.toString(), 2).longValue()))
            .blackPawns(new BitBoard(new BigInteger(bP.toString(), 2).longValue()))
            .blackKnights(new BitBoard(new BigInteger(bN.toString(), 2).longValue()))
            .blackBishops(new BitBoard(new BigInteger(bB.toString(), 2).longValue()))
            .blackRooks(new BitBoard(new BigInteger(bR.toString(), 2).longValue()))
            .blackQueens(new BitBoard(new BigInteger(bQ.toString(), 2).longValue()))
            .blackKing(new BitBoard(new BigInteger(bK.toString(), 2).longValue()));

        builder.castlingInformation(fenParts[2]);

        String enPassantSquare = fenParts[3].toLowerCase();
        if (!"-".equals(enPassantSquare)) {
            long val = 1;

            int file = enPassantSquare.charAt(0) - 'a';
            int rank = enPassantSquare.charAt(1) - '1';

            val <<= file;
            val <<= rank * 8;

            builder.enPassantSquare(new BitBoard(val));
        }

        return builder.build();
    }
}
