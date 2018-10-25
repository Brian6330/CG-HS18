import java.util.Scanner;

public class Plane extends Object3 {

    /**
     * one (arbitrary) point on the plane
     */
    private Vec3 center;

    /**
     * normal vector of the plane
     */
    private Vec3 normal;

    /**
     * Constructs a plane with specified data.
     *
     * @param c {@link #center}
     * @param n {@link #normal}
     */
    public Plane(Vec3 c, Vec3 n) {
        center = c != null ? c : new Vec3(0);
        normal = n != null ? n : new Vec3(0, 1, 0);
    }

    /**
     * Constructs a plane with specified scanner
     *
     * @param scn the scanner to be read
     */
    public Plane(Scanner scn) {
        read(scn);
    }

    @Override
    public boolean intersect(Ray ray,
                             Value<Vec3> intersection_point,
                             Value<Vec3> intersection_normal,
                             Value<Double> intersection_t) {
        final double dn = Vec3.dot(ray.getDirection(), normal);

        if (Math.abs(dn) > Double.MIN_VALUE) {
            final double t = Vec3.dot(normal, center.minus(ray.getOrigin())) / dn;
            if (t > 0) {
                intersection_t.set(t);
                intersection_point.set(ray.at(t));
                intersection_normal.set(normal);
                return true;
            }
        }

        return false;
    }

    @Override
    public void read(Scanner scn) {
        this.center = new Vec3(scn);
        this.normal = new Vec3(scn);
        getMaterial().read(scn);
    }

    /**
     * @return {@link #center}
     */
    public Vec3 getCenter() {
        return center;
    }

    /**
     * @return {@link #normal}
     */
    public Vec3 getNormal() {
        return normal;
    }
}
