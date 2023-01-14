package io.github.tedpaulsen.chess.lib;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Move {

    char piece;
    BitBoard from;
    BitBoard to;
    Kind moveKind;

    public Move(char piece, BitBoard from, BitBoard to) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.moveKind = Kind.NORMAL;
    }

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

    public enum Kind {
        EN_PASSANT_CAPTURE("en-passant-capture"),
        PAWN_DOUBLE_ADVANCE("pawn-double-advance"),
        NORMAL("normal");

        private final String value;

        Kind(String value) {
            this.value = value;
        }
    }
}
