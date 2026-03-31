package de.quoridor.exception.turn;

public enum TurnError {
    NOT_CURRENT_PLAYER,
    INVALID_MOVEMENT,
    NO_WALLS_REMAINING,
    INVALID_PLACEMENT,
    GAME_ALREADY_FINISHED
}
