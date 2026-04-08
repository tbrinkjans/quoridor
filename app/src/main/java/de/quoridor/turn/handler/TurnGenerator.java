package de.quoridor.turn.handler;

import de.quoridor.game.Player;
import de.quoridor.turn.Turn;
import java.util.stream.Stream;

public interface TurnGenerator<T extends Turn> {
    Stream<T> generate(Player player);
}
