package ru.vsu.cs;

import static ru.vsu.cs.BoardHelper.readCommand;
import static ru.vsu.cs.BoardHelper.writeBoardToConsole;

public class Main {
    public static void main(String[] args) {
        Board board = BoardFactory.create();
        play(board);
    }

    private static void play(Board board) {
        writeBoardToConsole(board.getField());

        while (!board.isGameEnded()) {
            try {
                board.executeCommand(readCommand(board.getColor()));
                writeBoardToConsole(board.getField());
            } catch (IllegalArgumentException exception) {
                System.out.println(exception.getMessage());
                System.out.println("Не расстраивайся, всё образумится. Попробуй ещё раз");
            }
        }
    }
}





























//for (int h = 1; h < 12; h++) {
//            if (h > 6) {
//                number++;
//            }
//
//            Cage cage = new Cage(9, 6);
//
//            for (int v = 1 + number; v < 12 - number; v++) {
//                if (cage.horizontal() == h && cage.vertical() == v) {
//                    continue;
//                }
//                if (h >= 6 && v >= 6 && h - cage.horizontal() == v - cage.vertical()) {
//                    possibleSteps.add(new Cage(v, h));
//                }
//
//                if (h > 6 && v < 6 && cage.vertical() - v == h - cage.horizontal()) {
//                    possibleSteps.add(new Cage(v, h));
//                }
//
//                if (Integer.compare(v, 6) * (v - cage.vertical()) == 2 * (cage.horizontal() - h)) {
//                    possibleSteps.add(new Cage(v, h));
//                }
//
//                if (h < 6 && v > 6 && 2 * (v - cage.vertical()) == cage.horizontal() - h) {
//                    possibleSteps.add(new Cage(v, h));
//                }
//
//                if (h < 6 && v < 6 && 2 * (cage.vertical() - v) == cage.horizontal() - h) {
//                    possibleSteps.add(new Cage(v, h));
//                }
//            }
//        }

//public class Main {
//    public static void main(String[] args) {
//        List<Cage> possibleSteps = new ArrayList<>();
//        Cage cage = new Cage(6,1);
//
////        op(1, -1,-2, cage, possibleSteps);
////        op(2, 1, -1, cage, possibleSteps);
////        op(1, 2, 1, cage, possibleSteps);
////        op1(-1, -1, -2, cage, possibleSteps);
////        op1(-2, 1, -1, cage, possibleSteps);
////        op1(-1, 2, 1, cage, possibleSteps);
////
////        for (Cage v : possibleSteps) {
////            System.out.println(v.vertical() + " " + v.horizontal());
////        }
//
//        int x = cage.vertical();
//        int y = cage.horizontal();
//        int number = 0;
//
//        op(0, -1, cage, possibleSteps);
//        op(1, 0, cage, possibleSteps);
//        op1(1, 0, cage, possibleSteps);
//        op1(0, -1, cage, possibleSteps);
//
////        for (int h = 1; h < 12; h++) {
////            if (h > 6) {
////                number++;
////            }
////
////            for (int v = 1 + number; v < 12 - number; v++) {
////                if (v == x && h != y) {
////                    possibleSteps.add(new Cage(v, h));
////                }
////            }
////        }
//
//        for (int i = 1; i < 12 - Math.abs(6 - x); i++) {
//            if (i != y) {
//                possibleSteps.add(new Cage(x, i));
//            }
//        }
//
//                for (Cage v : possibleSteps) {
//            System.out.println(v.vertical() + " " + v.horizontal());
//        }
//    }
//
//    private static void op(int firstIncrementH, int secondIncrementH, Cage cage, List<Cage> possibleSteps) {
//        int v = cage.vertical();
//        int h = cage.horizontal();
//        Cage probablyCage;
//
//        if (v + 1 <= 6) {
//            v++;
//            h += firstIncrementH;
//            probablyCage = new Cage(v, h);
//
//            while (v <= 6 && BoardHelper.isCageBelongBoard(probablyCage)) {
//                possibleSteps.add(probablyCage);
//                v += 1;
//                h += firstIncrementH;
//                probablyCage = new Cage(v, h);
//            }
//
//            v--;
//            h -= firstIncrementH;
//
//            if (v < 6) {
//                return;
//            }
//        }
//
//        v++;
//        h += secondIncrementH;
//
//        probablyCage = new Cage(v, h);
//
//        while (BoardHelper.isCageBelongBoard(probablyCage)) {
//            possibleSteps.add(probablyCage);
//            v++;
//            h += secondIncrementH;
//            probablyCage = new Cage(v, h);
//        }
//    }
//
//    private static void op1(int firstIncrementH, int secondIncrementH, Cage cage, List<Cage> possibleSteps) {
//        int v = cage.vertical();
//        int h = cage.horizontal();
//        Cage probablyCage;
//
//        if (v + -1 >= 6) {
//            v--;
//            h += firstIncrementH;
//            probablyCage = new Cage(v, h);
//
//            while (v >= 6 && BoardHelper.isCageBelongBoard(probablyCage)) {
//                possibleSteps.add(probablyCage);
//                v += -1;
//                h += firstIncrementH;
//                probablyCage = new Cage(v, h);
//            }
//
//            v++;
//            h -= firstIncrementH;
//
//            if (v > 6) {
//                return;
//            }
//        }
//
//        v--;
//        h += secondIncrementH;
//
//        probablyCage = new Cage(v, h);
//
//        while (BoardHelper.isCageBelongBoard(probablyCage)) {
//            possibleSteps.add(probablyCage);
//            v--;
//            h += secondIncrementH;
//            probablyCage = new Cage(v, h);
//        }
//    }
//}