package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Side;
import io.github.tedpaulsen.chess.lib.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueenMoveGenTest extends TestBase {

    @Test
    public void testQueenMoveGen() {
        var b = BoardRepresentation.fromFen("r6k/p1p2Q1p/1bp4p/3pP3/3P2bq/2P1R3/PP3PP1/6K1 b - - 3 25");
        Assertions.assertEquals(
            """
            8 ....111.
            7 ..111.11
            6 ....111.
            5 ...1.1.1
            4 .....1..
            3 .....1..
            2 ........
            1 ........
              abcdefgh""",
            generateQueenTargets(Side.WHITE, b).toString()
        );
    }
}
