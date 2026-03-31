package de.quoridor.exception.setup;

public class SetupException extends RuntimeException {
    private final SetupError error;

    public SetupException(SetupError error) {
        this.error = error;
    }

    public SetupError getError() {
        return error;
    }
}
