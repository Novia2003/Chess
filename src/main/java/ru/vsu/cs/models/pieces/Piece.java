package ru.vsu.cs.models.pieces;

import ru.vsu.cs.models.Step;

import java.util.List;

public interface Piece {
    List<Step> getPossibleSteps();

}
