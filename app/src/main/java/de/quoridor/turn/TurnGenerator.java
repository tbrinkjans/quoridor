package de.quoridor.turn;

import de.quoridor.game.Player;
import java.util.stream.Stream;

public interface TurnGenerator<T extends Turn> {
    Stream<T> generate(Player player);
}
