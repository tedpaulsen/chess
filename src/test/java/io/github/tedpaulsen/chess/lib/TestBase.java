package io.github.tedpaulsen.chess.lib;

import java.util.List;
import java.util.function.BiFunction;

public class TestBase {

    protected final MoveEngine moveEngine = new MoveEngine();

    public BitBoard generateBishopTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, moveEngine::generateBishopMoves);
    }

    public BitBoard generateRookTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, moveEngine::generateRookMoves);
    }

    private BitBoard generateTargets(
        Side side,
        BoardRepresentation board,
        BiFunction<Side, BoardRepresentation, List<Move>> moveGenerator
    ) {
        return new BitBoard(
            moveGenerator.apply(side, board).stream().map(m -> m.getTo().getValue()).reduce(0L, (acc, el) -> acc | el)
        );
    }
}
