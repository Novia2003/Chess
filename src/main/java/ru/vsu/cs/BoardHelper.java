package ru.vsu.cs;

import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.PieceColor;
import ru.vsu.cs.models.pieces.*;

public class BoardHelper {
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
}
