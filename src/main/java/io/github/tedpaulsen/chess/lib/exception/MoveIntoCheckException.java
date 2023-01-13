package io.github.tedpaulsen.chess.lib.exception;

public class MoveIntoCheckException extends RuntimeException {

    public MoveIntoCheckException(String message) {
        super(message);
    }
}
