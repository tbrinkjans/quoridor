package de.quoridor.game;

public class Player {
    private final Pawn pawn;
    private int wallCount;

    protected Player(Pawn pawn, int wallCount) {
        this.pawn = pawn;
        this.wallCount = wallCount;
    }

    public boolean hasWon() {
        return pawn.hasReachedFinish();
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void reduceWallCount() {
        wallCount--;
    }

    public boolean hasWallsRemaining() {
        return wallCount > 0;
    }

    public int getWallCount() {
        return wallCount;
    }
}
