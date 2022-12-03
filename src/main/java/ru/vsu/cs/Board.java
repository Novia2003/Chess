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

    private boolean isPreviousMoveAvoidedBattle;
    private Cage cageThatWasJumpedOver;
    public Board(AbstractPiece[][] boardWithInitialPositionPieces) {
        this.board = boardWithInitialPositionPieces;
        this.isGameEnded = false;
        this.color = PieceColor.WHITE;
    }

    public String[][] getField() {
        return BoardHelper.getBoardForConsole(board);
    }

    public void executeCommand(String command) {
        if(isGameEnded) {
            throw new IllegalStateException("Конец партии");
        }

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
                throw new IllegalStateException("Конечная клетка занята фигурой того же цвета");
            }
        }

        if (currentPiece instanceof Pawn) {
            makePawnMove(currentPiece, step);
        } else {
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
       return  (cage.horizontal() < 7) ? cage.vertical() - 1 : cage.vertical() - (cage.horizontal() - 5);
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
            throw new IllegalStateException("Пешка так бить не может");
        } else {
            if (isCageBusy(step.endCage()) && getPiece(step.endCage()).getColor() != color &&
                    checkForAttackingPawnMove(step)) {
                performPermutationInBoard(step, currentPiece);
            } else {
                if (checkForAttackingPawnMove(step) && isPreviousMoveAvoidedBattle &&
                        isSameCage(step.endCage(), cageThatWasJumpedOver)) {
                    performPermutationInBoard(step, currentPiece);
                    isPreviousMoveAvoidedBattle = false;
                    cageThatWasJumpedOver = null;
                } else {
                    isPreviousMoveAvoidedBattle = false;
                    cageThatWasJumpedOver = null;

                    if (checkStepForPossible(step, currentPiece) && checkStepForEscapeFromTheBattle(step)) {
                        isPreviousMoveAvoidedBattle = true;
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

    private boolean checkStepForEscapeFromTheBattle(Step step) {
        int startV = step.startCage().vertical();
        int startH = step.startCage().horizontal();
        int endH = step.endCage().horizontal();

        if (color == PieceColor.WHITE && startH + 2 == endH && startV == startH) {
            if (startV < 6 && (checkCageForPieceWithAnotherColor(new Cage(startV - 1, startH)) ||
                    checkCageForPieceWithAnotherColor(new Cage(startV + 1, startH + 1)))) {
                return true;
            } else {
                if (startV > 6 && (checkCageForPieceWithAnotherColor(new Cage(startV + 1, startH)) ||
                checkCageForPieceWithAnotherColor(new Cage(startV - 1, startH + 1)))) {
                    return true;
                } else {
                    return startV == 6 && (checkCageForPieceWithAnotherColor(new Cage(startV + 1, startH)) ||
                            checkCageForPieceWithAnotherColor(new Cage(startV - 1, startH)));
                }
            }
        }

        if (color == PieceColor.BLACK && startH - 2 == endH && startV == startH) {
            if (startV < 6 && (checkCageForPieceWithAnotherColor(new Cage(startV - 1, startH - 1)) ||
                    checkCageForPieceWithAnotherColor(new Cage(startV + 1, startH)))) {
                return true;
            } else {
                if (startV > 6 && (checkCageForPieceWithAnotherColor(new Cage(startV - 1, startH)) ||
                        checkCageForPieceWithAnotherColor(new Cage(startV + 1, startH - 1)))) {
                    return true;
                } else {
                    return startV == 6 && (checkCageForPieceWithAnotherColor(new Cage(startV - 1, startH - 1)) ||
                            checkCageForPieceWithAnotherColor(new Cage(startV + 1, startH - 1)));
                }
            }
        }

        return false;
    }

    private boolean checkCageForPieceWithAnotherColor(Cage cage) {
        return BoardHelper.isCageBelongBoard(cage) && isCageBusy(cage) && getPiece(cage).getColor() != color;
    }
}
