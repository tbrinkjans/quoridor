package de.quoridor.turn.handler;

import de.quoridor.exception.turn.TurnError;
import de.quoridor.game.Board;
import de.quoridor.turn.TurnHandler;
import de.quoridor.turn.operation.NoOperationTurn;

public class NoOperationTurnHandler implements TurnHandler<NoOperationTurn> {
    @Override
    public void execute(Board board, NoOperationTurn turn) {
        // No-op turn, do nothing
    }

    @Override
    public TurnError validate(Board board, NoOperationTurn turn) {
        // No-op turn is always valid
        return null;
    }
}
