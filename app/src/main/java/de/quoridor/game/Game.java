package de.quoridor.game;

import de.quoridor.exception.setup.SetupError;
import de.quoridor.exception.setup.SetupException;
import de.quoridor.exception.turn.TurnError;
import de.quoridor.exception.turn.TurnException;
import de.quoridor.turn.Turn;
import de.quoridor.turn.TurnAvailability;
import de.quoridor.turn.TurnGenerator;
import de.quoridor.turn.TurnHandler;
import de.quoridor.turn.move.MovePawnTurn;
import de.quoridor.turn.move.MovePawnTurnHandler;
import de.quoridor.turn.place.PlaceWallTurn;
import de.quoridor.turn.place.PlaceWallTurnHandler;
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

    private GameState state = GameState.SETUP;
    private Player winner;

    public static Game create(int playerCount) {
        if (playerCount < 2 || playerCount > 4) {
            throw new SetupException(SetupError.INVALID_PLAYER_COUNT);
        }
        return new Game(playerCount);
    }

    private Game(int playerCount) {
        board = new Board(playerCount);
        createPlayers(playerCount);
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

    public void start() {
        if (state != GameState.SETUP) {
            throw new SetupException(SetupError.GAME_ALREADY_STARTED);
        }

        registerTurnHandlers();
        state = GameState.RUNNING;
    }

    private void registerTurnHandlers() {
        PlaceWallTurnHandler placeWallTurnHandler = new PlaceWallTurnHandler();
        turnHandlers.put(PlaceWallTurn.class, placeWallTurnHandler);
        turnAvailabilitys.add(placeWallTurnHandler);

        MovePawnTurnHandler movePawnTurnHandler = new MovePawnTurnHandler();
        turnHandlers.put(MovePawnTurn.class, movePawnTurnHandler);
        turnGenerators.add(movePawnTurnHandler);
        turnAvailabilitys.add(movePawnTurnHandler);
    }

    @SuppressWarnings("unchecked")
    private TurnHandler<Turn> getTurnHandler(Turn turn) {
        return (TurnHandler<Turn>) turnHandlers.get(turn.getClass());
    }

    public void executeTurn(Turn turn) {
        TurnError error = validateTurn(turn);
        if (error != null) {
            throw new TurnException(error);
        }

        TurnHandler<Turn> handler = getTurnHandler(turn);
        handler.execute(board, turn);
        turnHistory.push(turn);

        Player player = turn.player();
        if (player.hasWon()) {
            finish(player);
            return;
        }

        nextPlayer();
    }

    private void finish(Player winner) {
        this.winner = winner;
        state = GameState.FINISHED;
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        Player nextPlayer = getCurrentPlayer();
        if (!hasValidTurns(nextPlayer)) {
            // If the next player has no valid turns, skip them
            nextPlayer();
        }
    }

    public TurnError validateTurn(Turn turn) {
        if (state != GameState.RUNNING) {
            return TurnError.INVALID_GAME_STATE;
        }

        if (turn.player() != getCurrentPlayer()) {
            return TurnError.NOT_CURRENT_PLAYER;
        }

        TurnHandler<Turn> handler = getTurnHandler(turn);
        return handler.validate(board, turn);
    }

    public Set<Turn> generateValidTurns(Player player) {
        if (state != GameState.RUNNING || player != getCurrentPlayer()) {
            return Set.of();
        }

        return turnGenerators.stream()
            .flatMap(generator -> generator.generate(player))
            .collect(Collectors.toSet());
    }

    public boolean hasValidTurns(Player player) {
        if (state != GameState.RUNNING || player != getCurrentPlayer()) {
            return false;
        }

        return turnAvailabilitys.stream()
            .anyMatch(availability -> availability.any(player));
    }

    public Stack<Turn> getTurnHistory() {
        return turnHistory;
    }

    public GameState getState() {
        return state;
    }

    public Player getWinner() {
        return winner;
    }
}
