import java.util.Scanner;

public class Ray {

    /**
     * origin of the ray
     */
    private Vec3 origin;

    /**
     * at of the ray (should be normalized)
     */
    private Vec3 direction;

    /**
     * Constructs a ray with specified data. The at will be normalized.
     *
     * @param o {@link #origin}
     * @param d {@link #direction}
     */
    public Ray(Vec3 o, Vec3 d) {
        this.origin = o;
        this.direction = Vec3.normalize(d);
    }

    /**
     * Constructs a ray with specified scanner.
     *
     * @param scn the scanner to be read
     */
    public Ray(Scanner scn) {
        read(scn);
    }

    /**
     * Reads ray components from the scanner stream to the specified ray.
     *
     * @param scn the scanner to be read
     * @param r   the ray to be created
     * @return the reduced scanner
     */
    public static Scanner read(Scanner scn, Value<Ray> r) {
        r.set(new Ray(scn));
        return scn;
    }

    /**
     * Computes the point on the ray at the parameter t.
     *
     * @param t the distance to be travelled along this ray's at
     * @return the resulting point
     */
    public Vec3 at(double t) {
        return origin.plus(direction.multiply(t));
    }

    /**
     * @return {@link #origin}
     */
    public Vec3 getOrigin() {
        return origin;
    }

    /**
     * @return {@link #direction}
     */
    public Vec3 getDirection() {
        return direction;
    }

    /**
     * Reads ray components from the specified scanner to this ray.
     *
     * @param scn the scanner to be read
     * @return the reduced scanner
     */
    public Scanner read(Scanner scn) {
        this.origin = new Vec3(scn);
        this.direction = new Vec3(scn);
        return scn;
    }
}
