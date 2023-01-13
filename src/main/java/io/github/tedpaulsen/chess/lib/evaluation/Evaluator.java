package io.github.tedpaulsen.chess.lib.evaluation;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Side;

public abstract class Evaluator {

    public abstract double evaluate(Side side, BoardRepresentation board);
}
