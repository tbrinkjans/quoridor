package de.quoridor.turn;

import de.quoridor.game.Field;
import de.quoridor.game.Player;

public record MovePawnTurn(Player player, Field destination) implements Turn {}
