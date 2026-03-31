package de.quoridor.turn;

import de.quoridor.game.Board;

public interface TurnHandler<T extends Turn> {
    void execute(Board board, T turn);
    boolean validate(Board board, T turn);
}
