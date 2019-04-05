package model;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    public static List<RasterInstance> instances = new ArrayList<RasterInstance>();
    public static List<Light> lights = new ArrayList<Light>();
    public static Camera camera = new Camera(new Vector(0, 0, 0), new Vector(0, 0, 0));

    static {
        RasterModel cube = RasterModel.cube();
        RasterModel sphere = RasterModel.sphere();
        RasterModel triangle = RasterModel.triangle();

        RasterInstance c1 = new RasterInstance(
                cube,
                Vector.create(-1.5, 0, 4),
                Vector.create(0, 0, 0),
                Vector.create(1, 1, 1),
                new Vector());

        RasterInstance c2 = new RasterInstance(
                cube,
                Vector.create(-1.5, 0, 6),
                Vector.create(Math.PI / 6.0, Math.PI / 6.0, Math.PI / 6.0),
                Vector.create(0.75, 0.75, 0.75),
                new Vector(0.025, 0.025 * 1.5, -0.01));

        RasterInstance t1 = new RasterInstance(
                triangle,
                Vector.create(0, 0, 0.9),
                Vector.create(Math.PI / 4.0, 0, 0),
                Vector.create(0.5, 0.5, 0.5),
                new Vector());

        RasterInstance s2 = new RasterInstance(
                sphere,
                Vector.create(1.5, 0, 6),
                Vector.create(Math.PI / 6.0, Math.PI / 6.0, Math.PI / 6.0),
                Vector.create(1, 1, 1),
                new Vector(0.02, -0.02 * 1.5, 0.01));

        instances.add(s2);
        instances.add(c2);
    }

    static {
        Light ambient = new Light("ambient", 0.2, new Vector(0, 0, 0), new Vector(0, 0, 0));
        Light point = new Light("point", 0.5, new Vector(0, 1, 0), new Vector(0, -1, 1));
        Light directional = new Light("directional", 0.0, new Vector(-3, 0, 0), new Vector(3, -3, 3));
        lights.add(ambient);
        lights.add(point);
        lights.add(directional);
    }

}
