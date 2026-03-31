package de.quoridor.turn;

import de.quoridor.exception.turn.TurnError;
import de.quoridor.game.Board;

public interface TurnHandler<T extends Turn> {
    void execute(Board board, T turn);
    TurnError validate(Board board, T turn);
}
