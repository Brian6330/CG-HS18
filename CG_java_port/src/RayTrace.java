import java.util.Vector;

public class RayTrace {

    public static void main(String[] args) {
        Vector<RayTraceJob> jobs = new Vector<>(1);

        if (args.length == 2)
            jobs.add(new RayTraceJob(args[0], args[1]));
        else if (args.length == 1 && "0".equals(args[0])) {
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/spheres/spheres.sce", "spheres.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/cylinders/cylinders.sce", "cylinders.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/spheres/spheres.sce", "spheres.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/cylinders/cylinders.sce", "cylinders.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/combo/combo.sce", "combo.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/molecule/molecule.sce", "molecule.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/molecule2/molecule2.sce", "molecule2.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/cube/cube.sce", "cube.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/mask/mask.sce", "mask.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/mirror/mirror.sce", "mirror.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/toon_faces/toon_faces.sce", "toon_faces.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/office/office.sce", "office.tga"));
            jobs.add(new RayTraceJob(System.getProperty("user.dir") + "/scenes/rings/rings.sce", "rings.tga"));
        } else {
            System.out.println("Usage: <input.sce> <output.tga>\nOr: \"0\"");
            System.exit(1);
        }

        for (final RayTraceJob job : jobs) {
            System.out.println("Read scene '" + job.scenePath + "'...");
            Scene s = new Scene(job.scenePath);
            System.out.println("done (" + s.numObjects() + " objects)");

            StopWatch timer = new StopWatch();
            System.out.println("Ray tracing...");
            timer.start();
            var image = s.render();
            timer.stop();
            System.out.println("done (" + timer + ")");

            System.out.println("Write image...");
            image.write(job.outPath);
            System.out.println("done");
        }
    }

    private static class RayTraceJob {
        private String scenePath, outPath;

        private RayTraceJob(String s, String o) {
            this.scenePath = s;
            this.outPath = o;
        }
    }
}
