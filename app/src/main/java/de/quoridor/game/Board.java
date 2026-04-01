package de.quoridor.game;

import de.quoridor.common.Orientation;
import de.quoridor.common.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final Field[][] fields = new Field[9][9];
    private final List<Pawn> pawns = new ArrayList<>();
    private final Set<Wall> walls = new HashSet<>();

    protected Board(int pawnCount) {
        createFields();
        createPawns(pawnCount);
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

    public Field[][] getFields() {
        return fields;
    }

    private void createPawns(int pawnCount) {
        Field[] pawnStartingFields = {
            fields[4][0],
            fields[0][4],
            fields[4][8],
            fields[8][4]
        };

        Field[][] pawnFinishFields = {
            Arrays.stream(fields).map(field -> field[8]).toArray(Field[]::new),
            fields[8],
            Arrays.stream(fields).map(field -> field[0]).toArray(Field[]::new),
            fields[0]
        };

        for (int i = 0; i < pawnCount; i++) {
            if (i == 1 && pawnCount == 2) {
                // In a 2-player game, the 2nd player starts on the opposite side of the board
                i++;
            }

            Field startingField = pawnStartingFields[i];
            Set<Field> finishFields = Set.of(pawnFinishFields[i]);

            Pawn pawn = new Pawn(startingField, finishFields);
            startingField.setPawn(pawn);

            pawns.add(pawn);
        }
    }

    public void movePawn(Pawn pawn, Field destination) {
        Field current = pawn.getField();
        current.setPawn(null);
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

    public void removeWall(Wall wall) {
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
