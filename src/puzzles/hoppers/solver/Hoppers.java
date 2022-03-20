package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The main Hoppers program that takes command
 * line args (filename.txt) to create an initial
 * config and solve it.
 *
 * @author Hritik "Ricky" Gupta
 */
public class Hoppers {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }

        Solver solver = new Solver();
        List<Configuration> path = new LinkedList<>();

        HoppersConfig config = new HoppersConfig(args[0]);

        Optional<List<Configuration>> solved = solver.solve(config);

        if (solved.isPresent()) {
            path = solved.get();
        }

        System.out.println("File: " + args[0]);
        System.out.println(config.display());
        solver.displaySolution(path);
    }
}
