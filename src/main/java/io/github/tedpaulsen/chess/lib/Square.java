package io.github.tedpaulsen.chess.lib;

public class Square {

    public static BitBoard A(int rank) {
        return new BitBoard(1L << 8 * (rank - 1));
    }

    public static BitBoard B(int rank) {
        return new BitBoard((1L << 1) << 8 * (rank - 1));
    }

    public static BitBoard C(int rank) {
        return new BitBoard((1L << 2) << 8 * (rank - 1));
    }

    public static BitBoard D(int rank) {
        return new BitBoard((1L << 3) << 8 * (rank - 1));
    }

    public static BitBoard E(int rank) {
        return new BitBoard((1L << 4) << 8 * (rank - 1));
    }

    public static BitBoard F(int rank) {
        return new BitBoard((1L << 5) << 8 * (rank - 1));
    }

    public static BitBoard G(int rank) {
        return new BitBoard((1L << 6) << 8 * (rank - 1));
    }

    public static BitBoard H(int rank) {
        return new BitBoard((1L << 7) << 8 * (rank - 1));
    }
}
