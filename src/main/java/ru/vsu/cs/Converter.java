package ru.vsu.cs;

import ru.vsu.cs.models.Cage;
import ru.vsu.cs.models.Step;

import java.util.Locale;

public class Converter {
    public static Step convertStringToStep(String string) {
        String[] cages = string.split(" ");

        if (cages.length != 2) {
            throw new IllegalArgumentException("Неверно введенный ход");
        }

        Cage startCage = convertStringToCage(cages[0]);
        Cage endCage = convertStringToCage(cages[1]);

        return new Step(startCage, endCage);
    }


    private static Cage convertStringToCage(String string) {
        if (string.length() != 2) {
            throw new IllegalArgumentException("Неверно заданная клетка");
        }

        string = string.toLowerCase(Locale.ROOT);

        int vertical = string.charAt(0) - 96;
        int horizontal = Integer.parseInt(string.substring(1));

        if (vertical == 10) {
            throw new IllegalArgumentException("На доске нет вертикали j");
        }

        if (vertical == 11 || vertical == 12) {
            vertical--;
        }

        Cage cage = new Cage(vertical, horizontal);

        if (!BoardHelper.isCageBelongBoard(cage)) {
            throw new IllegalArgumentException("На доске нет клетки " + string);
        }

        return cage;
    }
}
