package model;

public class Matrix {

    public double[][] mat;

    public Matrix(int rows, int cols) {
        mat = new double[rows][cols];
    }

    public Matrix(double[][] mat) {
        this.mat = mat;
    }

    Matrix dot(Matrix other) {
        assert (this.getCols() == other.getRows());
        Matrix result = new Matrix(this.getRows(), other.getCols());
        for (int r = 0; r < result.getRows(); r++) {
            for (int c = 0; c < result.getCols(); c++) {
                for (int k = 0; k < this.getCols(); k++) {
                    result.mat[r][c] += this.mat[r][k] * other.mat[k][c];
                }
            }
        }
        return result;
    }

    Matrix transpose() {
        Matrix result = new Matrix(this.getCols(), this.getRows());
        for (int r = 0; r < result.getRows(); r++) {
            for (int c = 0; c < result.getCols(); c++) {
                result.mat[r][c] = this.mat[c][r];
            }
        }
        return result;
    }

    public int getRows() {
        return mat.length;
    }

    public int getCols() {
        return mat[0].length;
    }

    public Vector toVec() {
        if (mat.length == 1 && mat[0].length == 3) {
            return new Vector(mat[0][0], mat[0][1], mat[0][2]);
        } else if (mat.length == 3 && mat[0].length == 1) {
            return new Vector(mat[0][0], mat[1][0], mat[2][0]);
        }
        throw new RuntimeException("dims doesn't match");
    }

    public Matrix resize(int rows, int cols) {
        Matrix result = new Matrix(rows, cols);
        for (int r = 0; r < Math.min(mat.length, rows); r++) {
            for (int c = 0; c < Math.min(mat[0].length, cols); c++) {
                if (r < result.mat.length && c < result.mat[0].length
                        && r < this.mat.length && c < this.mat[0].length) {
                    result.mat[r][c] = this.mat[r][c];
                }
            }
        }
        return result;
    }

    public static Matrix fromVec(Vector v) {
        Matrix m = new Matrix(3, 1);
        m.mat[0][0] = v.x;
        m.mat[1][0] = v.y;
        m.mat[2][0] = v.z;
        return m;
    }

    public double det() {
        return detInner(this);
    }

    public static double detInner(Matrix m) {
        if (m.getRows() != m.getCols()) {
            throw new IllegalArgumentException("not a square matrix");
        }
        if (m.getRows() == 1) {
            return m.mat[0][0];
        }
        double sign = 1;
        double result = 0;
        for (int i = 0; i < m.mat[0].length; i++) {
            double a = m.mat[0][i];
            result += sign * a * detInner(matWithoutRowAndCol(m, 0, i));
            sign = -sign;
        }
        return result;
    }

    public static Matrix matWithoutRowAndCol(Matrix m, int row, int col) {
        Matrix result = new Matrix(m.getRows() - 1, m.getCols() - 1);
        int rr = 0;
        for (int r = 0; r < m.getRows(); r++) {
            if (r == row) {
                continue;
            }
            int rc = 0;
            for (int c = 0; c < m.getCols(); c++) {
                if (c == col) {
                    continue;
                }
                result.mat[rr][rc] = m.mat[r][c];
                rc += 1;
            }
            rr += 1;
        }
        return result;
    }

}
