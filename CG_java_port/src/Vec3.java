import java.util.Scanner;

public class Vec3 {

	private final double[] data = new double[3];

	public Vec3() {
	}

	public Vec3(double s) {
		this(s, s, s);
	}

	public Vec3(double x, double y, double z) {
		data[0] = x;
		data[1] = y;
		data[2] = z;
	}

	public Vec3(double[] d) {
		this(d[0], d[1], d[2]);
	}

	public Vec3(Scanner scn) {
		read(scn);
	}

	/**
	 * Inverts the specified vector.
	 *
	 * @return the resulting vector
	 */
	public static Vec3 invert(Vec3 v) {
		return new Vec3(-v.data[0], -v.data[1], -v.data[2]);
	}

	/**
	 * Adds both specified vectors.
	 *
	 * @param v0 a vector
	 * @param v1 a vector
	 * @return the resulting vector
	 */
	public static Vec3 add(Vec3 v0, Vec3 v1) {
		return v0.plus(v1);
	}

	/**
	 * Subtracts the specified second vector from the first specified vector
	 *
	 * @param v0 a vector
	 * @param v1 the vector to be subtracted
	 * @return the resulting vector
	 */
	public static Vec3 subtract(Vec3 v0, Vec3 v1) {
		return v0.minus(v1);
	}

	/**
	 * Multiplies both specified vectors component-wise.
	 *
	 * @param v0 a vector
	 * @param v1 a vector
	 * @return te resulting vector
	 */
	public static Vec3 multiply(Vec3 v0, Vec3 v1) {
		return v0.multiply(v1);
	}

	/**
	 * Multiplies the specified vector with the specified scalar.
	 *
	 * @param v a vector
	 * @param s the scalar to be multiplied with
	 * @return the resulting vector
	 */
	public static Vec3 multiply(Vec3 v, double s) {
		return v.multiply(s);
	}

	/**
	 * Divides the specified vector with the specified scalar.
	 *
	 * @param v a vector
	 * @param s the scalar to be divided with
	 * @return the resulting vector
	 */
	public static Vec3 divide(Vec3 v, double s) {
		return v.divide(s);
	}

	/**
	 * Computes the component-wise minimum of both specified vectors.
	 *
	 * @param v0 a vector
	 * @param v1 a vector
	 * @return the resulting vector
	 */
	public static Vec3 min(Vec3 v0, Vec3 v1) {
		return new Vec3(
				Math.min(v0.data[0], v1.data[0]),
				Math.min(v0.data[1], v1.data[1]),
				Math.min(v0.data[2], v1.data[2])
		);
	}

	/**
	 * Computes the component-wise maximum of both specified vectors.
	 *
	 * @param v0 a vector
	 * @param v1 a vector
	 * @return the resulting vector
	 */
	public static Vec3 max(Vec3 v0, Vec3 v1) {
		return new Vec3(
				Math.max(v0.data[0], v1.data[0]),
				Math.max(v0.data[1], v1.data[1]),
				Math.max(v0.data[2], v1.data[2])
		);
	}

	/**
	 * Computes the Euclidian dot product of both specified vectors.
	 *
	 * @param v0 a vector
	 * @param v1 a vector
	 * @return the resulting value
	 */
	public static double dot(Vec3 v0, Vec3 v1) {
		return v0.data[0] * v1.data[0] + v0.data[1] * v1.data[1] + v0.data[2] * v1.data[2];
	}

	/**
	 * Computes the Euclidian norm of the specified vector.
	 *
	 * @param v the vector to be normed
	 * @return the resulting value
	 */
	public static double norm(Vec3 v) {
		return Math.sqrt(dot(v, v));
	}

	/**
	 * Normalizes the specified vector.
	 *
	 * @param v the vector to be normalized
	 * @return the resulting vector
	 */
	public static Vec3 normalize(Vec3 v) {
		final double n = norm(v);
		if (n != 0.0)
			return new Vec3(v.data[0] / n,
					v.data[1] / n,
					v.data[2] / n);
		return v;
	}

	/**
	 * Computes the distance between both specified vectors.
	 *
	 * @param v0 a vector
	 * @param v1 a vector
	 * @return the resulting value
	 */
	public static double distance(Vec3 v0, Vec3 v1) {
		return norm(v0.minus(v1));
	}

