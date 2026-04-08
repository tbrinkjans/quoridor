package de.quoridor.turn.handler;

import de.quoridor.exception.turn.TurnError;
import de.quoridor.game.Board;
import de.quoridor.turn.Turn;

public interface TurnHandler<T extends Turn> {
    void execute(Board board, T turn);
    TurnError validate(Board board, T turn);
}
