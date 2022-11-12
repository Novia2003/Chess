package ru.vsu.cs.models.pieces;

import ru.vsu.cs.BoardHelper;
import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;

public class Rook extends AbstractPiece { // ЛАДЬЯ
    public Rook(Cage cage, PieceColor color) {
        super(cage, color);
    }

    @Override
    public AbstractPiece movePiece(Cage newCage) {
        return new Rook(newCage, color);
    }

    @Override
    protected void updatePossibleSteps() {
        super.updatePossibleSteps();

        findFromLeftToRight(0, -1);
        findFromLeftToRight(1, 0);
        findFromRightToLeft(1, 0);
        findFromRightToLeft(0, -1);

        for (int i = 1; i < 12 - Math.abs(6 - cage.vertical()); i++) {
            if (i != cage.horizontal()) {
                possibleSteps.add(new Step(cage, new Cage(cage.vertical(), i)));
            }
        }
    }

    private void findFromLeftToRight(int firstIncrementH, int secondIncrementH) {
        int v = cage.vertical();
        int h = cage.horizontal();
        Cage probablyCage;

        if (v + 1 <= 6) {
            v++;
            h += firstIncrementH;
            probablyCage = new Cage(v, h);

            while (v <= 6 && BoardHelper.isCageBelongBoard(probablyCage)) {
                possibleSteps.add(new Step(cage, probablyCage));
                v += 1;
                h += firstIncrementH;
                probablyCage = new Cage(v, h);
            }

            v--;
            h -= firstIncrementH;

            if (v < 6) {
                return;
            }
        }

        v++;
        h += secondIncrementH;

        probablyCage = new Cage(v, h);

        while (BoardHelper.isCageBelongBoard(probablyCage)) {
            possibleSteps.add(new Step(cage, probablyCage));
            v++;
            h += secondIncrementH;
            probablyCage = new Cage(v, h);
        }
    }

    private void findFromRightToLeft(int firstIncrementH, int secondIncrementH) {
        int v = cage.vertical();
        int h = cage.horizontal();
        Cage probablyCage;

        if (v + -1 >= 6) {
            v--;
            h += firstIncrementH;
            probablyCage = new Cage(v, h);

            while (v >= 6 && BoardHelper.isCageBelongBoard(probablyCage)) {
                possibleSteps.add(new Step(cage, probablyCage));
                v += -1;
                h += firstIncrementH;
                probablyCage = new Cage(v, h);
            }

            v++;
            h -= firstIncrementH;

            if (v > 6) {
                return;
            }
        }

        v--;
        h += secondIncrementH;

        probablyCage = new Cage(v, h);

        while (BoardHelper.isCageBelongBoard(probablyCage)) {
            possibleSteps.add(new Step(cage, probablyCage));
            v--;
            h += secondIncrementH;
            probablyCage = new Cage(v, h);
        }
    }
}
