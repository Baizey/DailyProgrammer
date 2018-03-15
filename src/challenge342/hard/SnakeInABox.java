package challenge342.hard;

import lsm.helpers.IO.write.text.console.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.stream.IntStream;

public class SnakeInABox {
    public static void main(String... args) {
        for (int i = 1; i < 10; i++) {
            Colony colony = new Colony(i);
            Note.writenl("Dimension " + i + ": " + colony.solve());
        }
    }
}

class Colony {
    private int dimension;
    private Node start;

    Colony(int dimension) {
        this.dimension = dimension;
        start = new Node(dimension, new BitSet(dimension));
        HashMap<Integer, Node> nodes = new HashMap<>();
        nodes.put(start.id, start);
        int max = (int) Math.pow(2, dimension);
        // Create nodes in graph
        for (int i = 1; i < max; i++) {
            BitSet id = new BitSet(dimension);
            String bitString = Long.toBinaryString(i);
            if (bitString.length() < dimension) {
                char[] fill = new char[dimension - bitString.length()];
                Arrays.fill(fill, '0');
                bitString = new String(fill) + bitString;
            } else bitString = bitString.substring(bitString.length() - dimension);
            bitString = new StringBuilder(bitString).reverse().toString();
            for (int j = 0; j < dimension; j++)
                id.set(j, bitString.charAt(j) == '1');
            nodes.put(i, new Node(dimension, id));
        }
        // Add edges in graph
        for (Node n : nodes.values()) {
            BitSet id = n.bits;
            for (int i = 0; i < dimension; i++) {
                boolean b = id.get(i);
                id.set(i, !b);
                nodes.get(n.createId()).addEdge(n, i);
                id.set(i, b);
            }
        }
    }

    int solve(){
        Node at = start;
        ArrayList<Integer> path = new ArrayList<>();
        do {
            int pick = at.getBest();
            path.add(pick);
            at.alter();
            at = at.edges[pick];
        } while(at.canMove());

        return path.size();
    }

}

class Node {
    final BitSet bits;
    final int id;
    private int value;
    final Node[] edges;

    Node(int dimesions, BitSet id) {
        this.value = dimesions;
        this.bits = id;
        this.id = createId();
        edges = new Node[dimesions];
    }

    void alter(){
        for(Node n :edges)
            for(Node nn : n.edges)
                if(nn.value > 0) nn.value++;
        for(Node n :edges) n.value = -1;
        this.value = -1;
    }

    void addEdge(Node node, int i) {
        if (edges[i] != null) return;
        edges[i] = node;
        node.edges[i] = this;
    }

    int createId() {
        return bits.length() > 0 ? (int) bits.toLongArray()[0] : 0;
    }

    boolean canMove() {
        return Arrays.stream(edges)
                .filter(node -> node.value > 0)
                .map(i -> true).findFirst().orElse(false);
    }

    int getBest() {
        int best = IntStream.range(0, edges.length)
                .filter(n -> edges[n].value > 0)
                .findFirst().orElse(-1);
        for(int i = best + 1; i < edges.length; i++)
            if(edges[i].value > 0 && edges[i].value < edges[best].value)
                best = i;
        return best;
    }
}