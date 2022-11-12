package ru.vsu.cs.models.pieces;

import ru.vsu.cs.BoardHelper;
import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;

import java.util.ArrayList;
import java.util.List;

public class Knight extends AbstractPiece { // КОНЬ
    public Knight(Cage cage, PieceColor color) {
        super(cage, color);
    }

    @Override
    public AbstractPiece movePiece(Cage newCage) {
        return new Knight(newCage, color);
    }

    @Override
    protected void updatePossibleSteps() {
        super.updatePossibleSteps();

        List<Cage> probablyCages = new ArrayList<>();
        int x = cage.vertical();
        int y = cage.horizontal();

        probablyCages.add(new Cage(x - 1, y + 2));
        probablyCages.add(new Cage(x + 1, y + 2));
        probablyCages.add(new Cage(x - 2, y + 1));
        probablyCages.add(new Cage(x + 2, y + 1));
        probablyCages.add(new Cage(x - 3, y - 1));
        probablyCages.add(new Cage(x + 3, y - 1));
        probablyCages.add(new Cage(x - 3, y - 2));
        probablyCages.add(new Cage(x + 3, y - 2));
        probablyCages.add(new Cage(x - 1, y - 3));
        probablyCages.add(new Cage(x + 1, y - 3));
        probablyCages.add(new Cage(x - 2, y - 3));
        probablyCages.add(new Cage(x + 2, y - 3));

        for (Cage probablyCage : probablyCages) {
            if (BoardHelper.isCageBelongBoard(probablyCage)) {
                possibleSteps.add(new Step(cage, probablyCage));
            }
        }
    }
}