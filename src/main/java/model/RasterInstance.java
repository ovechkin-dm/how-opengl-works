package model;

public class RasterInstance {

    public Vector w;
    public RasterModel model;
    public Vector translation;
    public Vector rotation;
    public Vector scale;
    public Matrix tm;

    public RasterInstance(RasterModel model,
                          Vector translation,
                          Vector rotation,
                          Vector scale,
                          Vector w) {
        this.model = model;
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
        this.w = w;
    }

    public void compute(Camera camera) {
        tm = MatrixUtils.transform(this, camera);
    }

    public Vector transform(Vector v) {
        Matrix m = Matrix.fromVec(v);
        m = m.resize(4, 1);
        m.mat[3][0] = 1;
        Matrix result = tm.dot(m);
        Vector coords = new Vector(result.mat[0][0], result.mat[1][0], result.mat[2][0]);
        return coords;
    }

}
