import java.util.Scanner;

public class Cylinder extends Object3 {

	/**
	 * venter position
	 */
	private Vec3 center;

	/**
	 * unit axis vector
	 */
	private Vec3 axis;

	/**
	 * radius
	 */
	private double radius;

	/**
	 * height
	 */
	private double height;

	/**
	 * Constructs a cylinder with specified data.
	 *
	 * @param c {@link #center}
	 * @param r {@link #radius}
	 * @param a {@link #axis}
	 * @param h {@link #height}
	 */
	public Cylinder(final Vec3 c, double r, final Vec3 a, double h) {
		this.center = c != null ? c : new Vec3(0);
		this.radius = r != 0 ? r : 1;
		this.axis = a != null ? a : new Vec3(1, 0, 0);
		this.height = h != 0 ? h : 1;
	}

	/**
	 * Constructs a cylinder with specified stream.
	 * The stream will be reduced.
	 *
	 * @param scn the stream to be read
	 */
	public Cylinder(Scanner scn) {
		read(scn);
	}

	@Override
	public boolean intersect(Ray ray,
	                         Value<Vec3> intersection_point,
	                         Value<Vec3> intersection_normal,
	                         Value<Double> intersection_t) {
		final Vec3 dir = ray.getDirection();
		final Vec3 oc = ray.getOrigin().minus(center);

		final double dir_parallel = Vec3.dot(axis, dir),
				oc_parallel = Vec3.dot(axis, oc);

		final double[] t = new double[2];
		final int nsol = SolveQuadratic.solveQuadratic(
				Vec3.dot(dir, dir) - dir_parallel * dir_parallel,
				2.0 * (Vec3.dot(dir, oc) - dir_parallel * oc_parallel),
				Vec3.dot(oc, oc) - oc_parallel * oc_parallel - radius * radius,
				t);

		// find the closest valid solution
		// (in front of the viewer and within the cylinder's height)
		intersection_t.setValue(NO_INTERSECTION);
		for (int i = 0; i < nsol; i++) {
			if (t[i] <= 0) continue;
			double z = Vec3.dot(ray.direction(t[i]).minus(center), axis);
			if (2.0 * Math.abs(z) < height)
				intersection_t.setValue(Math.min(intersection_t.getValue(), t[i]));
		}
		if (intersection_t.getValue() == NO_INTERSECTION) return false;

		// compute intersection data
		intersection_point.setValue(ray.direction(intersection_t.getValue()));
		intersection_normal.setValue(
				intersection_point.getValue()
						.minus(center)
						.divide(radius));
		intersection_normal.setValue(
				intersection_normal.getValue()
						.minus(axis.multiply(
								Vec3.dot(intersection_normal.getValue(), axis))));

		// choose the normal's orientation to be the opposite the ray's
		// (in case the ray intersects the inside surface)
		if (Vec3.dot(intersection_normal.getValue(), dir) > 0)
			intersection_normal.setValue(Vec3.invert(intersection_normal.getValue()));

		return true;
	}

	@Override
	public void read(Scanner scn) {
		this.center = new Vec3(scn);
		this.radius = scn.nextDouble();
		this.axis = new Vec3(scn);
		this.height = scn.nextDouble();
		getMaterial().read(scn);
	}

	/**
	 * @return {@link #center}
	 */
	public Vec3 getCenter() {
		return center;
	}

	/**
	 * @return {@link #axis}
	 */
	public Vec3 getAxis() {
		return axis;
	}

	/**
	 * @return {@link #radius}
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @return {@link #height}
	 */
	public double getHeight() {
		return height;
	}
}
