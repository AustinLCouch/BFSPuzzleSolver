package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Plain-text version of Hoppers puzzle.
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */
public class HoppersPTUI implements Observer<HoppersModel, HoppersClientData> {

    /** model in MVC pattern to be used by the view and controller */
    private HoppersModel model;

    public HoppersPTUI(HoppersConfig config) {
        this.model = new HoppersModel(config);
        this.model.addObserver(this);
        update(this.model, null);
    }

    /**
     * Reads a command and executes corresponding
     * method from model.
     */
    public void run() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("Game command: ");
            String fields = in.nextLine();
            String[] input = fields.split("\\s+");
            if (input.length > 0) {
                if (input[0].startsWith("q")) {
                    break;

                } else if (input[0].startsWith("h")) {
                    this.model.hint();
                    displayMessage();

                } else if (input[0].startsWith("l")) {
                    this.model.load(input[1]);
                    displayMessage();

                } else if (input[0].startsWith("s")) {
                    int startRow = Integer.parseInt(input[1]);
                    int startCol = Integer.parseInt(input[2]);
                    if (!this.model.isPresent(startRow, startCol)) {
                        displayMessage();
                        continue;
                    }
                    System.out.print("Destination: ");
                    input = in.nextLine().split("\\s+");
                    this.model.select(startRow, startCol, Integer.parseInt(input[0]), Integer.parseInt(input[1]));
                    displayMessage();

                } else if (input[0].startsWith("r")) {
                    this.model.reset();
                    displayMessage();

                } else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * Prints on standard output help for the game,
     * including commands.
     */
    public void displayHelp() {
        System.out.println(
                        """ 
                        h(int)              -- hint next move
                        l(oad) filename     -- load new puzzle file
                        s(elect) r c        -- select cell at r, c
                        q(uit)              -- quit the game
                        r(eset)             -- reset the current game
                        """
        );
    }

    /**
     * Prints on standard output the message contained in the client data
     */
    public void displayMessage() {
        System.out.println(this.model.getData().getMessage());
    }

    /**
     * @return String that when printed, displays the board
     */
    public String displayBoard() {
        HoppersConfig config = this.model.getCurrentConfig();
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("\s".repeat(3));

        for (int col = 0; col < HoppersConfig.numCols; ++col) {
            output.append(col);
            output.append("\s");
        }

        output.append("\n");
        output.append("\s".repeat(2));
        output.append("--".repeat(Math.max(0, HoppersConfig.numCols)));
        output.append("\n");

        for (int row = 0; row < HoppersConfig.numRows; ++row) {
            output.append(row);
            output.append("|\s");
            for (int col = 0; col < HoppersConfig.numCols; ++col) {
                output.append(config.getBoard()[row][col]);
                output.append("\s");
            }
            output.append("\n");
        }
        return output.toString();
    }

    @Override
    public void update(HoppersModel model, HoppersClientData data) {
        System.out.println(displayBoard());
    }

    /**
     * Runs a PTUI version of the Hoppers puzzle
     *
     * @param args command line args
     * @throws IOException if issue reading file occurs
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        HoppersPTUI PTUI = new HoppersPTUI(new HoppersConfig(args[0]));
        PTUI.run();
    }
}
