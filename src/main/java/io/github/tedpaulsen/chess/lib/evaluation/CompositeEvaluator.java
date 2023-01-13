package io.github.tedpaulsen.chess.lib.evaluation;

import io.github.tedpaulsen.chess.lib.BoardRepresentation;
import io.github.tedpaulsen.chess.lib.Side;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CompositeEvaluator extends Evaluator {

    List<Evaluator> evaluators;

    public static CompositeEvaluator of(Evaluator... evaluators) {
        return new CompositeEvaluator(List.of(evaluators));
    }

    @Override
    public double evaluate(final Side side, final BoardRepresentation board) {
        return evaluators.stream().map(e -> e.evaluate(side, board)).reduce(0.0d, Double::sum);
    }
}
