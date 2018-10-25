package CG_JavaPort;

import java.util.Scanner;

public class Camera {

	/**
	 * position of the eye in 3D space (camera center)
	 */
	private Vec3 eye;

	/**
	 * the center of the scene the camera is looking at
	 */
	private Vec3 center;

	/**
	 * up-direction of the camera
	 */
	private Vec3 up;

	/**
	 * opening angle (field of view) in y-direction
	 */
	private double fovy;

	/**
	 * image width in pixels
	 */
	private int width;

	/**
	 * image height in pixels
	 */
	private int height;

	private Vec3 x_dir, y_dir, lower_left;

	public Camera() {
	}

	/**
	 * Constructs a camera with specified data.
	 *
	 * @param e {@link #eye}
	 * @param c {@link #center}
	 * @param u {@link #up}
	 * @param f {@link #fovy}
	 * @param w {@link #width}
	 * @param h {@link #height}
	 */
	public Camera(Vec3 e, Vec3 c, Vec3 u, double f, int w, int h) {
		this.eye = e;
		this.center = c;
		this.up = u;
		this.fovy = f;
		this.width = w;
		this.height = h;
		init();
	}

	/**
	 * Constructs a camera with specified stream.
	 *
	 * @param scn the stream to be read
	 */
	public Camera(Scanner scn) {
		read(scn);
	}

	/**
	 * This function precomputes some variables that are later required for {@link #primary_ray(int, int)} ’.}
	 */
	private void init() {
		// compute viewing direction and distance of eye to scene center
		Vec3 view = Vec3.normalize(center.minus(eye));
		double dist = Vec3.distance(center, eye);

		// compute width & height of the image plane
		// based on the opening angle of the camera (fovy) and the distance
		// of the eye to the near plane (dist)
		double image_height = 2.0 * dist * Math.tan(0.5 * fovy / 180.0 * Math.PI);
		double image_width = (double) width / height * image_height;

		// compute right and up vectors on the image plane
		x_dir = Vec3.normalize(Vec3.cross(view, up)).multiply(image_width).divide(width);
		y_dir = Vec3.normalize(Vec3.cross(x_dir, view)).multiply(image_height).divide(height);

		// compute lower left corner on the image plane
		lower_left = center.minus(x_dir.multiply(.5f * width)).minus(y_dir.multiply(.5f * height));
	}

	/**
	 * Creates a ray for a pixel in the image.
	 *
	 * @param x the pixel location in image
	 * @param y the pixel location in image
	 * @return the resulting ray
	 */
	public Ray primary_ray(int x, int y) {
		return new Ray(eye,
				lower_left.plus(
						x_dir.multiply(x)).plus(
								y_dir.multiply(y)).minus(
										eye));
	}

	/**
	 * @return {@link #eye}
	 */
	public Vec3 getEye() {
		return eye;
	}

	/**
	 * @return {@link #center}
	 */
	public Vec3 getCenter() {
		return center;
	}

	/**
	 * @return {@link #up}
	 */
	public Vec3 getUp() {
		return up;
	}

	/**
	 * @return {@link #fovy}
	 */
	public double getFovy() {
		return fovy;
	}

	/**
	 * @return {@link #width}
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return {@link #height}
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Reads camera components from the specified DataInputStream to this camera.
	 * @param scn the stream to be read
	 * @return the reduced stream
	 */
	public Scanner read(Scanner scn) {
		this.eye = new Vec3(scn);
		this.center = new Vec3(scn);
		this.up = new Vec3(scn);
		this.fovy = scn.nextDouble();
		this.width = scn.nextInt();
		this.height = scn.nextInt();
		init();
		return scn;
	}

	/**
	 * Reads camera components from the specified DataInputStream to the specified camera.
	 * @param scn the stream to be read
	 * @param c the camera to be created
	 * @return the reduced stream
	 */
	public Scanner read(Scanner scn, Value<Camera> c) {
		c.setValue(new Camera(scn));
		return scn;
	}
}
