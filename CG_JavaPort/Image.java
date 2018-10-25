package CG_JavaPort;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

public class Image {

	/** vector with all pixels in the image */
	private Vector<Value<Vec3>> pixels;

	/** image width in pixels */
	private int width;

	/** image height in pixels */
	private int height;

	/**
	 * Constructs an image of size width * height.
	 * @param w {@link #width}
	 * @param h {@link #height}
	 */
	public Image(int w, int h) {
		assert w >= 0;
		assert h >= 0;
		resize(w, h);
	}

	/**
	 * Resizes the image
	 * @param w {@link #width}
	 * @param h {@link #height}
	 */
	public void resize(int w, int h) {
		this.width = w;
		this.height = h;
		pixels = new Vector<>(width * height);
		for (int i=0; i<width*height; i++) pixels.add(new Value<>());
	}

	/**
	 * Read accesss to pixel at specified position.
	 * @param x the x position
	 * @param y the y position
	 * @return the stored pixel
	 */
	public Value<Vec3> get(int x, int y) {
		assert x < width;
		assert y < width;
		int index = y*width + x;
		return pixels.get(y*width + x);
	}

	/**
	 * Writes the image in TGA format to a file.
	 * @param filename the filename to save the image to
	 * @return {@code true} after completion
	 * @throws IOException
	 */
	public boolean write(String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));

			outStream.write(0); // id length
			outStream.write(0); // no color map
			outStream.write(2); // uncompressed image
			outStream.write(0); // offset color map table
			outStream.write(0); //
			outStream.write(0); // number of entries
			outStream.write(0); //
			outStream.write(0); // bits per pixel
			outStream.write(0); // abs coordinate lower left display in x direction
			outStream.write(0); //
			outStream.write(0); // abs coordinate lower left display in y direction
			outStream.write(0); //
			outStream.write(width & 0x00FF);    // width in pixels
			outStream.write((width & 0xFF00) / 256);    //
			outStream.write(height & 0x00FF);   // height in pixels
			outStream.write((height & 0xFF00) / 256);   //
			outStream.write(24);    // bits per pixel
			outStream.write(0); // image descriptor

			for (Value<Vec3> color : pixels) {
				outStream.write((char) (255.0 * color.getValue().get(2)));
				outStream.write((char) (255.0 * color.getValue().get(1)));
				outStream.write((char) (255.0 * color.getValue().get(0)));
			}

			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Can't write Image to file <" + filename + ")");
		}
		return true;
	}

	/**
	 * @return {@link #width}
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return {@link #width}
	 */
	public int getHeight() {
		return height;
	}
}
