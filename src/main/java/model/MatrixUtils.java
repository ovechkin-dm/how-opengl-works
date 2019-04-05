package model;

public class MatrixUtils {

    public static Matrix yRotationMatrix(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double[][] m = {
                {cos, 0, sin},
                {0, 1, 0},
                {-sin, 0, cos}
        };
        return new Matrix(m);
    }

    public static Matrix zRotationMatrix(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double[][] m = {
                {cos, -sin, 0},
                {sin, cos, 0},
                {0, 0, 1}
        };
        return new Matrix(m);
    }

    public static Matrix xRotationMatrix(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double[][] m = {
                {1, 0, 0},
                {0, cos, -sin},
                {0, sin, cos}
        };
        return new Matrix(m);
    }

    public static Matrix scaleMatrix(Vector scale) {
        Matrix result = new Matrix(4, 4);
        result.mat[0][0] = scale.x;
        result.mat[1][1] = scale.y;
        result.mat[2][2] = scale.z;
        result.mat[3][3] = 1;
        return result;
    }

    public static Matrix translationMatrix(Vector v) {
        Matrix result = new Matrix(4, 4);
        result.mat[0][0] = 1;
        result.mat[1][1] = 1;
        result.mat[2][2] = 1;
        result.mat[3][3] = 1;

        result.mat[0][3] = v.x;
        result.mat[1][3] = v.y;
        result.mat[2][3] = v.z;

        return result;
    }

    public static Matrix rotationMatrix(Vector v) {
        Matrix r = MatrixUtils.xRotationMatrix(v.x);
        Matrix p = MatrixUtils.yRotationMatrix(v.y);
        Matrix y = MatrixUtils.zRotationMatrix(v.z);
        Matrix rotation = y.dot(p.dot(r));
        rotation = rotation.resize(4, 4);
        rotation.mat[3][3] = 1;
        return rotation;
    }


    public static Matrix transform(RasterInstance instance, Camera camera) {

        Matrix ir = rotationMatrix(instance.rotation);
        Matrix is = scaleMatrix(instance.scale);
        Matrix it = translationMatrix(instance.translation);
        /*The inverse of a translation matrix is the translation matrix with the opposite signs on each
        of the translation components.
        The inverse of a rotation matrix is the rotation matrix's transpose.
        The inverse of a matrix product is the product of the inverse matrices ordered in reverse.*/

        Matrix ct = translationMatrix(new Vector(
                -camera.position.x,
                -camera.position.y,
                -camera.position.z)
        );

        Matrix cr = rotationMatrix(camera.rotation).transpose();
        return cr.dot(ct.dot(it.dot(is.dot(ir))));
    }

}
