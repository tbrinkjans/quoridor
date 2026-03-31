package de.quoridor.exception.turn;

public class TurnException extends RuntimeException {
    private final TurnError error;

    public TurnException(TurnError error) {
        this.error = error;
    }

    public TurnError getError() {
        return error;
    }
}
