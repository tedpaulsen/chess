package io.github.tedpaulsen.chess.lib;

public class MoveIntoCheckException extends RuntimeException {

    public MoveIntoCheckException(String message) {
        super(message);
    }
}
