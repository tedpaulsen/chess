package io.github.tedpaulsen.chess.lib;

import java.math.BigInteger;
import lombok.Value;

@Value
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

    private BoardRepresentation(
        BitBoard whitePawns,
        BitBoard whiteKnights,
        BitBoard whiteBishops,
        BitBoard whiteRooks,
        BitBoard whiteQueens,
        BitBoard whiteKing,
        BitBoard blackPawns,
        BitBoard blackKnights,
        BitBoard blackBishops,
        BitBoard blackRooks,
        BitBoard blackQueens,
        BitBoard blackKing
    ) {
        this.whitePawns = whitePawns;
        this.whiteKnights = whiteKnights;
        this.whiteBishops = whiteBishops;
        this.whiteRooks = whiteRooks;
        this.whiteQueens = whiteQueens;
        this.whiteKing = whiteKing;
        this.blackPawns = blackPawns;
        this.blackKnights = blackKnights;
        this.blackBishops = blackBishops;
        this.blackRooks = blackRooks;
        this.blackQueens = blackQueens;
        this.blackKing = blackKing;
    }

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
        return new BoardRepresentation(
            whitePawns,
            whiteKnights,
            whiteBishops,
            whiteRooks,
            whiteQueens,
            whiteKing,
            blackPawns,
            blackKnights,
            blackBishops,
            blackRooks,
            blackQueens,
            blackKing
        );
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

        String[] ranks = fen.trim().split(" ")[0].split("/");
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

        return new BoardRepresentation(
            new BitBoard(new BigInteger(wP.toString(), 2).longValue()),
            new BitBoard(new BigInteger(wN.toString(), 2).longValue()),
            new BitBoard(new BigInteger(wB.toString(), 2).longValue()),
            new BitBoard(new BigInteger(wR.toString(), 2).longValue()),
            new BitBoard(new BigInteger(wQ.toString(), 2).longValue()),
            new BitBoard(new BigInteger(wK.toString(), 2).longValue()),
            new BitBoard(new BigInteger(bP.toString(), 2).longValue()),
            new BitBoard(new BigInteger(bN.toString(), 2).longValue()),
            new BitBoard(new BigInteger(bB.toString(), 2).longValue()),
            new BitBoard(new BigInteger(bR.toString(), 2).longValue()),
            new BitBoard(new BigInteger(bQ.toString(), 2).longValue()),
            new BitBoard(new BigInteger(bK.toString(), 2).longValue())
        );
    }
}
