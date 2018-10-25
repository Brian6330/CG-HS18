import java.util.Scanner;

public class Material {

    /**
     * ambient color
     */
    private Vec3 ambient;

    /**
     * diffuse color
     */
    private Vec3 diffuse;

    /**
     * specular color
     */
    private Vec3 specular;

    /**
     * shininess factor
     */
    private double shininess;

    /**
     * reflectivity factor (1 = perfect mirror, 0 = no reflection)
     */
    private double mirror;

    public Material() {
    }

    /**
     * Constructs a material with specified data.
     *
     * @param a  {@link #ambient}
     * @param d  {@link #diffuse}
     * @param s  {@link #specular}
     * @param sn {@link #shininess}
     * @param m  {@link #mirror}
     */
    public Material(Vec3 a, Vec3 d, Vec3 s, double sn, double m) {
        this.ambient = a;
        this.diffuse = d;
        this.specular = s;
        this.shininess = sn;
        this.mirror = m;
    }

    /**
     * Constructs a material with specified scanner.
     *
     * @param scn the scanner to be read
     */
    public Material(Scanner scn) {
        read(scn);
    }

    /**
     * Reads material components from the specified scanner to the specified material.
     *
     * @param scn the scanner to be read
     * @param m   the material to be created
     * @return the reduced scanner
     */
    public static Scanner read(Scanner scn, Value<Material> m) {
        m.set(new Material(scn));
        return scn;
    }

    /**
     * Reads material components from the specified scanner to this material.
     *
     * @param scn the scanner to be read
     * @return the reduced scanner
     */
    public Scanner read(Scanner scn) {
        this.ambient = new Vec3(scn);
        this.diffuse = new Vec3(scn);
        this.specular = new Vec3(scn);
        this.shininess = scn.nextDouble();
        this.mirror = scn.nextDouble();
        return scn;
    }

    /**
     * @return {@link #ambient}
     */
    public Vec3 getAmbient() {
        return ambient;
    }

    /**
     * @return {@link #diffuse}
     */
    public Vec3 getDiffuse() {
        return diffuse;
    }

    /**
     * @return {@link #specular}
     */
    public Vec3 getSpecular() {
        return specular;
    }

    /**
     * @return {@link #shininess}
     */
    public double getShininess() {
        return shininess;
    }

    /**
     * @return {@link #mirror}
     */
    public double getMirror() {
        return mirror;
    }
}
