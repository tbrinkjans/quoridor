package de.quoridor.turn.handler;

import de.quoridor.common.Orientation;
import de.quoridor.common.Position;
import de.quoridor.exception.turn.TurnError;
import de.quoridor.game.Board;
import de.quoridor.game.Player;
import de.quoridor.game.Wall;
import de.quoridor.turn.TurnAvailability;
import de.quoridor.turn.TurnHandler;
import de.quoridor.turn.operation.PlaceWallTurn;
import java.util.Set;

public class PlaceWallTurnHandler implements TurnHandler<PlaceWallTurn>, TurnAvailability<PlaceWallTurn> {
    @Override
    public void execute(Board board, PlaceWallTurn turn) {
        Wall wall = turn.wall();
        board.placeWall(wall);

        Player player = turn.player();
        player.reduceWallCount();
    }

    @Override
    public TurnError validate(Board board, PlaceWallTurn turn) {
        Player player = turn.player();
        if (!player.hasWallsRemaining()) {
            return TurnError.NO_WALLS_REMAINING;
        }

        Wall wall = turn.wall();
        if (!validatePlacement(board, wall)) {
            return TurnError.INVALID_PLACEMENT;
        }

        return null;
    }

    private boolean validatePlacement(Board board, Wall wall) {
        Position position = wall.position();
        if (position.x() < 0 || position.x() >= 7 || position.y() < 0 || position.y() >= 7) {
            return false;
        }

        Set<Wall> walls = board.getWalls();
        boolean occupied = walls.stream().anyMatch(w -> w.position().equals(position));
        if (occupied) {
            return false;
        }

        Orientation orientation = wall.orientation();
        return walls.stream()
            .filter(w -> w.orientation() == orientation)
            .noneMatch(w -> w.position().isAdjacentTo(position, orientation));
    }

    @Override
    public boolean any(Player player) {
        return player.hasWallsRemaining();
    }
}
