package io.github.tedpaulsen.chess.lib;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder(toBuilder = true)
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
        PAWN_PROMOTE_TO_QUEEN("pawn-promote-to-queen"),
        PAWN_PROMOTE_TO_ROOK("pawn-promote-to-rook"),
        PAWN_PROMOTE_TO_BISHOP("pawn-promote-to-bishop"),
        PAWN_PROMOTE_TO_KNIGHT("pawn-promote-to-knight"),
        KING_SIDE_CASTLE("king-side-castle"),
        QUEEN_SIDE_CASTLE("queen-side-castle"),
        NORMAL("normal");

        private final String value;

        Kind(String value) {
            this.value = value;
        }
    }
}
