package io.github.tedpaulsen.chess.lib;

import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BitBoardTest {

    @Test
    public void testToSingletons() {
        Assertions.assertEquals(
            Set.of(new BitBoard(0b0100), new BitBoard(0b0010)),
            new BitBoard(0b0110).toSingletons()
        );
        Assertions.assertEquals(
            Set.of(new BitBoard(0b1000), new BitBoard(0b0010), new BitBoard(0b0001)),
            new BitBoard(0b1011).toSingletons()
        );

        Assertions.assertEquals(
            Set.of(new BitBoard(-9223372036854775808L)),
            new BitBoard(-9223372036854775808L).toSingletons()
        );

        Assertions.assertEquals(
            Set.of(
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_10000000_00000000L),
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_01000000_00000000L),
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00100000_00000000L),
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00010000_00000000L),
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00001000_00000000L),
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000100_00000000L),
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000010_00000000L),
                new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_00000001_00000000L)
            ),
            BoardRepresentation.initial().getWhitePawns().toSingletons()
        );
    }

    @Test
    public void testIsSingleton() {
        Assertions.assertTrue(new BitBoard(0b0100).isSingleton());
        Assertions.assertTrue(new BitBoard(0b0001).isSingleton());
    }

    @Test
    public void testIsNotSingleton() {
        Assertions.assertFalse(new BitBoard(0b0110).isSingleton());
        Assertions.assertFalse(new BitBoard(0b0000).isSingleton());
    }

    @Test
    public void testCount() {
        Assertions.assertEquals(
            1,
            new BitBoard(0b00000000_00000000_00000000_00000000_00000000_00000000_01000000_00000000L).getPieceCount()
        );
    }
}
