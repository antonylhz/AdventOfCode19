package day2;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Intcode {

    private int[] data;
    private int counter;

    private int run(final int[] originalData, final int noun, final int verb) {
        data = Arrays.copyOf(originalData, originalData.length);
        data[1] = noun;
        data[2] = verb;
        for (int i = 0; i < data.length; i += 4) {
            if (data[i] == 99) break;
            else if (data[i] == 1) {
                data[data[i + 3]] = data[data[i + 1]] + data[data[i + 2]];
            } else if (data[i] == 2) {
                data[data[i + 3]] = data[data[i + 1]] * data[data[i + 2]];
            }
        }
        return data[0];
    }

    private Integer find(int[] data, int target) {
        counter = 0;
        for (int noun = 12; noun < data.length; noun++) {
            for (int verb = 2; verb < data.length; verb++) {

                    int res = run(data, noun, verb);
                    counter++;
                    System.out.println((counter++) + ": " + noun + "," + verb + " = " + res);
                    if (res == target) {
                        return noun * 100 + verb;
                    }
            }
        }

        return null;
    }

    private Integer binarySearch(int[] data, int target) {
        counter = 0;
        return binarySearch(data, 0,data.length - 1, 0, data.length - 1, target);
    }

    private Integer binarySearch(int[] data, int n1, int n2, int v1, int v2, int target) {
        if (n1 < 0 || n2 < 0 || n2 >= data.length || n1 > n2 ||
                v1 < 0 || v2 < 0 || v2 >= data.length || v1 > v2) {
            return null;
        }

        int nm = n1 + (n2 - n1) / 2, vm = v1 + (v2 - v1) / 2;
        int res = run(data, nm, vm);
        System.out.println((counter++) + " : " + "n: " + n1 + "->" + n2 + ", v: " + v1 + "->" + v2 + " = " + res);
        Integer config;
        if (res == target) {
            config = nm * 100 + vm;
        } else if (res < target) {
            config = binarySearch(data, n1, nm, vm + 1, v2, target);
            if (config == null)
                config = binarySearch(data, nm + 1, n2, v1, v2, target);
        } else {
            config = binarySearch(data, nm, n2, v1, vm - 1, target);
            if (config == null)
                config = binarySearch(data, n1, nm - 1, v1, v2, target);
        }
        return config;
    }

    public static void main(String[] args) {
        try {
            URL url = Intcode.class.getClassLoader().getResource("day2/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                int[] data = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    data[i] = Integer.parseInt(tokens[i]);
                }
                Intcode intcode = new Intcode();
                System.out.println(intcode.find(data,19690720));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

