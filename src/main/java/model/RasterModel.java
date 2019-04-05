package model;

import clipping.BackFaceCulling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RasterModel {

    public Vector[] vertices;
    public Triangle[] triangles;
    public String name;

    public RasterModel(Vector[] vertices,
                       Triangle[] triangles,
                       String name) {
        this.vertices = vertices;
        this.triangles = triangles;
        this.name = name;
        BackFaceCulling.reorderTrianglePoints(vertices, triangles);
    }

    public static RasterModel cube() {
        Vector[] v = new Vector[]{
                Vector.create(1, 1, 1),
                Vector.create(-1, 1, 1),
                Vector.create(-1, -1, 1),
                Vector.create(1, -1, 1),
                Vector.create(1, 1, -1),
                Vector.create(-1, 1, -1),
                Vector.create(-1, -1, -1),
                Vector.create(1, -1, -1),
        };
        Triangle[] triangles = new Triangle[]{
                new Triangle(0, 1, 2, MyColor.RED),
                new Triangle(0, 2, 3, MyColor.RED),
                new Triangle(4, 0, 3, MyColor.GREEN),
                new Triangle(4, 3, 7, MyColor.GREEN),
                new Triangle(5, 4, 7, MyColor.BLUE),
                new Triangle(5, 7, 6, MyColor.BLUE),
                new Triangle(1, 5, 6, MyColor.YELLOW),
                new Triangle(1, 6, 2, MyColor.YELLOW),
                new Triangle(4, 5, 1, MyColor.PURPLE),
                new Triangle(4, 1, 0, MyColor.PURPLE),
                new Triangle(2, 6, 7, MyColor.CYAN),
                new Triangle(2, 7, 3, MyColor.CYAN),

        };
        return new RasterModel(v, triangles, "cube");
    }

    public static RasterModel triangle() {
        Vector[] v = new Vector[]{
                Vector.create(-1, 0, 0),
                Vector.create(0, 1, 0),
                Vector.create(1, 0, 0),
        };
        Triangle[] triangles = new Triangle[]{
                new Triangle(0, 1, 2, MyColor.RED, new Vector(1, 0, 1))
        };
        return new RasterModel(v, triangles, "triangle");
    }

    public static RasterModel sphere() {
        int idx = 1;
        int numPatches = 15;
        HashMap<Vector, Integer> mapping = new HashMap<Vector, Integer>();
        List<Triangle> triangles = new ArrayList<Triangle>();
        mapping.put(new Vector(0, 0, 0), 0);
        for (int i = 1; i <= numPatches; i++) {
            double cury = (i / (double) numPatches - 0.5) * 2.0;
            double prevy = ((i - 1) / (double) numPatches - 0.5) * 2.0;

            double curr = Math.sin(Math.acos(cury));
            double prevr = Math.sin(Math.acos(prevy));

            for (int j = 1; j <= numPatches * 2 ; j++) {
                double angle = Math.PI / numPatches * ((double) j);
                double prevangle = Math.PI / numPatches * ((double) j - 1);

                double curx = Math.sin(angle);
                double curz = Math.cos(angle);

                double prevx = Math.sin(prevangle);
                double prevz = Math.cos(prevangle);

                Vector lower1 = new Vector(curx * curr, cury, curz * curr);
                Vector lower2 = new Vector(prevx * curr, cury, prevz * curr);

                Vector upper1 = new Vector(curx * prevr, prevy, curz * prevr);
                Vector upper2 = new Vector(prevx * prevr, prevy, prevz * prevr);
                List<Vector> ptemp = new ArrayList<Vector>();
                ptemp.add(lower1);
                ptemp.add(lower2);
                ptemp.add(upper1);
                ptemp.add(upper2);
                for (Vector v : ptemp) {
                    if (!mapping.containsKey(v)) {
                        mapping.put(v, idx);
                        idx += 1;
                    }
                }
                int l1id = mapping.get(lower1);
                int l2id = mapping.get(lower2);

                int u1id = mapping.get(upper1);
                int u2id = mapping.get(upper2);
                if (l1id == l2id) {
                    triangles.add(new Triangle(l1id, u1id, u2id, MyColor.GREEN));
                } else if (u1id == u2id) {
                    triangles.add(new Triangle(u1id, l1id, l2id, MyColor.GREEN));
                } else {
                    triangles.add(new Triangle(l1id, u2id, u1id, MyColor.GREEN));
                    triangles.add(new Triangle(l1id, u2id, l2id, MyColor.GREEN));
                }
            }
        }
        Triangle[] arr = triangles.toArray(new Triangle[0]);
        Vector[] points = new Vector[idx];
        for (Map.Entry<Vector, Integer> e : mapping.entrySet()) {
            points[e.getValue()] = e.getKey();
        }
        return new RasterModel(points, arr, "sphere");

    }

}
