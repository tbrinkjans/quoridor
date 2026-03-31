package de.quoridor.game;

import de.quoridor.common.Orientation;
import de.quoridor.common.Position;

public record Wall(Position position, Orientation orientation) {}
