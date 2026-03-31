package de.quoridor.turn.handler;

import de.quoridor.exception.turn.TurnError;
import de.quoridor.game.Board;
import de.quoridor.game.Field;
import de.quoridor.game.Pawn;
import de.quoridor.game.Player;
import de.quoridor.turn.TurnAvailability;
import de.quoridor.turn.TurnGenerator;
import de.quoridor.turn.TurnHandler;
import de.quoridor.turn.operation.MovePawnTurn;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MovePawnTurnHandler
    implements TurnHandler<MovePawnTurn>, TurnGenerator<MovePawnTurn>, TurnAvailability<MovePawnTurn>
{
    @Override
    public void execute(Board board, MovePawnTurn turn) {
        Pawn pawn = turn.player().getPawn();
        Field destination = turn.destination();
        board.movePawn(pawn, destination);
    }

    @Override
    public TurnError validate(Board board, MovePawnTurn turn) {
        Pawn pawn = turn.player().getPawn();
        Field destination = turn.destination();
        if (!validateMovement(board, pawn, destination)) {
            return TurnError.INVALID_MOVEMENT;
        }
        return null;
    }

    private boolean validateMovement(Board board, Pawn pawn, Field destination) {
        Field current = pawn.getField();
        return generateMovements(current).anyMatch(d -> d.equals(destination));
    }

    @Override
    public Stream<MovePawnTurn> generate(Player player) {
        Pawn pawn = player.getPawn();
        Field current = pawn.getField();
        return generateMovements(current)
            .map(destination -> new MovePawnTurn(player, destination));
    }

    private Stream<Field> generateMovements(Field current) {
        return current.getNeighbors().stream()
            .flatMap(neighbor -> generateMovementsAtNeighbor(current, neighbor))
            .distinct();
    }

    private Stream<Field> generateMovementsAtNeighbor(Field current, Field neighbor) {
        if (!neighbor.hasPawn()) {
            return Stream.of(neighbor);
        }

        List<Field> emptyNeighborFields = neighbor.getNeighbors().stream()
            .filter(field -> !field.hasPawn())
            .toList();

        Optional<Field> fieldBehindNeighbor = emptyNeighborFields.stream()
            .filter(field -> field.getPosition().isAlignedTo(current.getPosition()))
            .findFirst();

        return fieldBehindNeighbor.isPresent()
            ? fieldBehindNeighbor.stream()
            : emptyNeighborFields.stream();
    }

    @Override
    public boolean any(Player player) {
        return generate(player).findAny().isPresent();
    }
}
