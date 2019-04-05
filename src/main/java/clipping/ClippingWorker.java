package clipping;

import model.Constants;
import model.Plane;
import model.Triangle;
import model.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClippingWorker {

    private Vector viewPortParams;
    private List<Plane> planes;

    public ClippingWorker(Vector viewPortParams) {
        this.viewPortParams = viewPortParams;
        this.planes = createClippingPlanes();
    }

    public ClippingResult clip(Vector[] pointsArr, List<Triangle> trianglesArr) {
        List<Vector> points = new ArrayList<Vector>(Arrays.asList(pointsArr));
        List<Triangle> triangles = new ArrayList<Triangle>(trianglesArr);
        RittersBoundingSphere.BoundingSphere sphere = RittersBoundingSphere.getBoundingSphere(points);
        for (Plane plane : planes) {
            double distance = plane.n.dot(sphere.center) + plane.d;
            if (distance < 0 && Math.abs(distance) > sphere.radius) {
                return ClippingResult.EMPTY;
            }
        }
        ClippingResult result = new ClippingResult(points, triangles);
        for (Plane plane : planes) {
            clipForPlane(result, plane);
        }
        return result;
    }

    private void clipForPlane(ClippingResult result, Plane plane) {
        List<Triangle> toAdd = new ArrayList<Triangle>();
        for (Triangle triangle : result.triangles) {
            List<Integer> inside = new ArrayList<Integer>(3);
            List<Integer> outside = new ArrayList<Integer>(3);
            List<Integer> pidx = new ArrayList<Integer>(3);

            pidx.add(triangle.p1);
            pidx.add(triangle.p2);
            pidx.add(triangle.p3);
            for (int pid : pidx) {
                Vector p = result.points.get(pid);
                double dist = plane.n.dot(p) + plane.d;
                if (dist < 0) {
                    outside.add(pid);
                } else {
                    inside.add(pid);
                }

            }
            double[][] intersections = new double[2][2];
            for (int i = 0; i < outside.size(); i++) {
                int out = outside.get(i);
                for (int j = 0; j < inside.size(); j++) {
                    int in = inside.get(j);
                    Vector b = result.points.get(out);
                    Vector a = result.points.get(in);
                    double tnum = -plane.d - plane.n.dot(a);
                    double tdenom = plane.n.dot(b.minus(a));
                    double t = tnum / tdenom;
                    intersections[j][i] = t;
                }
            }
            if (outside.size() == 1) {
                int pid = outside.get(0);
                Vector out = result.points.get(pid);
                Vector in1 = result.points.get(inside.get(0));
                Vector in2 = result.points.get(inside.get(1));
                double t1 = intersections[0][0];
                double t2 = intersections[1][0];
                Vector i1 = in1.plus(out.minus(in1).mul(t1)).plus(Constants.EPS);
                Vector i2 = in2.plus(out.minus(in2).mul(t2)).plus(Constants.EPS);
                int idx1 = result.points.size();
                int idx2 = result.points.size() + 1;
                result.points.add(i1);
                result.points.add(i2);
                Triangle triangle1 = new Triangle(inside.get(0), inside.get(1), idx1, triangle.color);
                Triangle triangle2 = new Triangle(inside.get(1), idx1, idx2, triangle.color);
                double outs = triangle.getShadowByIndex(pid);
                double in1s = triangle.getShadowByIndex(inside.get(0));
                double in2s = triangle.getShadowByIndex(inside.get(1));
                double s1 = in1s + t1 * (outs - in1s);
                double s2 = in2s + t2 * (outs - in2s);
                triangle1.shades.x = in1s;
                triangle1.shades.y = in2s;
                triangle1.shades.z = s1;

                triangle2.shades.x = in2s;
                triangle2.shades.y = s1;
                triangle2.shades.z = s2;

                toAdd.add(triangle1);
                toAdd.add(triangle2);
            } else if (outside.size() == 2) {
                int outid1 = outside.get(0);
                int outid2 = outside.get(1);
                int inid = inside.get(0);
                Vector out1 = result.points.get(outid1);
                Vector out2 = result.points.get(outid2);
                Vector in = result.points.get(inid);
                double t1 = intersections[0][0];
                double t2 = intersections[0][1];
                Vector i1 = in.plus(out1.minus(in).mul(t1)).plus(Constants.EPS);
                Vector i2 = in.plus(out2.minus(in).mul(t2)).plus(Constants.EPS);
                int idx1 = result.points.size();
                int idx2 = result.points.size() + 1;
                result.points.add(i1);
                result.points.add(i2);

                Triangle triangle1 = new Triangle(inid, idx1, idx2, triangle.color);
                double ins = triangle.getShadowByIndex(inid);
                double out1s = triangle.getShadowByIndex(outid1);
                double out2s = triangle.getShadowByIndex(outid2);

                double s1 = ins + t1 * (out1s - ins);
                double s2 = ins + t2 * (out2s - ins);
                triangle1.shades.x = ins;
                triangle1.shades.y = s1;
                triangle1.shades.z = s2;

                toAdd.add(triangle1);
            } else if (inside.size() == 3){
                toAdd.add(triangle);
            }
        }
        result.triangles = toAdd;
    }

    private List<Plane> createClippingPlanes() {
        List<Plane> result = new ArrayList<Plane>();
        Vector origin = new Vector();
        Vector leftBottom = new Vector(-viewPortParams.x, -viewPortParams.y, viewPortParams.z);
        Vector leftTop = new Vector(-viewPortParams.x, viewPortParams.y, viewPortParams.z);
        Vector rightBottom = new Vector(viewPortParams.x, -viewPortParams.y, viewPortParams.z);
        Vector rightTop = new Vector(viewPortParams.x, viewPortParams.y, viewPortParams.z);

        Plane left = fromThreePoints(leftBottom, origin, leftTop);
        Plane right = fromThreePoints(rightTop, origin, rightBottom);
        Plane bottom = fromThreePoints(rightBottom, origin, leftBottom);
        Plane top = fromThreePoints(leftTop, origin, rightTop);
        Plane front = fromThreePoints(leftBottom, rightBottom, rightTop);

        result.add(left);
        result.add(right);
        result.add(bottom);
        result.add(top);
        result.add(front);
        return result;
    }

    private Plane fromThreePoints(Vector a, Vector b, Vector c) {
        Vector n = b.minus(a).cross(c.minus(a)).normalize();
        double d = -n.dot(a);
        return new Plane(n, d);
    }

}
