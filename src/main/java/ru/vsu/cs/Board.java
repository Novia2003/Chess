package ru.vsu.cs;

import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;
import ru.vsu.cs.models.pieces.AbstractPiece;
import ru.vsu.cs.models.pieces.King;


public class Board {
    private AbstractPiece[][] board;
    private boolean isGameEnded;
    private PieceColor color;

    public Board(AbstractPiece[][] boardWithInitialPositionPieces) {
        this.board = boardWithInitialPositionPieces;
        this.isGameEnded = false;
        this.color = PieceColor.WHITE;
    }

    public AbstractPiece[][] getField() {
        AbstractPiece[][] copy = new AbstractPiece[11][];

        for (int i = 0; i < 11; i++) {
            int length = (i > 5) ? 11 - 2 * (i - 5) : 11;
            System.arraycopy(board[i], 0, copy[i], 0, length);
        }

        return copy;
    }

    public void executeCommand(String command) {
        if(isGameEnded) {
            throw new IllegalStateException("Конец партии");
        }

        final var step = Converter.convertStringToStep(command);
        movePiece(step);
    }

    private void movePiece(Step step) {
        AbstractPiece currentPiece = getPiece(step.startCage());

        if (currentPiece.getColor() != color) {
            throw new IllegalArgumentException("В начальной клетке хода находится фигура соперника");
        }

//        if (currentPiece instanceof Pawn && isCageBusy(step.endCage())) {
//            AbstractPiece probablyDiedInFuturePiece = getPiece(step.endCage());
//
//            if (probablyDiedInFuturePiece)
//        }

        if (isCageBusy(step.endCage())) {
            if (getPiece(step.endCage()).getColor() == currentPiece.getColor()) {
                throw new IllegalStateException("Конечная клетка занята фигурой того же цвета");
            }
        }

        if (currentPiece.getPossibleSteps().stream().anyMatch(anyStep ->
                isSameCage(anyStep.endCage(), step.endCage()))) {
            board[step.startCage().horizontal() - 1][findQuantityColumns(step.startCage())] = null;

            if (isCageBusy(step.endCage()) && getPiece(step.endCage()) instanceof King) {
                isGameEnded = true;
            }

            board[step.endCage().horizontal() - 1][findQuantityColumns(step.endCage())] =
                    currentPiece.movePiece(step.endCage());
        } else {
            throw new IllegalArgumentException("Фигура в начальной клетке так не ходит");
        }

        color = (color == PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;
    }

    private AbstractPiece getPiece(Cage cage) {
        if (!isCageBusy(cage)) {
            throw new IllegalArgumentException("Начальная клетка хода пуста");
        }

        return board[cage.horizontal() - 1][findQuantityColumns(cage)];
    }

    private boolean isCageBusy(Cage cage) {
        return board[cage.horizontal() - 1][findQuantityColumns(cage)] != null;
    }

    private boolean isSameCage(Cage firstCage, Cage secondCage) {
        return firstCage.horizontal() == secondCage.horizontal() && firstCage.vertical() == secondCage.vertical();
    }

    private int findQuantityColumns(Cage cage) {
       return  (cage.horizontal() < 7) ? cage.vertical() - 1 : cage.vertical() - (cage.horizontal() - 5);
    }
}
