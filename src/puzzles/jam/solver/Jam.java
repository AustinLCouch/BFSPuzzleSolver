package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.JamConfig;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Main Jam program that takes command line args (filename.txt) to create an initial config and solve it.
 *
 * @author Austin Couch
 */
public class Jam {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }
        else{
            try{
                Solver solver = new Solver();
                List<Configuration> path = new LinkedList<>();
                JamConfig start = new JamConfig(args[0]);
                Optional<List<Configuration>> solved = solver.solve(start);
                if (solved.isPresent()){
                    path = solved.get();
                }
                System.out.println("File: " + args[0]);
                System.out.println(start.display());
                solver.displaySolution(path);
            }
            catch (IOException e){
            }
        }
    }
}