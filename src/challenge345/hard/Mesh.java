package challenge345.hard;


import lsm.helpers.IO.write.text.console.Note;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Mesh {

    public static void main(String... args) {
        Arrays.stream(new String[]{
                "5\n0 0\n0 4\n4 4\n4 0\n2 2",
                "8\n0 0\n0 6\n6 0\n6 6\n2 2\n2 4\n4 2\n4 4",
                "0 0\n0 32\n32 0\n32 32\n13 13\n13 19\n19 19\n19 13\n16 5\n16 27\n5 16\n27 16"
        }).map(Mesh::triangulate).forEach(Note::writenl);
    }

    private static PrettyPoint[][] triangulate(String input) {
        PrettyPoint[] points = Pattern.compile("\n").splitAsStream(input)
                .map(str -> str.split(" "))
                .filter(str -> str.length == 2)
                .map(str -> Arrays.stream(str).mapToInt(Integer::parseInt).toArray())
                .map(i -> new PrettyPoint(i[0], i[1]))
                .toArray(PrettyPoint[]::new);

        //Arrays.sort(points, (o1, o2) -> o1.x == o2.x ? o1.y - o2.y : o1.x - o2.x);

        ArrayList<PrettyPoint[]> edges = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            PrettyPoint a = points[i];
            next:
            for (int j = 0; j < i; j++) {
                PrettyPoint b = points[j];

                for (PrettyPoint[] line : edges)
                    if (crossSign(a, b, line))
                        continue next;

                edges.add(new PrettyPoint[]{a, b});
            }
        }

        return edges.toArray(new PrettyPoint[edges.size()][2]);
    }

    private static boolean crossSign(PrettyPoint a, PrettyPoint b, PrettyPoint[] line) {
        return crossSign(line[0], line[1], a) != crossSign(line[0], line[1], b)
                && crossSign(a, b, line[0]) != crossSign(a, b, line[1]);
    }

    private static boolean crossSign(PrettyPoint origin, PrettyPoint end1, PrettyPoint end2) {
        BigDecimal p1 = BigDecimal.valueOf(end1.x - origin.x).multiply(BigDecimal.valueOf(end1.y - origin.y));
        BigDecimal p2 = BigDecimal.valueOf(end2.x - origin.x).multiply(BigDecimal.valueOf(end2.y - origin.y));
        return p1.subtract(p2).compareTo(BigDecimal.ZERO) < 0;
    }

}

class PrettyPoint extends Point {
    int id = nextId++;
    private static int nextId = 0;

    PrettyPoint(int x, int y) {
        super(x, y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}