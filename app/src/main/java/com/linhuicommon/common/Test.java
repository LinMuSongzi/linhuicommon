package com.linhuicommon.common;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Test {

    private int MAX = 33;
    int[] numbers = new int[MAX];
    int[] openRed = new int[6];
    int openCount;
    int count = 0;
    int openBule;
    final List<Integer> REPEAT = new LinkedList<>();
    final List<Integer[]> OPENS = new LinkedList<>();
    int agreeNumber;
    private int[] buyNumbers = {1, 2, 12, 14, 24, 28, 07 * 100};
    private boolean[] agrees;

    public void input(int maxNumber) {
        numbers = new int[maxNumber];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
        }
    }

    public int getNumber() {

        Random random = new Random();
        int i = random.nextInt(numbers.length) + 1;
        for (int n : openRed) {
            if (n == 0) {
                break;
            } else if (i == n) {
                count++;
                REPEAT.add(i);
                i = getNumber();
                return i;
            }
        }
        return i;
    }


    public static void main(String[] args) {

        Test test = new Test();

        test.start(-10);

    }

    private void start(int r) {
        for (int i = 0; i < 6; i++) {
            System.out.print(print(buyNumbers[i]) + " ");
        }
        System.out.println("+ " + print(buyNumbers[buyNumbers.length - 1] / 100));
        System.out.println();

        boolean flag = r < 0;
        for (; ; ) {
            openCount++;
            for (int i = 0; i < 6; i++) {
//            System.out.print(print(test.getNumber()) + " ");
                openRed[i] = getNumber();
            }
            Arrays.sort(openRed);
            for (int i = 0; i < 6; i++) {
                System.out.print(print(openRed[i]) + " ");
            }
            openBule = new Random().nextInt(16) + 1;

            {
                Integer[] o = new Integer[7];
                for (int i = 0; i < 6; i++) {
                    o[i] = openRed[i];
                }
                o[o.length - 1] = openBule;
                OPENS.add(o);

            }


            System.out.print("+ " + print(openBule) + "   ");
            System.out.print(Arrays.toString(checkIsMatching()));
            System.out.print("    --    count = " + count);
            System.out.print("   " + Arrays.toString(REPEAT.toArray()));
            System.out.print("   " + agreeNumber + " + " + (agrees[buyNumbers.length - 1] ? 1 : 0));
            System.out.println();
            if (agreeNumber == 6){// && agrees[buyNumbers.length - 1]) {

                break;
            }
            REPEAT.clear();
            count = 0;
            openRed = new int[6];
            openBule = 0;
            agreeNumber = 0;
            if (r > 0) {
                r--;
            } else if (!flag) {
                break;
            }
        }

        System.out.println("open " + openCount);

    }

    private boolean ok() {
        return agreeNumber == 6;
    }

    private boolean[] checkIsMatching() {
        agrees = new boolean[buyNumbers.length];
        int[] copyOpenRed = Arrays.copyOf(openRed, 6);
        Arrays.sort(buyNumbers);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (buyNumbers[i] == copyOpenRed[j]) {
                    agrees[j] = true;
                    agreeNumber++;
                    break;
                }
            }
        }
        agrees[buyNumbers.length - 1] = openBule == buyNumbers[buyNumbers.length - 1] / 100;
        return agrees;
    }

    private static String print(int number) {
        return number > 9 ? String.valueOf(number) : "0" + number;
    }

}
