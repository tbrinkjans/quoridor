package de.quoridor.game;

import java.util.Set;

public class Pawn {
    private Field field;
    private Set<Field> finishFields;

    protected Pawn() {}

    protected void setField(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    protected void setFinishFields(Set<Field> finishFields) {
        this.finishFields = finishFields;
    }

    public boolean hasReachedFinish() {
        return finishFields.contains(field);
    }

    public Set<Field> getFinishFields() {
        return finishFields;
    }
}
