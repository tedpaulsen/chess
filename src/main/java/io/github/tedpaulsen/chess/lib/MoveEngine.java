package io.github.tedpaulsen.chess.lib;

import io.github.tedpaulsen.chess.lib.evaluation.Evaluator;
import io.github.tedpaulsen.chess.lib.movegen.BishopMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.KingMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.KnightMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.PawnMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.QueenMoveGen;
import io.github.tedpaulsen.chess.lib.movegen.RookMoveGen;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MoveEngine {

    private final PawnMoveGen pawnMoveGen;
    private final KnightMoveGen knightMoveGen;
    private final BishopMoveGen bishopMoveGen;
    private final RookMoveGen rookMoveGen;
    private final QueenMoveGen queenMoveGen;
    private final KingMoveGen kingMoveGen;

    private final Evaluator evaluator;

    public BoardRepresentation move(Side side, BoardRepresentation board) {
        // I'm using map.entry as queue elements so that I can compute the priority statically before inserting
        // the element. The standard priority queue impl relies only on the elements being comparable and I want
        // to avoid re-evaluating log(n) moves every move when having to insert a new move to my priority queue.
        PriorityQueue<Map.Entry<Move, Double>> orderedMoves = new PriorityQueue<>(Map.Entry.comparingByValue());
        for (Move moveToEval : generateAllPseudoLegalMoves(side, board)) {
            try {
                BoardRepresentation newBoard = makeMove(board, moveToEval);
                orderedMoves.add(Map.entry(moveToEval, evaluator.evaluate(side, newBoard)));
            } catch (MoveIntoCheckException e) {
                log.info("Refusing move into check");
            }
        }

        Move bestMove = orderedMoves.poll().getKey();
        return makeMove(board, bestMove);
    }

    private List<Move> generateAllPseudoLegalMoves(Side side, BoardRepresentation board) {
        List<Move> allMoves = new ArrayList<>();
        allMoves.addAll(pawnMoveGen.generateMoves(side, board));
        allMoves.addAll(knightMoveGen.generateMoves(side, board));
        allMoves.addAll(bishopMoveGen.generateMoves(side, board));
        allMoves.addAll(rookMoveGen.generateMoves(side, board));
        allMoves.addAll(queenMoveGen.generateMoves(side, board));
        allMoves.addAll(kingMoveGen.generateMoves(side, board));
        return allMoves;
    }

    private BoardRepresentation makeMove(BoardRepresentation board, Move move) {
        BitBoard updatedPieces = board.get(move.getPiece()).move(move.getFrom(), move.getTo());
        BoardRepresentation updatedBoard = board.set(move.getPiece(), updatedPieces);

        if (isInCheck(move.getSide(), updatedBoard)) {
            throw new MoveIntoCheckException("move into check");
        }

        return updatedBoard;
    }

    public boolean isInCheck(Side side, BoardRepresentation board) {
        BitBoard kingInQuestion = side.is(Side.WHITE) ? board.getWhiteKing() : board.getBlackKing();
        Side attackingSide = side.is(Side.WHITE) ? Side.BLACK : Side.WHITE;

        BitBoard allAttackedSquares = pawnMoveGen
            .getSquaresAttacked(attackingSide, board)
            .union(knightMoveGen.getSquaresAttacked(attackingSide, board))
            .union(bishopMoveGen.getSquaresAttacked(attackingSide, board))
            .union(rookMoveGen.getSquaresAttacked(attackingSide, board))
            .union(queenMoveGen.getSquaresAttacked(attackingSide, board))
            .union(kingMoveGen.getSquaresAttacked(attackingSide, board));

        return !allAttackedSquares.intersect(kingInQuestion).isEmpty();
    }
}
