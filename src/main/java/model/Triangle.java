package model;

public class Triangle {

    public int p1;
    public int p2;
    public int p3;
    public MyColor color;
    public Vector shades;

    public Triangle(int p1,
                    int p2,
                    int p3,
                    MyColor color) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.color = color;
        this.shades = new Vector(1, 0, 0);
    }

    public Triangle(int p1, int p2, int p3, MyColor color, Vector shades) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.color = color;
        this.shades = shades;
    }

    public double getShadowByIndex(int idx) {
        if (p1 == idx) {
            return shades.x;
        }
        if (p2 == idx) {
            return shades.y;
        }
        if (p3 == idx) {
            return shades.z;
        }
        throw new RuntimeException("index not found");
    }

}
