package de.quoridor.turn.handler;

import de.quoridor.game.Player;
import de.quoridor.turn.Turn;

public interface TurnAvailability<T extends Turn> {
    boolean any(Player player);
}
