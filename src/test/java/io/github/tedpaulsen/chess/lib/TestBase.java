package io.github.tedpaulsen.chess.lib;

import io.github.tedpaulsen.chess.lib.movegen.BishopMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.MoveGen;
import io.github.tedpaulsen.chess.lib.movegen.PawnMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.QueenMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.RookMoveGen;

public class TestBase {

    protected final BishopMoveGen bishopMoveGen = new BishopMoveGen();
    protected final RookMoveGen rookMoveGen = new RookMoveGen();
    protected final PawnMoveGen pawnMoveGen = new PawnMoveGen();
    protected final QueenMoveGen queenMoveGen = new QueenMoveGen();

    public BitBoard generateBishopTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, bishopMoveGen);
    }

    public BitBoard generateRookTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, rookMoveGen);
    }

    public BitBoard generatePawnTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, pawnMoveGen);
    }

    public BitBoard generateQueenTargets(Side side, BoardRepresentation board) {
        return generateTargets(side, board, queenMoveGen);
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
