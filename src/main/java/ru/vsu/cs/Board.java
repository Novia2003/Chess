package ru.vsu.cs;

import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.Step;
import ru.vsu.cs.models.pieces.AbstractPiece;
import ru.vsu.cs.models.pieces.King;
import ru.vsu.cs.models.pieces.Pawn;


public class Board {
    private AbstractPiece[][] board;
    private boolean isGameEnded;
    private PieceColor color;

    private boolean isMagicMovePossible;
    private Cage cageThatWasJumpedOver;

    public Board(AbstractPiece[][] boardWithInitialPositionPieces) {
        this.board = boardWithInitialPositionPieces;
        this.isGameEnded = false;
        this.color = PieceColor.WHITE;
    }

    public String[][] getField() {
        return BoardHelper.getBoardForConsole(board);
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public PieceColor getColor() {
        return color;
    }

    public void executeCommand(String command) {
        Step step = Converter.convertStringToStep(command);
        movePiece(step);
    }

    private void movePiece(Step step) {
        AbstractPiece currentPiece = getPiece(step.startCage());

        if (currentPiece.getColor() != color) {
            throw new IllegalArgumentException("В начальной клетке хода находится фигура соперника");
        }

        if (isCageBusy(step.endCage())) {
            if (getPiece(step.endCage()).getColor() == currentPiece.getColor()) {
                throw new IllegalArgumentException("Конечная клетка занята фигурой того же цвета");
            }
        }

        if (currentPiece instanceof Pawn) {
            makePawnMove(currentPiece, step);
        } else {
            if (isMagicMovePossible) {
                isMagicMovePossible = false;
                cageThatWasJumpedOver = null;
            }

            if (checkStepForPossible(step, currentPiece)) {
                performPermutationInBoard(step, currentPiece);
            } else {
                throw new IllegalArgumentException("Фигура в начальной клетке так не ходит");
            }
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


    private int findQuantityColumns(Cage cage) {
        return (cage.horizontal() < 7) ? cage.vertical() - 1 : cage.vertical() - (cage.horizontal() - 5);
    }

    private boolean checkStepForPossible(Step step, AbstractPiece piece) {
        return piece.getPossibleSteps().stream().anyMatch(anyStep -> isSameCage(anyStep.endCage(), step.endCage()));
    }

    private boolean isSameCage(Cage firstCage, Cage secondCage) {
        return firstCage.horizontal() == secondCage.horizontal() && firstCage.vertical() == secondCage.vertical();
    }

    private void performPermutationInBoard(Step step, AbstractPiece piece) {
        board[step.startCage().horizontal() - 1][findQuantityColumns(step.startCage())] = null;

        if (isCageBusy(step.endCage()) && getPiece(step.endCage()) instanceof King) {
            isGameEnded = true;
        }

        board[step.endCage().horizontal() - 1][findQuantityColumns(step.endCage())] =
                piece.movePiece(step.endCage());
    }

    private void makePawnMove(AbstractPiece currentPiece, Step step) {
        if (isCageBusy(step.endCage()) && getPiece(step.endCage()).getColor() != currentPiece.getColor() &&
                checkStepForPossible(step, currentPiece)) {
            throw new IllegalArgumentException("Пешка так бить не может");
        }

        if (isCageBusy(step.endCage()) && getPiece(step.endCage()).getColor() != color &&
                checkForAttackingPawnMove(step)) {
            performPermutationInBoard(step, currentPiece);
            return;
        }

        if (checkForAttackingPawnMove(step) && isMagicMovePossible &&
                isSameCage(step.endCage(), cageThatWasJumpedOver)) {
            performPermutationInBoard(step, currentPiece);

            int shift = (color == PieceColor.BLACK) ? 1 : -1;
            int rowDeletedPawn = cageThatWasJumpedOver.horizontal() - 1 + shift;
            int colDeletedPawn = findQuantityColumns(cageThatWasJumpedOver);
            board[rowDeletedPawn][colDeletedPawn] = null;

            isMagicMovePossible = false;
            cageThatWasJumpedOver = null;
            return;
        } else {
            isMagicMovePossible = false;
            cageThatWasJumpedOver = null;
        }

        if (checkPawnForDoubleJump(step) && checkStepForPossible(step, currentPiece)) {
            isMagicMovePossible = true;
            int shift = (color == PieceColor.BLACK) ? -1 : 1;
            cageThatWasJumpedOver = new Cage(currentPiece.getCage().vertical(),
                    currentPiece.getCage().horizontal() + shift);

            performPermutationInBoard(step, currentPiece);
            return;
        }

        if (checkStepForPossible(step, currentPiece)) {
            performPermutationInBoard(step, currentPiece);
        } else {
            throw new IllegalArgumentException("Пешка так не ходит");
        }
    }

    private boolean checkForAttackingPawnMove(Step step) {
        int startV = step.startCage().vertical();
        int startH = step.startCage().horizontal();
        int endV = step.endCage().vertical();
        int endH = step.endCage().horizontal();

        if (color == PieceColor.WHITE) {
            if (startV < 6 && ((endH == startH && startV - 1 == endV) || (endH == 1 + startH && startV + 1 == endV))) {
                return true;
            } else {
                if (startV > 6 && ((endH == startH && startV + 1 == endV) || (endH == 1 + startH && startV - 1 == endV))) {
                    return true;
                } else {
                    return startV == 6 && endH == startH && Math.abs(startV - endV) == 1;
                }
            }
        } else {
            if (startV < 6 && ((endH == startH - 1 && startV - 1 == endV) || (endH == startH && startV + 1 == endV))) {
                return true;
            } else {
                if (startV > 6 && ((endH == startH && startV - 1 == endV) || (endH == startH - 1 && startV + 1 == endV))) {
                    return true;
                } else {
                    return startV == 6 && endH == startH - 1 && Math.abs(startV - endV) == 1;
                }
            }
        }
    }

    private boolean checkPawnForDoubleJump(Step step) {
        int startV = step.startCage().vertical();
        int startH = step.startCage().horizontal();
        int endV = step.endCage().vertical();
        int endH = step.endCage().horizontal();

        if (color == PieceColor.WHITE && startV == endV && startH + 2 == endH) {
            return true;
        }

        return color == PieceColor.BLACK && startV == endV && startH - 2 == endH;
    }
}


/*
 if (isCageBusy(step.endCage()) && getPiece(step.endCage()).getColor() != currentPiece.getColor() &&
                checkStepForPossible(step, currentPiece)) {
            throw new IllegalStateException("Пешка так бить не может");
        } else {
            if (isCageBusy(step.endCage()) && getPiece(step.endCage()).getColor() != color &&
                    checkForAttackingPawnMove(step)) {
                performPermutationInBoard(step, currentPiece);
            } else {
                if (checkForAttackingPawnMove(step) && isMagicMovePossible &&
                        isSameCage(step.endCage(), cageThatWasJumpedOver)) {
                    performPermutationInBoard(step, currentPiece);
                    isMagicMovePossible = false;
                    cageThatWasJumpedOver = null;
                } else {
                    isMagicMovePossible = false;
                    cageThatWasJumpedOver = null;

                    if (checkStepForPossible(step, currentPiece) && checkStepForEscapeFromTheBattle(step)) {
                        isMagicMovePossible = true;
                        int shift = (color == PieceColor.BLACK) ? -1 : 1;
                        cageThatWasJumpedOver = new Cage(currentPiece.getCage().vertical(),
                                currentPiece.getCage().horizontal() + shift);
                    } else {
                        if (checkStepForPossible(step, currentPiece)) {
                            performPermutationInBoard(step, currentPiece);
                        } else {
                            throw new IllegalStateException("Пешка так не ходит");
                        }
                    }
                }

            }
        }
 */