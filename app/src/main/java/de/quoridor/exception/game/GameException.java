package de.quoridor.exception.game;

public class GameException extends RuntimeException {
    private final GameError error;

    public GameException(GameError error) {
        this.error = error;
    }

    public GameError getError() {
        return error;
    }
}
