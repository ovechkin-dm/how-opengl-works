import clipping.BackFaceCulling;
import clipping.ClippingResult;
import clipping.ClippingWorker;
import model.*;
import shading.GouraudShading;

import java.util.ArrayList;
import java.util.List;

public class RasterScene {

    private MyCanvas canvas;
    Vector viewPortParams = new Vector(0.5, 0.5, 1);
    ClippingWorker clippingWorker = new ClippingWorker(viewPortParams);
    double[][] depthBuffer = new double[Constants.SC_W + 1][Constants.SC_H + 1];
    double step = 0;
    Vector camViewcenter = new Vector(0, 0, 6);
    double camPos = 0;

    public RasterScene(MyCanvas canvas) {
        this.canvas = canvas;
    }

    public void draw() {
        step += 1;
        Vector camVec = getCameraVec();
        Vector src = camViewcenter.minus(camVec);
        ModelUtils.camera.rotation = RotationUtils.toAngle(src);
        ModelUtils.camera.position = camVec;
        for (RasterInstance instance : ModelUtils.instances) {
            instance.compute(ModelUtils.camera);
            instance.rotation = instance.rotation.plus(instance.w);
            renderObject(instance);
        }
        camPos += 0.01;
    }

    public Vector getCameraVec() {
        double x = Math.sin(camPos) * 7.0 + camViewcenter.x;
        double z = Math.cos(camPos) * 7.0 + camViewcenter.z;
        double y = 4;
        return new Vector(x, y, z);
    }

    private void renderObject(RasterInstance rasterInstance) {
        RasterModel model = rasterInstance.model;
        Vector[] transformed = new Vector[model.vertices.length];
        for (int i = 0; i < transformed.length; i++) {
            transformed[i] = rasterInstance.transform(model.vertices[i]);
        }
        List<Triangle> frontTriangles = backFaceCull(rasterInstance.model.triangles, transformed);
        GouraudShading.applyShading(ModelUtils.lights, transformed, frontTriangles);
        ClippingResult clippingResult = clippingWorker.clip(transformed, frontTriangles);
        Vector[] projected = new Vector[clippingResult.points.size()];
        for (int i = 0; i < projected.length; i++) {
            projected[i] = projectVertex(clippingResult.points.get(i));
        }
        for (Triangle t : clippingResult.triangles) {
            renderTriangle(t, projected);
        }
    }

    private List<Triangle> backFaceCull(Triangle[] triangles, Vector[] transformed) {
        List<Triangle> result = new ArrayList<Triangle>();
        for (Triangle t: triangles) {
            if (BackFaceCulling.shouldRender(transformed, t)) {
                result.add(t);
            }
        }
        return result;
    }

    private void renderTriangle(Triangle t, Vector[] projected) {
        /*drawFrameTriangle(projected[t.p1], projected[t.p2], projected[t.p3], t.color);*/
        drawFilledTriangle(projected[t.p1], projected[t.p2], projected[t.p3],
                t.shades.x, t.shades.y, t.shades.z, t.color);
    }

    private Vector viewportToCanvas(Vector v) {
        double x = v.x * Constants.SC_W / viewPortParams.x / 2.0;
        double y = v.y * Constants.SC_H / viewPortParams.y / 2.0;
        return Vector.create(x, y, v.z);
    }

    private Vector projectVertex(Vector v) {
        Vector proj = Vector.create(v.x * viewPortParams.z / v.z, v.y * viewPortParams.z / v.z, 1 / v.z);
        return viewportToCanvas(proj);
    }


    private void drawFrameTriangle(Vector p1, Vector p2, Vector p3, MyColor color) {
        drawLine(p1, p2, color);
        drawLine(p2, p3, color);
        drawLine(p3, p1, color);
    }

