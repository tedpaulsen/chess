package io.github.tedpaulsen.chess.lib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BishopMovesTest {

    private final MoveEngine e = new MoveEngine();
    private BoardRepresentation board;

    @BeforeEach
    public void setup() {
        board = BoardRepresentation.fromFen("5rk1/2qb1ppp/1p1p1n2/1P2p3/1BP5/4P3/3NBPPP/5RK1 w - - 4 24");
    }

    @Test
    public void test() {
        BitBoard bishopTargets = new BitBoard(
            e
                .generateBishopMoves(Side.WHITE, board)
                .stream()
                .map(m -> m.getTo().getValue())
                .reduce(0L, (acc, el) -> acc | el)
        );
        Assertions.assertEquals(
            """
             8 ........
             7 ........
             6 ...1....
             5 1.1....1
             4 ......1.
             3 1.11.1..
             2 ........
             1 ...1....
               abcdefgh""",
            bishopTargets.toString()
        );
    }
}
