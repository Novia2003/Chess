package ru.vsu.cs;

import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.pieces.*;

import java.util.Arrays;
import java.util.Scanner;

public class BoardHelper {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";

    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";

    public static AbstractPiece[][] getBoardWithInitialPositionPieces() {
        AbstractPiece[][] board = new AbstractPiece[11][];

        for (int i = 0; i < 11; i++) {
            int length = (i > 5) ? 11 - 2 * (i - 5) : 11;
            board[i] = new AbstractPiece[length];
        }

        board[0][1] = new Pawn(new Cage(2, 1), PieceColor.WHITE);
        board[0][2] = new Rook(new Cage(3, 1), PieceColor.WHITE);
        board[0][3] = new Knight(new Cage(4, 1), PieceColor.WHITE);
        board[0][4] = new Queen(new Cage(5, 1), PieceColor.WHITE);

        for (int i = 0; i < 3; i++) {
            board[i][5] = new Bishop(new Cage(6, 1 + i), PieceColor.WHITE);
        }

        board[0][6] = new King(new Cage(7, 1), PieceColor.WHITE);
        board[0][7] = new Knight(new Cage(8, 1), PieceColor.WHITE);
        board[0][8] = new Rook(new Cage(9, 1), PieceColor.WHITE);

        for (int i = 0; i < 4; i++) {
            board[i][i + 1] = new Pawn(new Cage(i + 2, i + 1), PieceColor.WHITE);
            board[i][9 - i] = new Pawn(new Cage(10 - i, i + 1), PieceColor.WHITE);
        }
        board[4][5] = new Pawn(new Cage(6, 5), PieceColor.WHITE);

        for (int i = 0; i < 9; i++) {
            board[6][i] = new Pawn(new Cage(2 + i, 7), PieceColor.BLACK);
        }

        board[7][0] = new Rook(new Cage(3, 8), PieceColor.BLACK);
        board[8][0] = new Knight(new Cage(4, 9), PieceColor.BLACK);
        board[9][0] = new Queen(new Cage(5, 10), PieceColor.BLACK);

        for (int i = 0; i < 3; i++) {
            board[10 - i][i] = new Bishop(new Cage(6, 11 - i), PieceColor.BLACK);
        }

        board[9][2] = new King(new Cage(7, 10), PieceColor.BLACK);
        board[8][4] = new Knight(new Cage(8, 9), PieceColor.BLACK);
        board[7][6] = new Rook(new Cage(9, 8), PieceColor.BLACK);

        return board;
    }

    public static boolean isCageBelongBoard(Cage cage) {
        int x = cage.vertical();
        int y = cage.horizontal();

        if (x < 12 && y < 12 && x > 0 && y > 0) {
            if (y > 6) {
                int number = y - 6;

                return x > number && x < 12 - number;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static String[][] getBoardForConsole(AbstractPiece[][] array) {
        String[][] board = new String[22][13]; //21 11
        for (String[] row : board) {
            Arrays.fill(row, "        ");
        }

        int flag = 5;
        int rowForLetters = 1;
        int colForNumbers = 1;

        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                int boardRow, boardCol;

                if (row <= flag) {
                    boardRow = row * 2 + Math.abs(flag - col);
                    boardCol = col;
                } else {
                    int shift = row - flag;

                    boardRow = row * 2 + Math.abs((flag - shift) - col);
                    boardCol = col + shift;
                }

                board[boardRow + rowForLetters][boardCol + colForNumbers] = getDecoratedCage(array[row][col]);
            }
        }

        char[] letters = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'k', 'l'};

        for (int col = colForNumbers; col < board[0].length - colForNumbers; col++) {
            int row = Math.abs(flag + colForNumbers - col);
            board[row][col] = "    " + letters[col - colForNumbers] + "   ";
        }

        for (int row = 0; row < array.length; row++) {
            int boardRow, boardCol;
            if (row <= flag) {
                boardRow = row * 2 + flag;
                boardCol = 0;
            } else {
                int shift = row - flag;

                boardRow = row * 2 + flag - shift;
                boardCol =  shift;
            }

            int numberRow = row + 1;
            String indent = (numberRow > 9) ? "    " : "     ";

            board[boardRow + rowForLetters][boardCol] = indent + numberRow + "  ";

            board[boardRow + rowForLetters][boardCol + array[row].length + colForNumbers] = "  " + numberRow;
        }

        return board;
    }

    private static String getDecoratedCage(AbstractPiece piece) {
        if (piece == null) {
            return ANSI_YELLOW_BACKGROUND + "        " + ANSI_RESET;
        }

        String name = piece.getName();
        String result;

        switch (name.length()) {
            case 4 -> result = "  " + name + "  ";
            case 5 -> result = " " + name + "  ";
            case 6 -> result = " " + name + " ";
            default ->
                    throw new IllegalStateException("Я не создавал фигуру с названием из " + name.length() + " букв");
        }

        if (piece.getColor() == PieceColor.BLACK) {
            result = ANSI_YELLOW_BACKGROUND + ANSI_BLACK + result + ANSI_RESET;
        } else {
            result = ANSI_YELLOW_BACKGROUND + result + ANSI_RESET;
        }

        return result;
    }

    public static void writeBoardToConsole(String[][] board) {
        for (int row = board.length - 1; row > -1; row--) {
            for (String col : board[row]) {
                System.out.print(col);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String readCommand(PieceColor color) {
        String currentColor = (color == PieceColor.WHITE) ? "белые" : "чёрное";
        System.out.println("Сейчас ходят " + currentColor + " фигуры:");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        return command.strip();
    }
}
