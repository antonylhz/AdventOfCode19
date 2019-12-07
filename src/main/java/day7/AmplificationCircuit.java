package day7;

import day5.IntCodeComputer;

import java.io.File;
import java.net.URL;
import java.util.*;

public class AmplificationCircuit {

    private int ampSize;
    private LinkedList<IntCodeComputer> amplifiers;
    private int[] data;

    public AmplificationCircuit(int[] data, int ampSize) {
        this.ampSize = ampSize;
        this.data = data;
        amplifiers = new LinkedList<>();
        for (int i = 0; i < ampSize; i++) {
            amplifiers.add(new IntCodeComputer(Arrays.copyOf(data, data.length)));
        }
    }

    // Reset all int-code computers to original data
    public void reset() {
        amplifiers.clear();
        for (int i = 0; i < ampSize; i++) {
            amplifiers.add(new IntCodeComputer(Arrays.copyOf(data, data.length)));
        }
    }

    private int calculateSignal(List<Integer> phaseSettings, boolean firstLoop, int start) {
        LinkedList<Integer> input = new LinkedList<>();
        LinkedList<Integer> output;
        int extra = start;
        for (int i = 0; i < amplifiers.size(); i++) {
            if (firstLoop) {
                input.add(phaseSettings.get(i));
            }
            input.add(extra);
            if ((output = amplifiers.get(i).run(input)).isEmpty()) {
                throw new IllegalStateException("No output!");
            }
            extra = output.peekLast();
            input.clear();
        }
        return extra;
    }

    public int calculateSignalInFeedbackLoop(List<Integer> phaseSettings) {
        int signal = calculateSignal(phaseSettings, true, 0);
        while(!amplifiers.isEmpty() && !amplifiers.peekLast().isHalted) {
            signal = calculateSignal(phaseSettings, false, signal);
        }
        return signal;
    }

    public int findLargestSignalInOneLoop() {
        List<List<Integer>> phaseSettings = permute(0, 4);
        int largest = 0;
        for (List<Integer> phaseSetting : phaseSettings) {
            int signal = calculateSignal(phaseSetting, true, 0);
            System.out.println(phaseSetting + "->" + signal);
            largest = Math.max(largest, signal);
            reset();
        }
        return largest;
    }

    public int findLargestSignalInFeedbackLoop() {
        List<List<Integer>> phaseSettings = permute(5, 9);
        int largest = 0;
        for (List<Integer> phaseSetting : phaseSettings) {
            int signal = calculateSignalInFeedbackLoop(phaseSetting);
            System.out.println(phaseSetting + "->" + signal);
            largest = Math.max(largest, signal);
            reset();
        }
        return largest;
    }

    private List<List<Integer>> permute(int start, int end) {
        if (start == end) {
            return Collections.singletonList(Collections.singletonList(start));
        }
        List<List<Integer>> ps = permute(start, end - 1);
        List<List<Integer>> res = new LinkedList<>();
        for (int i = 0; i <= end - start; i++) {
            for (List<Integer> p: ps) {
                List<Integer> np = new LinkedList<>(p);
                np.add(i,  end);
                res.add(np);
            }
        }
        return res;
    }

    public static void main (String[] args) {
        try {
            URL url = AmplificationCircuit.class.getClassLoader().getResource("day7/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                int[] data = new int[tokens.length];
                for (int i = 0; i < data.length; i++) {
                    data[i] = Integer.parseInt(tokens[i]);
                }
                AmplificationCircuit amplificationCircuit = new AmplificationCircuit(data, 5);
                System.out.println(amplificationCircuit.findLargestSignalInOneLoop());
                System.out.println(amplificationCircuit.findLargestSignalInFeedbackLoop());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
