package challenge341.hard;

import lsm.helpers.IO.write.image.GifWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Driver {

    public static void main(String... args) throws IOException, InterruptedException {
        String[] problems = new String[]{
                "2000, [(1000,1500),(1200, 1500),(1400,1600),(1600,1800)]",
                "2000, [(600, 600), (700, 1200)]",
                "2000, [(300, 300), (1300, 300)]",
                "2000, [(825, 820), (840, 830), (830, 865), (835, 900)]",
                "5079, [(5079, 2000), (5079, 3000)]",
                "5079, [(10, 2000), (10, 3000)]",
                "5079, [(2000, 10), (3000, 10)]",
                "5079, [(2000, 5079), (3000, 5079)]",
                "5079, [(0, 0), (600, 600)]",
                "5079, [(5079, 5079), (4479, 4479)]",
                "5079, [(0, 5079), (600, 4479)]",
                "5079, [(5079, 0), (4479, 600)]",
                "5079, [(1000, 0), (1000, 5079)]",
                "5079, [(0, 1000), (5079, 1000)]",
                "5079, [(0, 0), (5079, 5079)]",
        };

        for(int i = 0; i < problems.length; i++) {
            String str = problems[i];
            Problem problem = new Problem(str);
            RenderedImage image = problem.view().toImage();
            GifWriter gif = new GifWriter("image_" + i);
            gif.add(image);
            gif.close();
        }
    }

}
class Problem extends JComponent {
    private int SCALE;
    private static int FRAME = 30;
    private ArrayList<Point> points = new ArrayList<>();
    private int size;
    private Point topLeftCorner;
    private int frameSize;

    Problem(String input) {

        int[] nums = Pattern.compile(",").splitAsStream(input.replaceAll("[ \\[\\]()]", "")).mapToInt(Integer::parseInt).toArray();
        for(SCALE = 1; nums[0] / SCALE > 700; SCALE++){}
        nums = Arrays.stream(nums).map(i -> i / SCALE).toArray();
        size = nums[0];

        Point min = new Point(size, size);
        Point max = new Point(0, 0);

        for(int i = 1; i < nums.length; i += 2) {
            Point p = new Point(nums[i], nums[i + 1]);
            points.add(p);
            min.x = Math.min(min.x, p.x);
            min.y = Math.min(min.y, p.y);
            max.x = Math.max(max.x, p.x);
            max.y = Math.max(max.y, p.y);
        }
        // Calculate corner positions & width/height
        topLeftCorner = new Point(min.x - FRAME, min.y - FRAME);
        int xFrame = max.x - min.x + FRAME * 2;
        int yFrame = max.y - min.y + FRAME * 2;
        frameSize = Math.max(xFrame, yFrame);

        // Make the square have the lines in center
        if(xFrame == frameSize) topLeftCorner.y -= (xFrame - yFrame) / 2;
        else topLeftCorner.x -= (yFrame - xFrame) / 2;

        if(frameSize > size) frameSize = size;

        Point bottomRightCorner = new Point(topLeftCorner.x + frameSize, topLeftCorner.y + frameSize);
        // Move frame to fit into the overall image
        topLeftCorner.x -= Math.max(0, bottomRightCorner.x - size);
        topLeftCorner.y -= Math.max(0, bottomRightCorner.y - size);
        topLeftCorner.x = Math.max(0, topLeftCorner.x);
        topLeftCorner.y = Math.max(0, topLeftCorner.y);
    }

    Problem view() throws InterruptedException {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(0, 0, size + 20, size + 45);
        window.getContentPane().add(this);
        window.setVisible(true);
        return this;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, size, size);

        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(topLeftCorner.x, topLeftCorner.y, frameSize, frameSize);

        g2.setColor(Color.BLUE);
        for(int i = 1; i < points.size(); i++) {
            g2.drawLine(
                points.get(i - 1).x, points.get(i - 1).y,
                points.get(i).x, points.get(i).y
            );
        }
    }

    BufferedImage toImage() throws IOException {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(getForeground());
        g.setFont(getFont());
        paintAll(g);
        return img.getSubimage(0, 0, getWidth(), getHeight());
    }
}