package CG_JavaPort;

public class SolveQuadratic {

    /**
     * Numerically robust solution to (possibly degenerate) quadratic equations.
     * Avoids catastrophic calcellation and handles equations that have degenerated to become linear or constant.
     * @param a coefficient of x²
     * @param b coefficient of x
     * @param c coefficient
     * @param solns array to hold between 0 and 2 solutions. Must be of length 2
     * @return number of solutions found
     */
    public static int solveQuadratic(double a, double b, double c, final double[] solns) {
        assert solns.length == 2;

        // handle degenerate (linear) case
        if (Math.abs(a) < 1e-10) {
            if (Math.abs(b) < 1e-10) return 0;
            solns[0] = - c/b;
            return 1;
        }

        double discriminant = b*b - 4*a*c;
        if (discriminant < 0) return 0;

        // Avoid cancellation:
        // One solution doesn't suffer cancellation:
        //      a * x1 = 1 / 2 [-b - bSign * sqrt(b^2 - 4ac)]
        // "x2" can be found from the fact:
        //      a * x1 * x2 = c
        double a_x1 = -0.5 * (b + Math.copySign(Math.sqrt(discriminant), b));

        solns[0] = a_x1 / a;
        solns[1] = c / a_x1;
        return 2;
    }
}
