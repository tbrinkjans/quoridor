package de.quoridor.common;

public record Position(int x, int y) {
    public boolean isAlignedTo(Position other) {
        return x == other.x || y == other.y;
    }

    public boolean isAdjacentTo(Position other) {
        int dx = Math.abs(x - other.x);
        int dy = Math.abs(y - other.y);

        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }

    public boolean isAdjacentTo(Position other, Orientation orientation) {
        int dx = Math.abs(x - other.x);
        int dy = Math.abs(y - other.y);

        return orientation == Orientation.HORIZONTAL
            ? (dx == 1 && dy == 0)
            : (dx == 0 && dy == 1);
    }
}
