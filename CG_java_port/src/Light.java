import java.util.Scanner;

public class Light {

    /**
     * position of the light source
     */
    private Vec3 position;

    /**
     * color of the light source
     */
    private Vec3 color;

    public Light() {
    }

    /**
     * Constructs a light with specified data.
     *
     * @param p {@link #position}
     * @param c {@link #color}
     */
    public Light(Vec3 p, Vec3 c) {
        this.position = p;
        this.color = c;
    }

    /**
     * Constructs a light with specified scanner.
     *
     * @param scn the scanner to be read
     */
    public Light(Scanner scn) {
        read(scn);
    }

    /**
     * Reads light components from the specified scanner to the specified light.
     *
     * @param scn the scanner to be read
     * @param l   the light to be created
     * @return the reduced scanner
     */
    public static Scanner read(Scanner scn, Value<Light> l) {
        l.set(new Light(scn));
        return scn;
    }

    /**
     * Reads light components from the specified scanner to this light.
     *
     * @param scn the scanner to be read
     * @return the reduced scanner
     */
    public Scanner read(Scanner scn) {
        this.position = new Vec3(scn);
        this.color = new Vec3(scn);
        return scn;
    }

    /**
     * @return {@link #position}
     */
    public Vec3 getPosition() {
        return position;
    }

    /**
     * @return {@link #color}
     */
    public Vec3 getColor() {
        return color;
    }
}
