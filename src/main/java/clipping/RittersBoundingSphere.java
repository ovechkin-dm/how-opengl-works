package clipping;

import model.Constants;
import model.Vector;

import java.util.List;

public class RittersBoundingSphere {

    public static BoundingSphere getBoundingSphere(List<Vector> points) {
        Vector x = points.get(0);
        Vector y = getLargestDistancePoint(x, points);
        Vector z = getLargestDistancePoint(y, points);
        Vector pos = y.plus(z).div(2);
        double r = y.minus(z).len() / 2.0;
        for (int i = 0; i < points.size(); i++) {
            Vector p = points.get(i);
            if (p.minus(pos).len() > r + Constants.EPS) {
                Vector back = pos.minus(p).normalize().mul(r);
                pos = back.plus(p).div(2.0);
                r = p.minus(back).len() / 2.0;
            }
        }
        return new BoundingSphere(r, pos);
    }

    private static Vector getLargestDistancePoint(Vector p, List<Vector> points) {
        double maxDist = -1;
        Vector largest = points.get(0);
        for (int i = 0; i < points.size(); i++) {
            double dist = p.minus(points.get(i)).len();
            if (dist > maxDist) {
                maxDist = dist;
                largest = points.get(i);
            }
        }
        return largest;
    }


    public static class BoundingSphere {
        double radius;
        Vector center;

        public BoundingSphere(double radius, Vector center) {
            this.radius = radius;
            this.center = center;
        }
    }

}
