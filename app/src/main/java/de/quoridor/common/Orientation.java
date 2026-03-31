package de.quoridor.common;

public enum Orientation {
    HORIZONTAL,
    VERTICAL;

    public Orientation opposite() {
        return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
    }
}
