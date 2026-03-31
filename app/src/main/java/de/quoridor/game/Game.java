package de.quoridor.game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;

    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex;

    private boolean finished;
    private Player winner;

    public Game(int playerCount) {
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

    public boolean isFinished() {
        return finished;
    }

    public Player getWinner() {
        return winner;
    }
}
