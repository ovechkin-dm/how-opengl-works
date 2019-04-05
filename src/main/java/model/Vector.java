package model;

public class Vector {

    final public static Vector[] vecPool = new Vector[1000];
    public static int poolPos = 0;

    static {
        for (int i = 0; i < vecPool.length; i++) {
            vecPool[i] = new Vector();
        }
    }

    public double x;
    public double y;
    public double z;

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector create() {
        return getVec();
    }

    public static Vector create(double x, double y, double z) {
        Vector result = getVec();
        result.x = x;
        result.y = y;
        result.z = z;
        return result;
    }

    private static Vector getVec() {
        /*poolPos = (poolPos + 1) % vecPool.length;
        return vecPool[poolPos];*/
        return new Vector(0, 0, 0);
    }

    public static Vector createOffPool() {
        return new Vector();
    }

    public double dot(Vector other) {
        double result = 0;
        result += this.x * other.x;
        result += this.y * other.y;
        result += this.z * other.z;
        return result;
    }

    public Vector minus(Vector other) {
        Vector result = getVec();
        result.x = this.x - other.x;
        result.y = this.y - other.y;
        result.z = this.z - other.z;
        return result;
    }

    public Vector minus(double other) {
        Vector result = getVec();
        result.x = this.x - other;
        result.y = this.y - other;
        result.z = this.z - other;
        return result;
    }

    public Vector plus(Vector other) {
        Vector result = getVec();
        result.x = this.x + other.x;
        result.y = this.y + other.y;
        result.z = this.z + other.z;
        return result;
    }

    public Vector plus(double other) {
        return minus(-other);
    }

    public Vector mul(double other) {
        Vector result = getVec();
        result.x = this.x * other;
        result.y = this.y * other;
        result.z = this.z * other;
        return result;
    }

    public double len() {
        return Math.sqrt(this.dot(this));
    }

    public Vector div(double other) {
        return mul(1.0 / other);
    }

    public double norm() {
        return Math.sqrt(dot(this));
    }

    public Vector normalize() {
        return div(norm());
    }

    public Vector cross(Vector b) {
        Vector a = this;
        return new Vector(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    public Vector copy() {
        return new Vector(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Double.compare(vector.x, x) != 0) return false;
        if (Double.compare(vector.y, y) != 0) return false;
        return Double.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