	/**
	 * Calculates the cross product of both specified vectors.
	 *
	 * @param v0 a vector
	 * @param v1 a vector
	 * @return the resulting vector
	 */
	public static Vec3 cross(Vec3 v0, Vec3 v1) {
		return new Vec3(v0.data[1] * v1.data[2] - v0.data[2] * v1.data[1],
				v0.data[2] * v1.data[0] - v0.data[0] * v1.data[2],
				v0.data[0] * v1.data[1] - v0.data[1] * v1.data[0]);
	}

	/**
	 * Reflects the specified vector at specified normal.
	 *
	 * @param v the vector to be reflected
	 * @param n the normal to be used
	 * @return the resulting vector
	 */
	public static Vec3 reflect(Vec3 v, Vec3 n) {
		return v.minus(n.multiply(2.0 * dot(n, v)));
	}

	/**
	 * Mirrors the specified vector at specified normal.
	 *
	 * @param v the vector to be mirrored
	 * @param n the normal to be used
	 * @return the resulting vector
	 */
	public static Vec3 mirror(Vec3 v, Vec3 n) {
		return n.multiply(2.0 * dot(n, v)).minus(v);
	}

	/**
	 * Reads vector components from the specified stream to the specified vector.
	 * The specified stream will be reduced.
	 *
	 * @param scn the stream to be read
	 * @param v   the vector to be created
	 * @return the reduced stream
	 */
	public static Scanner read(Scanner scn, Value<Vec3> v) {
		v.setValue(new Vec3(scn));
		return scn;
	}

	/**
	 * Appends a String representation of the specified vector to the specified StringBuilder.
	 *
	 * @param s the StringBuilder to be appended to
	 * @param v the resulting StringBuilder
	 * @return the resulting StringBuilder
	 */
	public static StringBuilder writeTo(StringBuilder s, Vec3 v) {
		return v.writeTo(s);
	}

	/**
	 * Returns the i-th component of this vector.
	 *
	 * @param i the index
	 * @return the i-th component
	 */
	public double get(int i) {
		return data[i];
	}

	/**
	 * Returns a copy of this vector' components as an array.
	 *
	 * @return the data
	 */
	public double[] getData() {
		return new double[]{data[0], data[1], data[2]};
	}

	/**
	 * Adds the specified vector to this vector.
	 *
	 * @param v the vector to be added
	 * @return the resulting vector
	 */
	public Vec3 plus(Vec3 v) {
		Vec3 r = new Vec3(this.data);
		for (int i = 0; i < 3; i++) r.data[i] += v.data[i];
		return r;
	}

	/**
	 * Subtracts the specified vector from this vector.
	 *
	 * @param v the vector to be subtracted
	 * @return the resulting vector
	 */
	public Vec3 minus(Vec3 v) {
		Vec3 r = new Vec3(this.data);
		for (int i = 0; i < 3; i++) r.data[i] -= v.data[i];
		return r;
	}

	/**
	 * Multiplies the specified vector to this vector component-wise.
	 *
	 * @param v the vector to be multiplied with
	 * @return the resulting vector
	 */
	public Vec3 multiply(Vec3 v) {
		Vec3 r = new Vec3(this.data);
		for (int i = 0; i < 3; i++) r.data[i] *= v.data[i];
		return r;
	}

	/**
	 * Multiplies this vector with the specified scalar.
	 *
	 * @param s the scalar to be multiplied with
	 * @return the resulting vector
	 */
	public Vec3 multiply(double s) {
		Vec3 r = new Vec3(this.data);
		for (int i = 0; i < 3; i++) r.data[i] *= s;
		return r;
	}

	/**
	 * Divides this vector with the specified scalar.
	 *
	 * @param s the scalar to be divided with
	 * @return the resulting vector
	 */
	public Vec3 divide(double s) {
		Vec3 r = new Vec3(this.data);
		for (int i = 0; i < 3; i++) r.data[i] /= s;
		return r;
	}

	/**
	 * Reads vector components from the specified stream to this vector.
	 * The specified stream will be reduced.
	 *
	 * @param scn the stream to be read
	 * @return the reduced stream
	 */
	public Scanner read(Scanner scn) {
		this.data[0] = scn.nextDouble();
		this.data[1] = scn.nextDouble();
		this.data[2] = scn.nextDouble();
		return scn;
	}

	/**
	 * Appends a String representation of this vector to the specified StringBuilder.
	 *
	 * @param s the StringBuilder to be appended to
	 * @return the resulting StringBuilder
	 */
	public StringBuilder writeTo(StringBuilder s) {
		return s.append(toPrettyString());
	}

	public String toPrettyString() {
		return '(' + this.data[0] + ", " + this.data[1] + ", " + this.data[2] + ')';
	}
}
