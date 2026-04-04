package de.quoridor.game;

import de.quoridor.common.Orientation;
import de.quoridor.common.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Board {
    private final Field[][] fields = new Field[9][9];
    private final List<Field> startingFields = new ArrayList<>();
    private final List<Set<Field>> finishFields = new ArrayList<>();

    private final List<Pawn> pawns = new ArrayList<>();

    private final Set<Wall> walls = new HashSet<>();

    protected Board() {
        createFields();
        initPawnFields();
    }

    private void createFields() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Position position = new Position(x, y);
                fields[x][y] = new Field(position);
            }
        }

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Field field = fields[x][y];
                if (y < 8) field.addNeighbor(fields[x][y + 1]);
                if (x < 8) field.addNeighbor(fields[x + 1][y]);
                if (y > 0) field.addNeighbor(fields[x][y - 1]);
                if (x > 0) field.addNeighbor(fields[x - 1][y]);
            }
        }
    }

    private void initPawnFields() {
        startingFields.add(fields[4][0]);
        startingFields.add(fields[0][4]);
        startingFields.add(fields[4][8]);
        startingFields.add(fields[8][4]);

        finishFields.add(Arrays.stream(fields).map(field -> field[8]).collect(Collectors.toSet()));
        finishFields.add(Set.of(fields[8]));
        finishFields.add(Arrays.stream(fields).map(field -> field[0]).collect(Collectors.toSet()));
        finishFields.add(Set.of(fields[0]));
    }

    public Field[][] getFields() {
        return fields;
    }

    protected Pawn addPawn() {
        int i = pawns.size();
        if (i == 1) {
            i++;
        } else if (i == 2) {
            Pawn last = pawns.get(1);
            placePawn(last, 1);
        }

        Pawn pawn = new Pawn();
        placePawn(pawn, i);
        pawns.add(pawn);

        return pawn;
    }

    protected void removePawn(Pawn pawn) {
        Field field = pawn.getField();
        field.setPawn(null);

        int i = pawns.indexOf(pawn);
        pawns.remove(pawn);

        int count = pawns.size();
        for (int j = i; j < count; j++) {
            Pawn current = pawns.get(j);
            placePawn(current, j);
        }

        if (count == 2) {
            Pawn last = pawns.get(1);
            placePawn(last, 2);
        }
    }

    private void placePawn(Pawn pawn, int i) {
        Field startingField = startingFields.get(i);
        movePawn(pawn, startingField);

        Set<Field> finishFields = this.finishFields.get(i);
        pawn.setFinishFields(finishFields);
    }

    public void movePawn(Pawn pawn, Field destination) {
        Field current = pawn.getField();
        if (current != null) {
            current.setPawn(null);
        }
        destination.setPawn(pawn);
        pawn.setField(destination);
    }

    public List<Pawn> getPawns() {
        return pawns;
    }

    public void placeWall(Wall wall) {
        List<Field[]> splitFields = getSplitFields(wall);
        for (Field[] pair : splitFields) {
            pair[0].removeNeighbor(pair[1]);
            pair[1].removeNeighbor(pair[0]);
        }
        walls.add(wall);
    }

    public void revertWall(Wall wall) {
        List<Field[]> splitFields = getSplitFields(wall);
        for (Field[] pair : splitFields) {
            pair[0].addNeighbor(pair[1]);
            pair[1].addNeighbor(pair[0]);
        }
        walls.remove(wall);
    }

    private List<Field[]> getSplitFields(Wall wall) {
        Position position = wall.position();
        Orientation orientation = wall.orientation();

        Field bottomLeft = fields[position.x()][position.y()];
        Field bottomRight = fields[position.x() + 1][position.y()];
        Field topRight = fields[position.x() + 1][position.y() + 1];
        Field topLeft = fields[position.x()][position.y() + 1];

        List<Field[]> splitFields = new ArrayList<>();
        if (orientation == Orientation.HORIZONTAL) {
            splitFields.add(new Field[] { bottomLeft, topLeft });
            splitFields.add(new Field[] { bottomRight, topRight });
        } else {
            splitFields.add(new Field[] { bottomLeft, bottomRight });
            splitFields.add(new Field[] { topLeft, topRight });
        }

        return splitFields;
    }

    public Set<Wall> getWalls() {
        return walls;
    }
}
