import model.Constants;
import model.MyColor;

import java.awt.*;

public class MyCanvas {

    private final int minx;
    private final int maxx;
    private final int miny;
    private final int maxy;
    private int width;
    private int height;
    private Graphics2D graphics;

    public MyCanvas(int width, int height) {
        assert (width % 2 == 0);
        assert (height % 2 == 0);
        this.width = width;
        this.height = height;
        this.minx = -width / 2;
        this.maxx = width / 2;
        this.miny = -height / 2;
        this.maxy = height / 2;
    }

    public void putPixel(int x, int y, MyColor color) {
        x += width / 2;
        y += height / 2;
        y = height - y;
        if (x >= width || y >= height || x < 0 || y < 0) {
            return;
        }
        graphics.setColor(color.asJavaColor());
        graphics.fillRect((int) (x * Constants.scale), (int) (y * Constants.scale),
                (int) (Constants.scale) + 1, (int) (Constants.scale) + 1);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMinx() {
        return minx;
    }

    public int getMaxx() {
        return maxx;
    }

    public int getMiny() {
        return miny;
    }

    public int getMaxy() {
        return maxy;
    }


    public void setGraphics(Graphics2D graphics) {
        this.graphics = graphics;
    }
}
