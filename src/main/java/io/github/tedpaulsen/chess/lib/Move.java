package io.github.tedpaulsen.chess.lib;

import lombok.Value;

@Value
public class Move {

    char piece;
    BitBoard from;
    BitBoard to;

    public Side getSide() {
        return piece <= 'Z' ? Side.WHITE : Side.BLACK;
    }

    @Override
    public String toString() {
        return """
            piece: %s
            from: (
            %s
            )
            to: (
            %s
            )""".formatted(
                piece,
                from,
                to
            );
    }
}
