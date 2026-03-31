package de.quoridor.turn.operation;

import de.quoridor.game.Player;
import de.quoridor.turn.Turn;

public record NoOperationTurn(Player player) implements Turn {}
