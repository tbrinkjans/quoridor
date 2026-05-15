package de.quoridor.common;

public record Position(int x, int y) {
    public boolean isAlignedTo(Position other) {
        return x == other.x || y == other.y;
    }

    public boolean isAlignedTo(Position other, Orientation orientation) {
        return orientation == Orientation.HORIZONTAL
            ? y == other.y
            : x == other.x;
    }

    public boolean isAdjacentTo(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y) == 1;
    }

    public boolean isAdjacentTo(Position other, Orientation orientation) {
        return orientation == Orientation.HORIZONTAL
            ? (Math.abs(x - other.x) == 1 && Math.abs(y - other.y) == 0)
            : (Math.abs(x - other.x) == 0 && Math.abs(y - other.y) == 1);
    }
}
