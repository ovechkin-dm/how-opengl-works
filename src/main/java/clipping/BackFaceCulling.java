package clipping;

import model.Matrix;
import model.Triangle;
import model.Vector;

public class BackFaceCulling {

    public static void reorderTrianglePoints(Vector[] vertices,
                                             Triangle[] triangles) {
        for (Triangle t : triangles) {
            reorderTriangle(vertices, t);
        }
    }

    private static void reorderTriangle(Vector[] vertices, Triangle triangle) {
        Vector p1 = vertices[triangle.p1];
        Vector p2 = vertices[triangle.p2];
        Vector p3 = vertices[triangle.p3];
        double det = computeDet(p1, p2, p3);
        if (det < 0) {
            int tmp = triangle.p1;
            triangle.p1 = triangle.p2;
            triangle.p2 = tmp;
        }
    }

    public static boolean shouldRender(Vector[] vertices, Triangle triangle) {
        Vector p1 = vertices[triangle.p1];
        Vector p2 = vertices[triangle.p2];
        Vector p3 = vertices[triangle.p3];
        return computeDet(p1, p2, p3) < 0;
    }

    public static double computeDet(Vector p1, Vector p2, Vector p3) {
        double[][] m = new double[][]{
                {p1.x, p1.y, p1.z, 1},
                {p2.x, p2.y, p2.z, 1},
                {p3.x, p3.y, p3.z, 1},
                {0, 0, 0, 1},
        };
        return new Matrix(m).det();
    }

}
