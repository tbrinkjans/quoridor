package de.quoridor.turn.operation;

import de.quoridor.game.Field;
import de.quoridor.game.Player;
import de.quoridor.turn.Turn;

public record MovePawnTurn(Player player, Field destination) implements Turn {}
