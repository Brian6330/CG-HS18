import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Scene {

    public double SHADOW_RAY_OFFSET = 1e-5;
    public double REFLECTION_RAY_OFFSET = 1e-5;
    /**
     * camera stores eye position, view at, and can generate primary rays
     */
    private Camera camera;
    /**
     * array for all lights in the scene
     */
    private Vector<Light> lights = new Vector<>();
    /**
     * array for all the objects in the scene
     */
    private Vector<Object3> objects = new Vector<>();
    /**
     * max recursion depth for mirroring
     */
    private int max_depth = 0;
    /**
     * background color
     */
    private Vec3 background = new Vec3(0);
    /**
     * global ambient light
     */
    private Vec3 ambience = new Vec3(0);

    /**
     * Constructor loads scene from file.
     *
     * @param path file to be loaded
     */
    public Scene(String path) {
        try {
            read(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't read in Scene with path <" + path + ">");
        }
    }

    /**
     * Allocate image and raytrace the scene.
     *
     * @return the resulting image
     */
    public Image render() {
        // allocate new image
        Image img = new Image(camera.getWidth(), camera.getHeight());

        // function rendering a full column of the image
        ThreadScheduler.Function raytraceColumn = (int x) -> {
            for (int y = 0; y < camera.getHeight(); y++) {
                Ray ray = camera.primary_ray(x, y);

                // compute color by tracing this ray
                Vec3 color = trace(ray, 0);

                // avoid over-saturation
                color = Vec3.min(color, new Vec3(1));

                assert x < camera.getWidth();
                // store pixel color
                img.get(x, y).set(color);
            }
        };

        ThreadScheduler scheduler = new ThreadScheduler();
        scheduler.createThreads(raytraceColumn, camera.getWidth());
        scheduler.start();
        scheduler.waitForFinish();

        // no copy, real object
        return img;
    }

    /**
     * Determines the color seen by a viewing ray.
     *
     * @param ray   the ray to be viewed from
     * @param depth how many times the 'ray' had been reflected. Goes from 0 to max_depth. Should be used for recursive function call.
     * @return the resulting color
     */
    public Vec3 trace(final Ray ray, final int depth) {
        // stop if recursion depth (=number of reflection) is too large)
        if (depth > max_depth) return new Vec3(0);

        // Find first intersection with an object. If an intersection is found,
        // it is stored in object, point, normal and t.
        Value<Object3> object = new Value<>();
        Value<Vec3> point = new Value<>();
        Value<Vec3> normal = new Value<>();
        Value<Double> t = new Value<>();

        if (!intersect(ray, object, point, normal, t)) return background;

        // compute local Phong lighting
        Vec3 color = lighting(point.get(), normal.get(), Vec3.invert(ray.getDirection()), object.get().getMaterial());

        // recursive call to collect color from reflections
        if (object.get().getMaterial().getMirror() > 0.0 && depth < max_depth) {
            Vec3 refl_dir = Vec3.reflect(ray.getDirection(), normal.get());
            Ray reflected_ray = new Ray(point.get().plus(refl_dir.multiply(REFLECTION_RAY_OFFSET)), refl_dir);
            double mirror = object.get().getMaterial().getMirror();

            // linear interpolation of reflected and current color
            color = color.multiply(1.0 - mirror).plus(trace(reflected_ray, depth + 1).multiply(mirror));

            //! simple addition is wrong!:
            // color *= trace(reflected_ray, depth+1).multiply(mirror);
        }

        return color;
    }

    /**
     * Computes the closest intersection point between a ray and all objects in the scene.
     *
     * @param ray    the ray that should be tested for intersections with all objects in the scene
     * @param object output parameter which holds the object from the scene, intersected by the ray, closest to the ray's origin
     * @param point  OUTPUT: the resulting intersection point
     * @param normal OUTPUT: the resulting normal at 'point'
     * @param _t     OUTPUT: distance between the ray's origin and 'point'
     * @return {@code true}, if there is an intersection point between ray and at least one object in the scene.
     */
    public boolean intersect(final Ray ray, Value<Object3> object, Value<Vec3> point, Value<Vec3> normal, Value<Double> _t) {
        Value<Double> t = new Value<>();
        Value<Double> tmin = new Value<>(Object3.NO_INTERSECTION);
        Value<Vec3> p = new Value<>();
        Value<Vec3> n = new Value<>();

        for (final Object3 o : objects) {
            if (o.intersect(ray, p, n, t)) {
                if (t.get() < tmin.get()) {
                    tmin.set(t.get());
                    object.set(o);
                    point.set(p.get());
                    normal.set(n.get());
                    _t.set(t.get());
                }
            }
        }

        return (tmin.get() != Object3.NO_INTERSECTION);
    }

    /**
     * Computes the Phong lighting for a given object intersection.
     *
     * @param point    the point, whose color should be determined
     * @param normal   the point's normal
     * @param view     normalized at from the point to the viewer's position
     * @param material holds material parameters of the point, that should be lit
     * @return the resulting lighting
     */
    public Vec3 lighting(final Vec3 point, final Vec3 normal, final Vec3 view, final Material material) {
        Vec3 color = ambience.multiply(material.getAmbient());

        // loop over each light source
        for (final Light l : lights) {
            // compute light at and distance from light source
            Vec3 light_direction = Vec3.normalize(l.getPosition().minus(point));
            double light_distance = Vec3.distance(l.getPosition(), point);

            // point in shadow? shoot shadow-ray
            Ray shadow_ray = new Ray(point.plus(light_direction.multiply(SHADOW_RAY_OFFSET)), light_direction);

            Value<Vec3> p = new Value<>();
            Value<Vec3> n = new Value<>();
            Value<Double> t = new Value<>();
            Value<Object3> o = new Value<>();
            if (intersect(shadow_ray, o, p, n, t) && (t.get() < light_distance)) continue;

            // add light source's diffuse term
            double NL = Vec3.dot(light_direction, normal);
            if (NL > 0.0) {
                color = color.plus(l.getColor().multiply(material.getDiffuse()).multiply(NL));

                // specular term
                double RV = Vec3.dot(view, Vec3.mirror(light_direction, normal));
                if (RV > 0.0)
                    color = color.plus(
                            l.getColor().multiply(material.getSpecular()))
                            .multiply(Math.pow(RV, material.getShininess()));
            }
        }

        return color;
    }

    /**
     * Reads a scene from the specified filename.
     *
     * @param filename the file to be read
     */
    public void read(String filename) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot open file <" + filename + ">");
        }

        final Scanner scn = new Scanner(fis).useDelimiter("\\s+");

        final Map<String, Runnable> entityParser = new HashMap<>(9, 1f);
        entityParser.put("depth", () -> max_depth = scn.nextInt());
        entityParser.put("camera", () -> camera = new Camera(scn));
        entityParser.put("background", () -> background = new Vec3(scn));
        entityParser.put("ambience", () -> ambience = new Vec3(scn));
        entityParser.put("light", () -> lights.add(new Light(scn)));
        entityParser.put("plane", () -> objects.add(new Plane(scn)));
        entityParser.put("sphere", () -> objects.add(new Sphere(scn)));
        entityParser.put("cylinder", () -> objects.add(new Cylinder(scn)));
        entityParser.put("mesh", () -> objects.add(new Mesh(scn)));

        Value<String> token = new Value<>("#") {
            @Override
            public void read(Scanner scn) {
                set(scn.next());
                System.out.println("Token: " + get());
            }
        };
        while (scn.hasNext()) {
            token.read(scn);
            if (token.get().charAt(0) == '#') {
                scn.nextLine();
                continue;
            }
            if (!entityParser.containsKey(token.get()))
                throw new RuntimeException("Invalid token encountered: " + token.get());
            entityParser.get(token.get()).run();
        }

        scn.close();
    }

    /**
     * @return number of objects
     */
    public int numObjects() {
        return objects.size();
    }

    /**
     * @return {@link #objects}
     */
    public Vector<Object3> getObjects() {
        return objects;
    }

    /**
     * @return {@link #camera}
     */
    public Camera getCamera() {
        return camera;
    }
}
