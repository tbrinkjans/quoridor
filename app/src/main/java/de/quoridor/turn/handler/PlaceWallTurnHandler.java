package de.quoridor.turn.handler;

import de.quoridor.common.Position;
import de.quoridor.exception.turn.TurnError;
import de.quoridor.game.Board;
import de.quoridor.game.Field;
import de.quoridor.game.Pawn;
import de.quoridor.game.Player;
import de.quoridor.game.Wall;
import de.quoridor.turn.TurnAvailability;
import de.quoridor.turn.TurnHandler;
import de.quoridor.turn.operation.PlaceWallTurn;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
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
        return !positionInvalid(wall.position())
            && !positionOccupied(board.getWalls(), wall.position())
            && !wallOverlaps(board.getWalls(), wall)
            && !wallBlocksPath(board, wall);
    }

    private boolean positionInvalid(Position position) {
        return position.x() < 0 || position.x() > 7 || position.y() < 0 || position.y() > 7;
    }

    private boolean positionOccupied(Set<Wall> walls, Position position) {
        return walls.stream()
            .anyMatch(w -> w.position().equals(position));
    }

    private boolean wallOverlaps(Set<Wall> walls, Wall wall) {
        return walls.stream()
            .filter(w -> w.orientation() == wall.orientation())
            .anyMatch(w -> w.position().isAdjacentTo(wall.position(), wall.orientation()));
    }

    private boolean wallBlocksPath(Board board, Wall wall) {
        board.placeWall(wall);

        List<Pawn> pawns = board.getPawns();
        boolean pathBlocked = pawns.stream()
            .anyMatch(pawn -> !pathExists(pawn));

        board.removeWall(wall);

        return pathBlocked;
    }

    private boolean pathExists(Pawn pawn) {
        Set<Field> visited = new HashSet<>();
        Queue<Field> queue = new ArrayDeque<>();

        Field start = pawn.getField();
        queue.add(start);
        visited.add(start);

        Set<Field> finishFields = pawn.getFinishFields();
        while (!queue.isEmpty()) {
            Field current = queue.poll();

            if (finishFields.contains(current)) {
                return true;
            }

            for (Field neighbor : current.getNeighbors()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return false;
    }

    @Override
    public boolean any(Player player) {
        return player.hasWallsRemaining();
    }
}
