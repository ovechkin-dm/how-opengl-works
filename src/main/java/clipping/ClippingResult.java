package clipping;

import model.Triangle;
import model.Vector;

import java.util.ArrayList;
import java.util.List;

public class ClippingResult {

    public static final ClippingResult EMPTY = new ClippingResult(new ArrayList<Vector>(),
            new ArrayList<Triangle>());

    public List<Vector> points;
    public List<Triangle> triangles;

    public ClippingResult(List<Vector> vertices, List<Triangle> triangles) {
        this.points = vertices;
        this.triangles = triangles;
    }
}
