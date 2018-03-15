package challenge344.inter;

import lsm.helpers.IO.write.text.console.Note;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Banker {

    private static final String data = "[3 3 2]\n[0 1 0 7 5 3]\n[2 0 0 3 2 2]\n[3 0 2 9 0 2]\n[2 1 1 2 2 2]\n[0 0 2 4 3 3]";
    public static void main(String... args){
        solve(data);
    }

    public static void solve(String data){
        String[] input = data.split("\n");
        String[] first = input[0].replaceAll("[\\[\\]]", "").split(" ");
        int[] avail = Arrays.stream(first).mapToInt(Integer::parseInt).toArray();
        ArrayDeque<Process> processes = new ArrayDeque<>(IntStream.range(1, input.length).mapToObj(i -> new Process(i - 1, input[i])).collect(Collectors.toList()));

        ArrayList<Process> order = new ArrayList<>();
        Process curr;
        int noLuck = 0;
        while((curr = processes.pollFirst()) != null) {
            if(curr.canComplete(avail)) {
                noLuck = 0;
                order.add(curr);
                curr.addTo(avail);
            } else {
                processes.addLast(curr);
                if(++noLuck > processes.size()) break;
            }
        }

        Note.writenl("Could complete the following process in order:");
        Note.writenl(order);
        Note.write("With this left free after: ");
        Note.writenl(avail);
        if(processes.size() > 0) {
            Note.writenl("Could not complete the following process:");
            Note.writenl(processes);
        } else Note.writenl("Could complete all process");
    }



}

class Process {
    private final int id;
    private int[] give, need;

    Process(int id, String input) {
        this.id = id;
        String[] data = input.replaceAll("[\\[\\]]", "").split(" ");
        give = new int[data.length / 2];
        need = new int[give.length];
        for(int i = 0; i < need.length; i++) {
            give[i] = Integer.parseInt(data[i]);
            need[i] = Integer.parseInt(data[i + need.length]) - give[i];
        }
    }

    boolean canComplete(int[] avail) {
        for(int i = 0; i < avail.length; i++)
            if(avail[i] < need[i])
                return false;
        return true;
    }

    void addTo(int[] avail){
        for(int i = 0; i < avail.length; i++)
            avail[i] += give[i];
    }

    public String toString(){
        return String.valueOf(id);
    }
}
