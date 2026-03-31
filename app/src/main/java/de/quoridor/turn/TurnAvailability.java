package de.quoridor.turn;

import de.quoridor.game.Player;

public interface TurnAvailability<T extends Turn> {
    boolean any(Player player);
}
