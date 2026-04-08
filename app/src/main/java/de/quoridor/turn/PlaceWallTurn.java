package de.quoridor.turn;

import de.quoridor.game.Player;
import de.quoridor.game.Wall;

public record PlaceWallTurn(Player player, Wall wall) implements Turn {}
