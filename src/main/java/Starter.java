import model.Constants;

import javax.swing.*;
import java.awt.*;

public class Starter extends JPanel {

    MyCanvas canvas = new MyCanvas(Constants.SC_W, Constants.SC_H);
    RasterScene scene = new RasterScene(canvas);

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, (int) (Constants.SC_W * Constants.scale) + 1,
                (int) (Constants.SC_H * Constants.scale) + 1);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        canvas.setGraphics(g2d);
        scene.draw();
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Scene");
        Starter game = new Starter();
        game.setDoubleBuffered(true);
        frame.add(game);
        frame.setSize((int) (Constants.SC_W * Constants.scale + 1), (int) (Constants.SC_H * Constants.scale + 1));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            game.repaint();
            Thread.sleep(10);
        }
    }
}
