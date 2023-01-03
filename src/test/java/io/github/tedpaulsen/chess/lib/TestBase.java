package io.github.tedpaulsen.chess.lib;

import io.github.tedpaulsen.chess.lib.movegen.BishopMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.PawnMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.RookMoveGen;
import java.util.List;
import java.util.function.BiFunction;

public class TestBase {

    protected final BishopMoveGen bishopMoveGen = new BishopMoveGen();
    protected final RookMoveGen rookMoveGen = new RookMoveGen();
    protected final PawnMoveGen pawnMoveGen = new PawnMoveGen();

    public BitBoard generateBishopTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, bishopMoveGen::generateMoves);
    }

    public BitBoard generateRookTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, rookMoveGen::generateMoves);
    }

    public BitBoard generatePawnTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, pawnMoveGen::generateMoves);
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
