package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Side;
import io.github.tedpaulsen.chess.lib.TestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KingMoveGenTest extends TestBase {

    @Test
    public void testKingMoves() {
        var board = BoardRepresentation.fromFen("r7/p1pk1p1p/1pNp1B2/1B6/2P5/4P3/Pp5P/2KR3R b - - 0 19");
        Assertions.assertEquals(
            """
            8 ........
            7 ........
            6 ........
            5 ........
            4 ........
            3 ........
            2 .111....
            1 .1......
              abcdefgh""",
            generateKingMoveTargets(Side.WHITE, board).toString()
        );
    }

    @Test
    public void testKingTargets() {
        var board = BoardRepresentation.fromFen("r7/p1pk1p1p/1pNp1B2/1B6/2P5/4P3/Pp5P/2KR3R b - - 0 19");
        Assertions.assertEquals(
            """
            8 ........
            7 ........
            6 ........
            5 ........
            4 ........
            3 ........
            2 .111....
            1 .1......
              abcdefgh""",
            kingMoveGen.getSquaresAttacked(Side.WHITE, board).toString()
        );
    }
}
