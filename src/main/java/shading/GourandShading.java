package shading;

import model.Light;
import model.Triangle;
import model.Vector;

import java.util.List;

public class GourandShading {

    public static void applyShading(List<Light> lights,
                                    Vector[] points,
                                    List<Triangle> triangles) {
        for (Triangle t : triangles) {
            t.shades.x = 0;
            t.shades.y = 0;
            t.shades.z = 0;
            for (Light l : lights) {
                shade(l, points, t);
            }
        }
    }

    private static void shade(Light light, Vector[] points, Triangle triangle) {
        Vector p1 = points[triangle.p1];
        Vector p2 = points[triangle.p2];
        Vector p3 = points[triangle.p3];
        Vector n = p2.minus(p1).cross(p2.minus(p3)).normalize();
        triangle.shades.x += compute(light, p1, n);
        triangle.shades.y += compute(light, p2, n);
        triangle.shades.z += compute(light, p3, n);
    }

    static double compute(Light light, Vector point, Vector n) {
        double i = 0;
        double specular = 0.8;
        if (light.getType().equals("ambient")) {
            i += light.getIntensity();
        } else {
            Vector l = Vector.create(0, 0, 0);
            double tmax = 0.0;
            if (light.getType().equals("point")) {
                l = point.minus(light.getPosition());
                tmax = 1.0;
            } else if (light.getType().equals("directional")) {
                l = light.getDirection();
                tmax = 10000000000.0;
            }
            //diffuse
            double ndl = n.dot(l);
            if (ndl > 0) {
                i += light.getIntensity() * ndl / (n.norm() * l.norm());
            }
            //specular
            Vector r = n.mul(2.0).mul(n.dot(l)).minus(l);
            double cos = r.dot(point) / (r.norm() * point.norm());
            if (cos > 0) {
                i += light.getIntensity() * Math.pow(cos, specular);
            }
        }
        return i;
    }

}
