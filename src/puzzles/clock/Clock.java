package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The main Clock program that takes command
 * line args to create an initial config and
 * solve it.
 *
 * @author Hritik "Ricky" Gupta
 */
public class Clock {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start stop");
        }

        Solver solver = new Solver();
        List<Configuration> path = new LinkedList<>();
        ClockConfig config = new ClockConfig(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2])
        );

        Optional<List<Configuration>> solved = solver.solve(config);

        if (solved.isPresent()) {
            path = solved.get();
        }

        System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
        solver.displaySolution(path);
    }
}