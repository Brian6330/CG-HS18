import java.util.Scanner;

public class Ray {

	/**
	 * origin of the ray
	 */
	private Vec3 origin;

	/**
	 * direction of the ray (should be normalized)
	 */
	private Vec3 direction;

	/**
	 * Constructs a ray with specified data. The direction will be normalized.
	 *
	 * @param o {@link #origin}
	 * @param d {@link #direction}
	 */
	public Ray(Vec3 o, Vec3 d) {
		this.origin = o;
		this.direction = Vec3.normalize(d);
	}

	/**
	 * Constructs a ray with specified stream. The stream will be reduced.
	 *
	 * @param scn the stream to be read
	 */
	public Ray(Scanner scn) {
		read(scn);
	}

	/**
	 * Reads ray components from the specified stream to the specified ray.
	 * The specified stream will be reduced.
	 *
	 * @param scn the stream to be read
	 * @param r   the ray to be created
	 * @return the reduced stream
	 */
	public static Scanner read(Scanner scn, Value<Ray> r) {
		r.setValue(new Ray(scn));
		return scn;
	}

	/**
	 * Computes the point on the ray at the parameter t.
	 *
	 * @param t the distance to be travelled along this ray's direction
	 * @return the resulting point
	 */
	public Vec3 direction(double t) {
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
	 * Reads ray components from the specified stream to this ray.
	 * The specified stream will be reduced.
	 *
	 * @param scn the stream to be read
	 * @return the reduced stream
	 */
	public Scanner read(Scanner scn) {
		this.origin = new Vec3(scn);
		this.direction = new Vec3(scn);
		return scn;
	}
}
