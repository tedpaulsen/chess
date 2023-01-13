package io.github.tedpaulsen.chess.lib.evaluation;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Side;

public class MaterialEvaluator extends Evaluator {

    private static final int PAWN_WT = 1;
    private static final int KNIGHT_WT = 3;
    private static final int BISHOP_WT = 3;
    private static final int ROOK_WT = 5;
    private static final int QUEEN_WT = 9;
    private static final int KING_WT = 200;

    private static final double BISHOP_PAIR = 0.5;

    @Override
    public double evaluate(Side side, BoardRepresentation board) {
        // spotless:off
        return (
            PAWN_WT * board.getPawns(side).getPieceCount() +
            KNIGHT_WT * board.getKnights(side).getPieceCount() +
            BISHOP_WT * board.getBishops(side).getPieceCount() +
            ROOK_WT * board.getRooks(side).getPieceCount() +
            QUEEN_WT * board.getQueens(side).getPieceCount() +
            KING_WT * board.getKing(side).getPieceCount() +
            BISHOP_PAIR * ((board.getBishops(side).getPieceCount() == 2) ? 1 : 0)
        );
        // spotless:on
    }
}
