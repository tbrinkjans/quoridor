package de.quoridor.exception.setup;

public class GameSetupException extends RuntimeException {
    private final GameSetupError error;

    public GameSetupException(GameSetupError error) {
        this.error = error;
    }

    public GameSetupError getError() {
        return error;
    }
}
