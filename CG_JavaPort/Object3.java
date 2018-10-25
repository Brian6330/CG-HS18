package CG_JavaPort;

import java.util.Scanner;

public abstract class Object3 {

    public static final double NO_INTERSECTION = Double.MAX_VALUE;

    /** the material of this object */
    private Material material = new Material();

    /**
     * Intersect the object with specified ray. Return whether there is an intersection.
     * If ray intersects the object, provide the following results:
     * @param ray the ray to intersect the object with
     * @param intersection_point OUTPUT: the point of intersection
     * @param intersection_normal OUTPUT: the surface normal at intersection point
     * @param intersection_t OUTPUT: ray parameter at intersection point
     * @return if intersection occurs
     */
    public abstract boolean intersect(final Ray ray,
                                      Value<Vec3> intersection_point,
                                      Value<Vec3> intersection_normal,
                                      Value<Double> intersection_t);

    /**
     * Parses object properties from specified stream.
     * @param scn the stream to be read
     */
    public void read(Scanner scn) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * @return {@link #material}
     */
    public Material getMaterial() {
        return this.material;
    }
}
