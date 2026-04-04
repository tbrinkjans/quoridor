package de.quoridor.game;

public class Player {
    private final Pawn pawn;
    private int wallCount;

    protected Player(Pawn pawn) {
        this.pawn = pawn;
    }

    public boolean hasWon() {
        return pawn.hasReachedFinish();
    }

    public Pawn getPawn() {
        return pawn;
    }

    protected void setWallCount(int wallCount) {
        this.wallCount = wallCount;
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
