package de.quoridor.turn.handler;

import de.quoridor.exception.turn.TurnError;
import de.quoridor.game.Board;
import de.quoridor.game.Field;
import de.quoridor.game.Pawn;
import de.quoridor.game.Player;
import de.quoridor.turn.MovePawnTurn;
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
        if (!validateMovement(pawn, destination)) {
            return TurnError.INVALID_MOVEMENT;
        }
        return null;
    }

    private boolean validateMovement(Pawn pawn, Field destination) {
        if (destination == null) {
            return false;
        }

        return generateMovements(pawn.getField())
            .anyMatch(d -> d == destination);
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

        List<Field> jumpableFields = neighbor.getNeighbors().stream()
            .filter(field -> !field.hasPawn())
            .toList();

        Optional<Field> straightJumpableField = jumpableFields.stream()
            .filter(field -> field.getPosition().isAlignedTo(current.getPosition()))
            .findFirst();

        return straightJumpableField.isPresent()
            ? straightJumpableField.stream()
            : jumpableFields.stream();
    }

    @Override
    public boolean any(Player player) {
        return generate(player).findAny().isPresent();
    }
}
