package model;

import java.awt.*;

public class MyColor {

    public double r;
    public double g;
    public double b;

    public MyColor(double r, double g, double b) {
        set(r, g, b);
    }

    public MyColor copy() {
        return new MyColor(r, g, b);
    }

    public Color asJavaColor() {
        return new Color((int) r, (int) g, (int) b);
    }

    public void fromColor(MyColor c) {
        set(c.r, c.g, c.b);
    }

    public void set(double r, double g, double b) {
        this.r = Math.min(r, 255);
        this.g = Math.min(g, 255);
        this.b = Math.min(b, 255);
    }

    public void mul(double x) {
        set(r * x, g * x, b * x);
    }

    public static final MyColor RED = new MyColor(255, 0, 0);
    public static final MyColor GREEN = new MyColor(0, 255, 0);
    public static final MyColor BLUE = new MyColor(0, 0, 255);
    public static final MyColor YELLOW = new MyColor(255, 255, 0);
    public static final MyColor PURPLE = new MyColor(255, 0, 255);
    public static final MyColor CYAN = new MyColor(0, 255, 255);

}
