package ru.vsu.cs;

public class BoardFactory {
    public static Board create() {
        return new Board(BoardHelper.getBoardWithInitialPositionPieces());
    }
}
