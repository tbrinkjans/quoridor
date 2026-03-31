package de.quoridor.game;

import de.quoridor.common.Position;
import java.util.HashSet;
import java.util.Set;

public class Field {
    private final Position position;
    private final Set<Field> neighbors = new HashSet<>();
    private Pawn pawn;

    protected Field(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    protected void addNeighbor(Field field) {
        neighbors.add(field);
    }

    protected void removeNeighbor(Field field) {
        neighbors.remove(field);
    }

    public Set<Field> getNeighbors() {
        return neighbors;
    }

    protected void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public boolean hasPawn() {
        return pawn != null;
    }

    public Pawn getPawn() {
        return pawn;
    }
}
