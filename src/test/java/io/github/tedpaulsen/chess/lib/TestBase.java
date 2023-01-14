package io.github.tedpaulsen.chess.lib;

import io.github.tedpaulsen.chess.lib.evaluation.CompositeEvaluator;
import io.github.tedpaulsen.chess.lib.evaluation.Evaluator;
import io.github.tedpaulsen.chess.lib.evaluation.MaterialEvaluator;
import io.github.tedpaulsen.chess.lib.movegen.BishopMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.KingMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.KnightMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.MoveGen;
import io.github.tedpaulsen.chess.lib.movegen.PawnMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.QueenMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.RookMoveGen;

public class TestBase {

    protected final PawnMoveGen pawnMoveGen;
    protected final KnightMoveGen knightMoveGen;
    protected final BishopMoveGen bishopMoveGen;
    protected final RookMoveGen rookMoveGen;
    protected final QueenMoveGen queenMoveGen;
    protected final KingMoveGen kingMoveGen;

    protected final MoveEngine moveEngine;

    protected final Evaluator compositeEvaluator;

    protected final MoveApplier moveApplier;

    public TestBase() {
        this.pawnMoveGen = new PawnMoveGen();
        this.knightMoveGen = new KnightMoveGen();
        this.bishopMoveGen = new BishopMoveGen();
        this.rookMoveGen = new RookMoveGen();
        this.queenMoveGen = new QueenMoveGen();
        this.kingMoveGen = new KingMoveGen();

        this.compositeEvaluator = CompositeEvaluator.of(new MaterialEvaluator());

        this.moveApplier = new MoveApplier();

        this.moveEngine =
            new MoveEngine(
                pawnMoveGen,
                knightMoveGen,
                bishopMoveGen,
                rookMoveGen,
                queenMoveGen,
                kingMoveGen,
                compositeEvaluator,
                moveApplier
            );
    }

    public BitBoard generateBishopMoveTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, bishopMoveGen);
    }

    public BitBoard generateRookMoveTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, rookMoveGen);
    }

    public BitBoard generatePawnMoveTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, pawnMoveGen);
    }

    public BitBoard generateQueenMoveTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, queenMoveGen);
    }

    public BitBoard generateKingMoveTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, kingMoveGen);
    }

    private BitBoard generateTargets(Side side, BoardRepresentation board, MoveGen moveGenerator) {
        return new BitBoard(
            moveGenerator
                .generateMoves(side, board)
                .stream()
                .map(m -> m.getTo().getValue())
                .reduce(0L, (acc, el) -> acc | el)
        );
    }
}
