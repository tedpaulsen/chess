package io.github.tedpaulsen.chess.lib;

public enum Side {
    WHITE(1),
    BLACK(-1);

    private final int val;

    Side(int val) {
        this.val = val;
    }

    public boolean is(Side s) {
        return this.equals(s);
    }
}
