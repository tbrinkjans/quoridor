package de.quoridor.turn.operation;

import de.quoridor.game.Player;
import de.quoridor.game.Wall;
import de.quoridor.turn.Turn;

public record PlaceWallTurn(Player player, Wall wall) implements Turn {}
