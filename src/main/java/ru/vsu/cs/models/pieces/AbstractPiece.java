package ru.vsu.cs.models.pieces;

import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPiece implements Piece {
    protected Cage cage;
    protected  List<Step> possibleSteps;
    protected final PieceColor color;

    public AbstractPiece(Cage cage, PieceColor color) {
        this.cage = cage;
        this.color = color;
        updatePossibleSteps();
    }

    @Override
    public List<Step> getPossibleSteps() {
        return possibleSteps;
    }

    public Cage getCage() {
        return cage;
    }

    public PieceColor getColor() {
        return color;
    }

    public abstract AbstractPiece movePiece(Cage newCage);
    protected void updatePossibleSteps() {
        possibleSteps = new ArrayList<>();
    }
}
