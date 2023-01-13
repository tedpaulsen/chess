package io.github.tedpaulsen.chess.lib.exception;

public class StalemateException extends RuntimeException {

    public StalemateException(String message) {
        super(message);
    }
}