    private void drawFilledTriangle(Vector p1, Vector p2, Vector p3,
                                    double p1h, double p2h, double p3h,
                                    MyColor color) {
        if (p1.y > p2.y) {
            Vector tmp = p1;
            p1 = p2;
            p2 = tmp;
            double tmph = p1h;
            p1h = p2h;
            p2h = tmph;
        }
        if (p2.y > p3.y) {
            Vector tmp = p2;
            p2 = p3;
            p3 = tmp;
            double tmph = p2h;
            p2h = p3h;
            p3h = tmph;
        }
        if (p1.y > p2.y) {
            Vector tmp = p1;
            p1 = p2;
            p2 = tmp;
            double tmph = p1h;
            p1h = p2h;
            p2h = tmph;
        }
        double[] x12 = interpolate(p1.x, p2.x, p1.y, p2.y);
        double[] x23 = interpolate(p2.x, p3.x, p2.y, p3.y);
        double[] x13 = interpolate(p1.x, p3.x, p1.y, p3.y);

        double[] z12 = interpolate(p1.z, p2.z, p1.y, p2.y);
        double[] z23 = interpolate(p2.z, p3.z, p2.y, p3.y);
        double[] z13 = interpolate(p1.z, p3.z, p1.y, p3.y);

        double[] h12 = interpolate(p1h, p2h, p1.y, p2.y);
        double[] h23 = interpolate(p2h, p3h, p2.y, p3.y);
        double[] h13 = interpolate(p1h, p3h, p1.y, p3.y);

        double[] concath = new double[x13.length];
        double[] concat = new double[x13.length];
        double[] concatz = new double[x13.length];
        int cnt = 0;
        for (int i = 0; i < x12.length - 1; i++) {
            concat[cnt] = x12[i];
            concath[cnt] = h12[i];
            concatz[cnt] = z12[i];
            cnt += 1;
        }
        for (int i = 0; i < x23.length; i++) {
            concat[cnt] = x23[i];
            concath[cnt] = h23[i];
            concatz[cnt] = z23[i];
            cnt += 1;
        }
        int m = concat.length / 2;
        double[] xleft = concat;
        double[] xright = x13;

        double[] hleft = concath;
        double[] hright = h13;

        double[] zleft = concatz;
        double[] zright = z13;

        if (x13[m] < concat[m]) {
            xleft = x13;
            xright = concat;

            hleft = h13;
            hright = concath;

            zleft = z13;
            zright = concatz;

        }
        for (int i = (int) p1.y; i < (int) p3.y - Constants.EPS; i++) {
            int idx = i - ((int) p1.y);
            double[] lineh = interpolate(hleft[idx], hright[idx], xleft[idx], xright[idx]);
            double[] linez = interpolate(zleft[idx], zright[idx], xleft[idx], xright[idx]);
            for (int j = (int) xleft[idx]; j < (int) xright[idx]; j++) {
                double h = lineh[j - (int) xleft[idx]];
                double z = linez[j - (int) xleft[idx]];
                putPixel(j, i, z, h, color);
            }
        }
    }

    private void drawLine(Vector p1, Vector p2, MyColor color) {
        if (Math.abs(p1.x - p2.x) > Math.abs(p1.y - p2.y)) {
            if (p1.x > p2.x) {
                Vector tmp = p1;
                p1 = p2;
                p2 = tmp;
            }
            double[] ys = interpolate(p1.y, p2.y, p1.x, p2.x);
            for (int i = (int) p1.x; i <= (int) p2.x; i++) {
                canvas.putPixel(i, (int) ys[i - (int) p1.x], color);
            }
        } else {
            if (p1.y > p2.y) {
                Vector tmp = p1;
                p1 = p2;
                p2 = tmp;
            }
            double[] xs = interpolate(p1.x, p2.x, p1.y, p2.y);
            for (int i = (int) p1.y; i <= (int) p2.y; i++) {
                int xcoord = (int) xs[i - (int) p1.y];
                int ycoord = i;
                canvas.putPixel(xcoord, ycoord, color);
            }
        }
    }

    void putPixel(int x, int y, double z, double h, MyColor color) {
        MyColor canvasPixel = new MyColor(0, 0, 0);
        if (depthBuffer.length <= x + Constants.SC_W / 2 + 1 || depthBuffer[0].length <= y + Constants.SC_H / 2 + 1) {
            return;
        }
        if (x < -Constants.SC_W / 2 || y < -Constants.SC_H / 2) {
            return;
        }
        if (z + step > depthBuffer[x + Constants.SC_W / 2 + 1][y + Constants.SC_H / 2 + 1]) {
            canvasPixel.fromColor(color);
            canvasPixel.mul(Math.max(h, 0));
            depthBuffer[x + Constants.SC_W / 2 + 1][y + Constants.SC_H / 2 + 1] = z + step;
            canvas.putPixel(x, y, canvasPixel);
        }
    }

    private double[] interpolate(double d0, double d1, double i0, double i1) {
        if (i0 > i1) {
            return new double[]{(int) d0};
        }
        if (Math.abs(i1 - i0) < Constants.EPS) {
            return new double[]{(int) d0};
        }
        double a = (d1 - d0) / (i1 - i0);
        int i = (int) i0;
        int ie = (int) i1;
        int size = (ie - i);
        double[] result = new double[size + 1];
        double prev = d0;
        for (int k = 0; k <= size; k++) {
            result[k] = prev;
            prev += a;
        }
        return result;
    }


}
