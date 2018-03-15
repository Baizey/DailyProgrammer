package challenge349.hard;

import lsm.helpers.IO.write.text.console.Note;

import static lsm.helpers.utils.Numbers.round;

public class PolyDivide {

    public static void main(String... args) {
        solve(2, "(0 0)(0.5 0.5)(0 1)");
        round(2, 1);
    }

    private static void solve(int n, String input){
        input = input.replaceAll("[()]", " ").replaceAll(" {2,}", " ").trim();
        String[] parts = input.split(" ");
        Point[] points = new Point[parts.length / 2 + 1];
        for(int i = 0, j = 0; i < points.length - 1; i++, j += 2)
            points[i] = new Point(Double.parseDouble(parts[j]), Double.parseDouble(parts[j + 1]));
        points[points.length - 1] = new Point(points[points.length - 2].x, points[points.length - 2].y);
        solve(n, points);
    }

    private static void solve(int n, Point[] points) {
        double totalArea = 0;
        for(int i = 1; i < points.length; i++)
            totalArea += points[i - 1].x * points[i].y - points[i - 1].y * points[i].x;
        totalArea = round(totalArea, 2).doubleValue();
        double goal = round(totalArea / n, 2).doubleValue();
        Note.writenl(totalArea);
    }
}

class Point{
    double x, y;
    Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    Point prev, next;

    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}