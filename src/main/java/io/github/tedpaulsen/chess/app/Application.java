package io.github.tedpaulsen.chess.app;

import io.github.tedpaulsen.chess.lib.BitBoard;
import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.MoveEngine;
import io.github.tedpaulsen.chess.lib.Side;

public class Application {

    public static void main(String[] args) {
        BoardRepresentation b = new BoardRepresentation();
        MoveEngine e = new MoveEngine();

        long knightOrigins = e
            .generateKnightMoves(Side.WHITE, b)
            .stream()
            .map(m -> m.getFrom().getValue())
            .reduce(0L, (acc, el) -> acc | el);

        long validKnightDestinations = e
            .generateKnightMoves(Side.WHITE, b)
            .stream()
            .map(m -> m.getTo().getValue())
            .reduce(0L, (acc, el) -> acc | el);

        System.out.println("knight origins");
        System.out.println(new BitBoard(knightOrigins));
        System.out.println("\nvalid knight destinations");
        System.out.println(new BitBoard(validKnightDestinations));
        /*
        knight origins
        8 ........
        7 ........
        6 ........
        5 ........
        4 ........
        3 ........
        2 ........
        1 .1....1.
          abcdefgh

        valid knight destinations
        8 ........
        7 ........
        6 ........
        5 ........
        4 ........
        3 1.1..1.1
        2 ...11...
        1 ........
          abcdefgh
         */
    }
}
