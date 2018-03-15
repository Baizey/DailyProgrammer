package challenge348.hard;


import lsm.helpers.IO.write.text.console.Note;
import lsm.helpers.Time;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SquareSum {

    public static void main(String... args) {
        int[] input = {8, 15, 22, 23, 24, 25, 32, 64, 128, 256};
        Note.setSeperator(" -> ").setTags("", "");
        for (int i = 1; i <= 256; i *= 2) {
            int finalI = i;
            Time.takeTime(Integer.toString(i),
                    () -> Note.writenl(solve(finalI)));
        }
    }

    private static ArrayList<Node> solve(int n) {
        // Create graph
        Set<Integer> sums = IntStream.rangeClosed(1, (int) Math.sqrt(n * 2)).boxed()
                .map(i -> i * i).collect(Collectors.toSet());

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i + 1);
            for (int j = 0; j < i; j++)
                nodes[i].addEdge(nodes[j], sums);
        }

        // Sort lists so that we look at best likely next moves
        Arrays.sort(nodes, Comparator.comparingInt(a -> a.edges.size()));
        for (Node node : nodes)
            node.edges.sort((a, b) -> b.edges.size() - a.edges.size());

        // Find path going through entire graph
        ArrayDeque<Path> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
            queue.add(new Path(nodes[i]));

        Path curr;
        while ((curr = queue.pollLast()) != null) {
            if (curr.path.size() == n) return curr.path;
            for (Node node : curr.at.edges)
                if (!curr.visited.contains(node))
                    queue.addLast(new Path(node, curr));
        }

        // Failed finding path
        return null;
    }

}

class Path {
    ArrayList<Node> path = new ArrayList<>();
    HashSet<Node> visited = new HashSet<>();
    Node at;

    Path(Node at) {
        this.at = at;
        visited.add(at);
        path.add(at);
    }

    Path(Node at, Path from) {
        this.path.addAll(from.path);
        this.visited.addAll(from.visited);
        this.at = at;
        visited.add(at);
        path.add(at);
    }
}

class Node extends lsm.helpers.graph.Node {
    private int value;
    ArrayList<Node> edges = new ArrayList<>();

    Node(int value) {
        super(0);
        this.value = value; }

    void addEdge(Node node, Set<Integer> sums) {
        if (!sums.contains(value + node.value)) return;
        addEdgeTwoway(node);
        edges.add(node);
        node.edges.add(this);
    }

    public String toString() {
        return String.valueOf(value);
    }
}