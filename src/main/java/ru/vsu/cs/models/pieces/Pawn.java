package ru.vsu.cs.models.pieces;

import ru.vsu.cs.BoardHelper;
import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;

public class Pawn extends AbstractPiece { // ПЕШКА
    public Pawn(Cage cage, PieceColor color) {
        super(cage, color);
    }

    @Override
    public AbstractPiece movePiece(Cage newCage) {
        final boolean isNewWhiteQueen = cage.horizontal() == 11 - Math.abs(6 - cage.vertical()) &&
                PieceColor.WHITE == color;
        final boolean isNewBlackQueen = cage.horizontal() == 1 && PieceColor.BLACK == color;

        if (isNewBlackQueen || isNewWhiteQueen) {
            return new Queen(cage, color);
        }

        return new Pawn(newCage, color);
    }

    @Override
    protected void updatePossibleSteps() {
        super.updatePossibleSteps();

        int moving = (color == PieceColor.BLACK) ? -1 : 1;

        Cage probablyCage = new Cage(cage.vertical(), cage.horizontal() + moving);

        if (BoardHelper.isCageBelongBoard(probablyCage)) {
            possibleSteps.add(new Step(cage, probablyCage));
        }

        if (PieceColor.BLACK == color && cage.horizontal() == 7) {
            possibleSteps.add(new Step(cage, new Cage(cage.vertical(), cage.horizontal() - 2)));
        }

        if (PieceColor.WHITE == color && ((cage.vertical() <= 6 && cage.vertical() - cage.horizontal() == 1) ||
                (cage.vertical() > 6 && cage.vertical() + cage.horizontal() == 11))) {
            possibleSteps.add(new Step(cage, new Cage(cage.vertical(), cage.horizontal() + 2)));
        }
    }
}
