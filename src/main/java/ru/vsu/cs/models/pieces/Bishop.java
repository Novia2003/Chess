package ru.vsu.cs.models.pieces;

import ru.vsu.cs.BoardHelper;
import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;

public class Bishop extends AbstractPiece { // СЛОН
    public Bishop(Cage cage, PieceColor color) {
        super(cage, color);
    }

    @Override
    public AbstractPiece movePiece(Cage newCage) {
        return new Bishop(newCage, color);
    }

    @Override
    protected void updatePossibleSteps() {
        super.updatePossibleSteps();

        findFromLeftToRight(1, -1,-2);
        findFromLeftToRight(2, 1, -1);
        findFromLeftToRight(1, 2, 1);
        findFromRightToLeft(-1, -1, -2);
        findFromRightToLeft(-2, 1, -1);
        findFromRightToLeft(-1, 2, 1);
    }

    private void findFromLeftToRight(int incrementV, int firstIncrementH, int secondIncrementH) {
        int v = cage.vertical();
        int h = cage.horizontal();
        Cage probablyCage;

        if (v + incrementV <= 6) {
            v += incrementV;
            h += firstIncrementH;
            probablyCage = new Cage(v, h);

            while (v <= 6 && BoardHelper.isCageBelongBoard(probablyCage)) {
                possibleSteps.add(new Step(cage, probablyCage));
                v += incrementV;
                h += firstIncrementH;
                probablyCage = new Cage(v, h);
            }

            v -= incrementV;
            h -= firstIncrementH;

            if (v < 6 && incrementV == 1) {
                return;
            }
        }

        if (v == 5) {
            v = 7;
        } else {
            v += incrementV;
            h += secondIncrementH;
        }

        probablyCage = new Cage(v, h);

        while (BoardHelper.isCageBelongBoard(probablyCage)) {
            possibleSteps.add(new Step(cage, probablyCage));
            v += incrementV;
            h += secondIncrementH;
            probablyCage = new Cage(v, h);
        }
    }

    private void findFromRightToLeft(int incrementV, int firstIncrementH, int secondIncrementH) {
        int v = cage.vertical();
        int h = cage.horizontal();
        Cage probablyCage;

        if (v + incrementV >= 6) {
            v += incrementV;
            h += firstIncrementH;
            probablyCage = new Cage(v, h);

            while (v >= 6 && BoardHelper.isCageBelongBoard(probablyCage)) {
                possibleSteps.add(new Step(cage, probablyCage));
                v += incrementV;
                h += firstIncrementH;
                probablyCage = new Cage(v, h);
            }

            v -= incrementV;
            h -= firstIncrementH;

            if (v > 6 && incrementV == -1) {
                return;
            }
        }

        if (v == 7) {
            v = 5;
        } else {
            v += incrementV;
            h += secondIncrementH;
        }

        probablyCage = new Cage(v, h);

        while (BoardHelper.isCageBelongBoard(probablyCage)) {
            possibleSteps.add(new Step(cage, probablyCage));
            v += incrementV;
            h += secondIncrementH;
            probablyCage = new Cage(v, h);
        }
    }
}
