package de.quoridor.game;

import de.quoridor.turn.Turn;
import de.quoridor.turn.TurnAvailability;
import de.quoridor.turn.TurnGenerator;
import de.quoridor.turn.TurnHandler;
import de.quoridor.turn.handler.MovePawnTurnHandler;
import de.quoridor.turn.handler.NoOperationTurnHandler;
import de.quoridor.turn.handler.PlaceWallTurnHandler;
import de.quoridor.turn.operation.MovePawnTurn;
import de.quoridor.turn.operation.NoOperationTurn;
import de.quoridor.turn.operation.PlaceWallTurn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Game {
    private final Board board;

    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex;

    private final Map<Class<? extends Turn>, TurnHandler<?>> turnHandlers = new HashMap<>();
    private final List<TurnGenerator<?>> turnGenerators = new ArrayList<>();
    private final List<TurnAvailability<?>> turnAvailabilitys = new ArrayList<>();

    private final Stack<Turn> turnHistory = new Stack<>();

    private boolean finished;
    private Player winner;

    public Game(int playerCount) {
        board = new Board(playerCount);
        createPlayers(playerCount);
        registerTurnHandlers();
    }

    public Board getBoard() {
        return board;
    }

    private void createPlayers(int playerCount) {
        List<Pawn> pawns = board.getPawns();
        int playerWallCount = Math.ceilDiv(20, playerCount);

        for (int i = 0; i < playerCount; i++) {
            Pawn pawn = pawns.get(i);
            int wallCount = playerWallCount;
            if (i == 1 && playerCount == 3) {
                // In a 3-player game, the 2nd player starts with one wall less
                wallCount--;
            }

            Player player = new Player(pawn, wallCount);
            players.add(player);
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void registerTurnHandlers() {
        MovePawnTurnHandler movePawnTurnHandler = new MovePawnTurnHandler();
        PlaceWallTurnHandler placeWallTurnHandler = new PlaceWallTurnHandler();
        NoOperationTurnHandler noOperationTurnHandler = new NoOperationTurnHandler();

        turnHandlers.put(MovePawnTurn.class, movePawnTurnHandler);
        turnHandlers.put(PlaceWallTurn.class, placeWallTurnHandler);
        turnHandlers.put(NoOperationTurn.class, noOperationTurnHandler);

        turnGenerators.add(movePawnTurnHandler);

        turnAvailabilitys.add(movePawnTurnHandler);
        turnAvailabilitys.add(placeWallTurnHandler);
    }

    @SuppressWarnings("unchecked")
    private TurnHandler<Turn> getTurnHandler(Turn turn) {
        return (TurnHandler<Turn>) turnHandlers.get(turn.getClass());
    }

    public void executeTurn(Turn turn) {
        TurnHandler<Turn> handler = getTurnHandler(turn);
        handler.execute(board, turn);
        turnHistory.push(turn);

        Player player = turn.player();
        if (player.hasWon()) {
            finished = true;
            winner = player;
            return;
        }

        nextPlayer();
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        Player nextPlayer = getCurrentPlayer();
        if (!hasValidTurns(nextPlayer)) {
            // If the next player has no valid turns, automatically execute a no-op turn for them
            executeTurn(new NoOperationTurn(nextPlayer));
        }
    }

    public boolean validateTurn(Turn turn) {
        if (finished || turn.player() != getCurrentPlayer()) {
            return false;
        }

        TurnHandler<Turn> handler = getTurnHandler(turn);
        return handler.validate(board, turn);
    }

    public Set<Turn> generateValidTurns(Player player) {
        return turnGenerators.stream()
            .flatMap(generator -> generator.generate(player))
            .collect(Collectors.toSet());
    }

    public boolean hasValidTurns(Player player) {
        return turnAvailabilitys.stream()
            .anyMatch(availability -> availability.any(player));
    }

    public Stack<Turn> getTurnHistory() {
        return turnHistory;
    }

    public boolean isFinished() {
        return finished;
    }

    public Player getWinner() {
        return winner;
    }
}
