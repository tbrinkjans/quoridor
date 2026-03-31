package de.quoridor.game;

import java.util.Set;

public class Pawn {
    private Field field;
    private final Set<Field> finishFields;

    protected Pawn(Field startingField, Set<Field> finishFields) {
        field = startingField;
        this.finishFields = finishFields;
    }

    protected void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public boolean hasReachedFinish() {
        return finishFields.contains(field);
    }

    public Set<Field> getFinishFields() {
        return finishFields;
    }
}
