package challenge352.hard;

import lsm.helpers.IO.write.text.console.Note;
import lsm.helpers.Time;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

public class Well {

    static String[] problems = new String[]{
            "3 3\n1 9 6\n2 8 5\n3 7 4\n4",
            "7 7" +
                    "\n38 33 11 48 19 45 22" +
                    "\n47 30 24 15 46 28 3" +
                    "\n14 13 2 34 8 21 17" +
                    "\n10 9 5 16 27 36 39" +
                    "\n18 32 20 1 35 49 12" +
                    "\n43 29 4 41 26 31 37" +
                    "\n25 6 23 44 7 42 40" +
                    "\n35",
            "7 7\n15 16 46 1 38 43 44\n25 10 7 6 34 42 14\n8 19 9 21 13 23 22\n32 11 29 36 3 5 47\n31 33 45 24 12 18 28\n40 41 20 26 39 48 2\n49 35 27 4 37 30 17\n26"
    };

    public static void main(String... args) {
        for (String problem : problems)
            Time.takeTime(() -> Note.writenl(solve(problem)));
    }

    private static int solve(String input) {
        String[] lines = input.split("\n");
        Node[][] grid = new Node[lines.length - 2][];
        for (int i = 1, j = 0; j < grid.length; i++, j++) {
            String[] cols = lines[i].split(" +");
            grid[j] = new Node[cols.length];
            for (int k = 0; k < cols.length; k++)
                grid[j][k] = new Node(cols[k]);
        }

        for (int y = 0; y < grid.length - 1; y++) {
            for (int x = 0; x < grid[y].length - 1; x++) {
                grid[x][y].add(grid[x + 1][y]);
                grid[x][y].add(grid[x][y + 1]);
            }
        }
        for (int i = 0; i < grid[0].length - 1; i++)
            grid[grid.length - 1][i].add(grid[grid.length - 1][i + 1]);
        for(int i = 0; i < grid.length - 1; i++)
            grid[i][grid[i].length - 1].add(grid[i + 1][grid[i].length - 1]);

        Node start = null;
        Node goal = null;
        for (Node[] row : grid)
            for (Node node : row)
                if (node.height == 1) start = node;
                else if (node.height == Integer.parseInt(lines[lines.length - 1])) goal = node;
        return solve(start, goal);
    }

    static void solve(Node start, int max) {
        while ((Node.goal.id >= Node.goalHolder.height) && start.height < max)
            start.addNextLevel(max);
    }

    private static int solve(Node start, Node goal) {
        Node.visited = new HashSet<>(){{ add(start); }};
        Node.goal = goal;
        Node.goalHolder = goal;
        while (Node.goal.id >= Node.goalHolder.height)
            start.addNextLevel(Integer.MAX_VALUE);
        return Node.waterUsed;
    }

}

class Node {
    final int id;
    static Node goal, goalHolder;
    static int waterUsed = 0;
    private int tiles = 1;
    static HashSet<Node> visited = new HashSet<>();
    private TreeSet<Node> neigbours = new TreeSet<>(Comparator.comparingInt(a -> a.height));
    int height;

    Node(String n) {
        this.height = Integer.parseInt(n);
        this.id = height;
    }

    @SuppressWarnings("ConstantConditions")
    void addNextLevel(int max) {
        Node victim = neigbours.pollFirst();
        if (visited.contains(victim)) return;
        visited.add(victim);
        if(id == goalHolder.id) max = goal.id + 1;
        max = Math.min(max, victim.height);

        if (max > height) {
            //Note.writenl(id + " += " + tiles + " * " + (max - height) + " to " + max);
            waterUsed += tiles * (max - height);
            height = max;
        } else if (victim.height < height)
            Well.solve(victim, height);

        if(victim.id == goalHolder.id)
            goalHolder = this;

        if(victim.height <= height) {
            tiles += victim.tiles;
            victim.neigbours.stream()
                .filter(i -> !visited.contains(i))
                .forEach(neigbours::add);
        } else {
            neigbours.add(victim);
            visited.remove(victim);
        }
    }

    public void add(Node node) {
        neigbours.add(node);
        node.neigbours.add(this);
    }

    public String toString() {
        return String.valueOf(id);
    }
}