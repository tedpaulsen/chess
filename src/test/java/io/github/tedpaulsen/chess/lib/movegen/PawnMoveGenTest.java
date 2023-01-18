package io.github.tedpaulsen.chess.lib.movegen;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Move;
import io.github.tedpaulsen.chess.lib.Side;
import io.github.tedpaulsen.chess.lib.Square;
import io.github.tedpaulsen.chess.lib.TestBase;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PawnMoveGenTest extends TestBase {

    @Test
    public void testPawnMoves() {
        var b = BoardRepresentation.fromFen("R6n/2q1rp2/1p1p1kp1/1P6/2P1p3/5PP1/3P2K1/b7 w - - 4 24");
        Assertions.assertEquals(
            """
            8 ........
            7 ........
            6 ........
            5 ..1.....
            4 ...1111.
            3 ...1....
            2 ........
            1 ........
              abcdefgh""",
            generatePawnMoveTargets(Side.WHITE, b).toString()
        );
    }

    @Test
    public void testPawnTargets() {
        var b = BoardRepresentation.fromFen("R6n/2q1rp2/1p1p1kp1/1P6/2P1p3/5PP1/3P2K1/b7 w - - 4 24");
        Assertions.assertEquals(
            """
            8 ........
            7 ........
            6 1.1.....
            5 .1.1....
            4 ....1111
            3 ..1.1...
            2 ........
            1 ........
              abcdefgh""",
            pawnMoveGen.getSquaresAttacked(Side.WHITE, b).toString()
        );
    }

    @Test
    public void testPawnAdvanceMoves() {
        var b = BoardRepresentation.fromFen("4k3/R1q3P1/2p5/4P3/p1P5/P7/8/6K1 w - - 4 24");
        Assertions.assertEquals(
            """
            8 ......1.
            7 ........
            6 ....1...
            5 ..1.....
            4 ........
            3 ........
            2 ........
            1 ........
              abcdefgh""",
            generatePawnMoveTargets(Side.WHITE, b).toString()
        );
    }

    @Test
    public void testPawnRightCapture() {
        var b = BoardRepresentation.fromFen("4k1Nq/6P1/5N2/4P3/Rpp5/P7/8/6K1 w - - 4 24");
        Assertions.assertEquals(
            """
            8 .......1
            7 ........
            6 ....1...
            5 ........
            4 .1......
            3 ........
            2 ........
            1 ........
              abcdefgh""",
            generatePawnMoveTargets(Side.WHITE, b).toString()
        );
    }

    @Test
    public void testPawnLeftCapture() {
        var b = BoardRepresentation.fromFen("4k1N1/8/8/8/Rpp5/2P1qN2/5P2/6K1 w - - 4 24");
        Assertions.assertEquals(
            """
            8 ........
            7 ........
            6 ........
            5 ........
            4 .1......
            3 ....1...
            2 ........
            1 ........
              abcdefgh""",
            generatePawnMoveTargets(Side.WHITE, b).toString()
        );
    }

    @Test
    public void testPawnDoubleAdvance() {
        var b = BoardRepresentation.fromFen("4k1N1/8/8/4q3/R1p5/4B3/P1P1P1P1/6K1 w - - 4 24");
        Assertions.assertEquals(
            """
            8 ........
            7 ........
            6 ........
            5 ........
            4 ......1.
            3 1.1...1.
            2 ........
            1 ........
              abcdefgh""",
            generatePawnMoveTargets(Side.WHITE, b).toString()
        );
    }

    @Test
    public void pawnsGivingCheck() {
        var b = BoardRepresentation.fromFen("R6n/2q1rp2/1p1p1kp1/1P4P1/2P1pPK1/8/3P4/b7 w - - 4 24");
        Assertions.assertTrue(moveEngine.isInCheck(Side.BLACK, b));
    }

    @Test
    public void testEnPassantCapture() {
        var b = BoardRepresentation.fromFen("rnbqkbnr/pppp2pp/4p3/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3");
        Assertions.assertEquals(
            """
            8 ........
            7 ........
            6 .....1..
            5 ........
            4 1111.111
            3 1111.111
            2 ........
            1 ........
              abcdefgh""",
            generatePawnMoveTargets(Side.WHITE, b).toString()
        );
    }

    @Test
    public void testPawnPromotion() {
        var b = BoardRepresentation.fromFen("8/2PR3p/p5k1/1P4p1/8/1P4p1/5P1P/4R1K1 w - - 1 45");
        List<Move> pawnC7Moves = pawnMoveGen
            .generateMoves(Side.WHITE, b)
            .stream()
            .filter(move -> move.getFrom().equals(Square.C(7)))
            .toList();

        // pawn on c7 can promote to queen, rook, bishop, or knight
        Assertions.assertEquals(4, pawnC7Moves.size());
        Assertions.assertEquals(
            Set.of(
                Move.Kind.PAWN_PROMOTE_TO_QUEEN,
                Move.Kind.PAWN_PROMOTE_TO_ROOK,
                Move.Kind.PAWN_PROMOTE_TO_BISHOP,
                Move.Kind.PAWN_PROMOTE_TO_KNIGHT
            ),
            pawnC7Moves.stream().map(Move::getMoveKind).collect(Collectors.toSet())
        );
        Assertions.assertEquals(Set.of(Square.C(8)), pawnC7Moves.stream().map(Move::getTo).collect(Collectors.toSet()));
    }
}
