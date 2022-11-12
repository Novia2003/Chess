package ru.vsu.cs.models.pieces;

import ru.vsu.cs.BoardHelper;
import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;

import java.util.ArrayList;
import java.util.List;


public class King extends AbstractPiece{
    public King(Cage cage, PieceColor color) {
        super(cage, color);
    }

    @Override
    public AbstractPiece movePiece(Cage newCage) {
        return new King(newCage, color);
    }

    @Override
    protected void updatePossibleSteps() {
        super.updatePossibleSteps();

        List<Cage> probablyCages = new ArrayList<>();
        int x = cage.vertical();
        int y = cage.horizontal();

        probablyCages.add(new Cage(x, y + 1));
        probablyCages.add(new Cage(x - 1, y + 1));
        probablyCages.add(new Cage(x + 1, y + 1));
        probablyCages.add(new Cage(x - 1, y));
        probablyCages.add(new Cage(x + 1, y));
        probablyCages.add(new Cage(x, y - 1));
        probablyCages.add(new Cage(x - 1, y - 1));
        probablyCages.add(new Cage(x - 2, y - 1));
        probablyCages.add(new Cage(x + 1, y - 1));
        probablyCages.add(new Cage(x + 2, y - 1));
        probablyCages.add(new Cage(x - 1, y - 2));
        probablyCages.add(new Cage(x + 1, y - 2));

        for (Cage probablyCage : probablyCages) {
            if (BoardHelper.isCageBelongBoard(probablyCage)) {
                possibleSteps.add(new Step(cage, probablyCage));
            }
        }
    }
}