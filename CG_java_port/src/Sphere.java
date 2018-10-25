import java.util.Scanner;

public class Sphere extends Object3 {

    /**
     * center position of the sphere
     */
    private Vec3 center;

    /**
     * radius of the sphere
     */
    private double radius;

    /**
     * Constructs a sphere with specified data
     *
     * @param c {@link #center}
     * @param r {@link #radius}
     */
    public Sphere(final Vec3 c, double r) {
        this.center = c != null ? c : new Vec3(0);
        this.radius = r != 0 ? r : 1;
    }

    /**
     * Constructs a sphere with specified stream.
     * The stream will be reduced.
     *
     * @param scn the stream to be read.
     */
    public Sphere(Scanner scn) {
        read(scn);
    }

    @Override
    public boolean intersect(Ray ray,
                             Value<Vec3> intersection_point,
                             Value<Vec3> intersection_normal,
                             Value<Double> intersection_t) {
        final Vec3 dir = ray.getDirection();
        final Vec3 oc = ray.getOrigin().minus(center);

        final double[] t = new double[2];
        int nsol = SolveQuadratic.solve(
                Vec3.dot(dir, dir),
                2 * Vec3.dot(dir, oc),
                Vec3.dot(oc, oc) - radius * radius,
                t);

        intersection_t.set(NO_INTERSECTION);

        // find the closest valid solution (in front of the viewer)
        for (int i = 0; i < nsol; i++) {
            if (t[i] > 0)
                intersection_t.set(Math.min(intersection_t.get(), t[i]));
        }

        if (intersection_t.get() == NO_INTERSECTION) return false;

        intersection_point.set(ray.at(intersection_t.get()));
        intersection_normal.set(
                intersection_point.get()
                        .minus(center)
                        .divide(radius));

        return true;
    }

    @Override
    public void read(Scanner scn) {
        this.center = new Vec3(scn);
        this.radius = scn.nextDouble();
        getMaterial().read(scn);
    }

    /**
     * @return {@link #center}
     */
    public Vec3 getCenter() {
        return center;
    }

    /**
     * @return {@link #radius}
     */
    public double getRadius() {
        return radius;
    }
}
