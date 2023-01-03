package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Side;
import io.github.tedpaulsen.chess.lib.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RookMoveGenTest extends TestBase {

    @Test
    public void testRookMoves() {
        var board = BoardRepresentation.fromFen("5rk1/2qb1ppp/1p1p1n2/1P2p3/1BP5/4P3/3NBPPP/5RK1 w - - 4 24");
        Assertions.assertEquals(
            """
              8 ........
              7 ........
              6 ........
              5 ........
              4 ........
              3 ........
              2 ........
              1 11111...
                abcdefgh""",
            generateRookTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testRookMovesWithFriendlyAndEnemyPieces() {
        var board = BoardRepresentation.fromFen("5rk1/2qb1p1p/1p1p2p1/1P6/2PRp3/4P1PP/3N1P2/n5K1 w - - 4 24");
        Assertions.assertEquals(
            """
              8 ........
              7 ........
              6 ...1....
              5 ...1....
              4 ....1...
              3 ...1....
              2 ........
              1 ........
                abcdefgh""",
            generateRookTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testRookInNECorner() {
        var board = BoardRepresentation.fromFen("7R/2qbrp2/1p1p1kp1/1P6/2P1p3/4P1P1/3N1P2/n5K1 w - - 4 24");
        Assertions.assertEquals(
            """
              8 1111111.
              7 .......1
              6 .......1
              5 .......1
              4 .......1
              3 .......1
              2 .......1
              1 .......1
                abcdefgh""",
            generateRookTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testRookInSECorner() {
        var board = BoardRepresentation.fromFen("8/2qbrp2/1p1p1kp1/1P6/2P1p3/4P1P1/3N1PK1/n6R w - - 4 24");
        Assertions.assertEquals(
            """
              8 .......1
              7 .......1
              6 .......1
              5 .......1
              4 .......1
              3 .......1
              2 .......1
              1 1111111.
                abcdefgh""",
            generateRookTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testRookInSWCorner() {
        var board = BoardRepresentation.fromFen("8/2qbrp2/1p1p1kp1/1P6/2P1p3/1n2P1P1/3N1PK1/R7 w - - 4 24");
        Assertions.assertEquals(
            """
              8 1.......
              7 1.......
              6 1.......
              5 1.......
              4 1.......
              3 1.......
              2 1.......
              1 .1111111
                abcdefgh""",
            generateRookTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testRookInNWCorner() {
        var board = BoardRepresentation.fromFen("R6n/2q1rp2/1p1p1kp1/1P6/2P1p3/4P1P1/3N1PK1/b7 w - - 4 24");
        Assertions.assertEquals(
            """
              8 .1111111
              7 1.......
              6 1.......
              5 1.......
              4 1.......
              3 1.......
              2 1.......
              1 1.......
                abcdefgh""",
            generateRookTargets(Side.WHITE, board).toString()
        );
    }
}
