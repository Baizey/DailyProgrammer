package challenge373.hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Node {

    private List<Node> children = new ArrayList<>();
    private int directChildren = 0;
    private int totalChildren = 0;

    static Node from(String str) {
        var root = new Node();
        if (str.length() <= 2) return root;
        var children = str.substring(1, str.length() - 1);
        int depth = 0, start = 0;
        for (int i = 0; i < children.length(); i++) {
            if (children.charAt(i) == '(') depth++;
            else depth--;
            if (depth == 0) {
                root.add(Node.from(children.substring(start, i + 1)));
                start = i + 1;
            }
        }
        return root;
    }

    private void add(Node node) {
        children.add(node);
        directChildren++;
        totalChildren += node.totalChildren + 1;
        children.sort(Node::difference);
    }

    private int difference(Node other) {
        if (directChildren != other.directChildren)
            return directChildren - other.directChildren;
        if (totalChildren != other.totalChildren)
            return totalChildren - other.totalChildren;
        for (int i = 0; i < children.size(); i++) {
            var diff = children.get(i).difference(other.children.get(i));
            if (diff == 0) continue;
            return diff;
        }
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Node)
            return difference((Node) other) == 0;
        return false;
    }

    public String toString() {
        return toString(0);
    }

    private String toString(int depth) {
        if (children.size() == 0)
            return "\t".repeat(depth) + "()\n";
        return "\t".repeat(depth) + "(\n"
                       + children.stream().map(c -> c.toString(depth + 1)).collect(Collectors.joining())
                       + "\t".repeat(depth) + ")\n";
    }

    @SuppressWarnings("WeakerAccess")
    public boolean embeddable(Node other) {
        if (directChildren >= other.directChildren) {
            if (other.directChildren == 0) return true;


            Ant best = new Ant(directChildren), gen = new Ant(directChildren), curr = new Ant(directChildren);
            for (int generation = 0; generation < 100; generation++) {
                gen.reset();
                for (int ignored = 0; ignored < 100; ignored++) {
                    curr.reset();

                    // TODO: ACO algorithm


                    if (curr.betterThan(gen)) {
                        gen.copy(curr);
                        if (gen.score == 0)
                            break;
                    }
                }

                if (gen.betterThan(best)) {
                    best.copy(gen);
                    if (best.score == 0)
                        break;
                }
            }
        }

        return children.stream()
                       .filter(child -> child.embeddable(other))
                       .map(i -> true).findFirst().orElse(false);
    }
}

class Option {

    

}

class Ant {
    IntList genes;
    int score = Integer.MAX_VALUE;

    Ant(int size) {
        this.genes = new IntList(size);
    }

    void reset() {
        this.genes.reset();
        this.score = Integer.MAX_VALUE;
    }

    void copy(Ant other) {
        this.score = other.score;
        this.genes.copyOver(other.genes);
    }

    boolean betterThan(Ant other) {
        return this.score < other.score;
    }
}

class IntList {
    private final int[] array;
    private int size = 0;

    IntList(int size) {
        this.array = new int[size];
    }

    void reset() {
        this.size = 0;
    }

    void add(int value) {
        array[size++] = value;
    }

    void copyOver(IntList other) {
        size = other.size;
        System.arraycopy(other.array, 0, array, 0, size);
    }

    public int[] get() {
        return array;
    }

    public int[] getUsed() {
        return Arrays.copyOf(array, size);
    }

    public int size() {
        return size;
    }
}

