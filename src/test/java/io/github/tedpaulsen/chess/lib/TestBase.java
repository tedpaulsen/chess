package io.github.tedpaulsen.chess.lib;

import io.github.tedpaulsen.chess.lib.movegen.BishopMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.RookMoveGen;

import java.util.List;
import java.util.function.BiFunction;

public class TestBase {

    protected final MoveEngine moveEngine = new MoveEngine();
    protected final BishopMoveGen bishopMoveGen = new BishopMoveGen();
    protected final RookMoveGen rookMoveGen = new RookMoveGen();

    public BitBoard generateBishopTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, bishopMoveGen::generateMoves);
    }

    public BitBoard generateRookTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, rookMoveGen::generateMoves);
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
