package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Side;
import io.github.tedpaulsen.chess.lib.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BishopMoveGenTest extends TestBase {

    @Test
    public void testBishopWithFriendlyAndEnemyPieces() {
        var board = BoardRepresentation.fromFen("5rk1/2qb1ppp/1p1p1n2/1P2p3/1BP5/4P3/3NBPPP/5RK1 w - - 4 24");
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
            generateBishopTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testBishopInSECorner() {
        var board = BoardRepresentation.fromFen("5rk1/2qb1ppp/1p1p1n2/1P2p3/2P5/4P1PP/3N1P2/5RKB w - - 4 24");
        Assertions.assertEquals(
            """
             8 1.......
             7 .1......
             6 ..1.....
             5 ...1....
             4 ....1...
             3 .....1..
             2 ......1.
             1 ........
               abcdefgh""",
            generateBishopTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testBishopInSWCorner() {
        var board = BoardRepresentation.fromFen("5rk1/2qb1ppp/1p1p1n2/1P2p3/2P5/4P3/3N1PPP/B4RK1 w - - 4 24");
        Assertions.assertEquals(
            """
             8 ........
             7 ........
             6 ........
             5 ....1...
             4 ...1....
             3 ..1.....
             2 .1......
             1 ........
               abcdefgh""",
            generateBishopTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testBishopInNWCorner() {
        var board = BoardRepresentation.fromFen("B4rk1/2qb1ppp/1p1p1n2/1P2p3/2P5/4P1PP/3N1P2/5RK1 w - - 4 24");
        Assertions.assertEquals(
            """
             8 ........
             7 .1......
             6 ..1.....
             5 ...1....
             4 ....1...
             3 .....1..
             2 ......1.
             1 .......1
               abcdefgh""",
            generateBishopTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testBishopInNECorner() {
        var board = BoardRepresentation.fromFen("5rkB/2qb1p1p/1p1p2p1/1P6/2P1p3/4P1PP/3N1P2/n4RK1 w - - 4 24");
        Assertions.assertEquals(
            """
             8 ........
             7 ......1.
             6 .....1..
             5 ....1...
             4 ...1....
             3 ..1.....
             2 .1......
             1 1.......
               abcdefgh""",
            generateBishopTargets(Side.WHITE, board).toString()
        );
    }
}
