import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Random;

public class DiceList {
    public String[][] dies = {
            {"B", "A", "O", "O", "Qu", "M"},
            {"U", "T", "E", "S", "L", "P"},
            {"I", "G", "E", "N", "V", "T"},
            {"O", "U", "L", "I", "E", "R"},
            {"A", "C", "E", "S", "L", "R"},
            {"R", "A", "T", "I", "B", "L"},
            {"S", "M", "I", "R", "O", "A"},
            {"I", "S", "E", "E", "F", "H"},
            {"S", "O", "T", "E", "N", "D"},
            {"A", "I", "C", "O", "F", "R"},
            {"V", "N", "Z", "D", "A", "E"},
            {"I", "E", "A", "T", "A", "O"},
            {"O", "T", "U", "C", "E", "N"},
            {"N", "O", "L", "G", "U", "E"},
            {"D", "C", "M", "P", "A", "E"},
            {"E", "R", "I", "N", "S", "H"},
    };
    public String[][] result = new String[4][4];

    public DiceList() {
        String[] randomLetter = new String[16];
        Random rand = new Random();
        for (int i = 0; i < 16; i++) {
            String[] item = dies[i];
            int n = rand.nextInt(6);
            randomLetter[i] = item[n];
        }
        for (String item : randomLetter) {
            int c = 0;
            int r = 0;
            boolean isAdd = false;
            do {
                isAdd = false;
                c = rand.nextInt(4);
                r = rand.nextInt(4);
                if (result[c][r] == null || result[c][r].isEmpty()) {
                    result[c][r] = item;
                    isAdd = true;
                }
            } while (isAdd == false);
        }

        for (int i = 0; i < 4; i++) {
            System.out.println(" ");
            for (int j = 0; j < 4; j++) {
                System.out.print(result[i][j] + " ");
            }
        }

        System.out.println(" ");

    }
}
