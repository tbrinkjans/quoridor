package de.quoridor.turn;

import de.quoridor.game.Player;

public sealed interface Turn permits MovePawnTurn, PlaceWallTurn {
    Player player();
}
