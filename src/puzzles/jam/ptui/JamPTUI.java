package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.jam.model.JamClientData;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * Plain-text version of Jam puzzle
 *
 * @author Austin Couch
 */
public class JamPTUI implements Observer<JamModel, JamClientData> {

    /** model using MVC format to connect with view and controller */
    private JamModel model;

    /** assign the model for this PTUI and add observer + update view */
    public JamPTUI(JamConfig config){
        this.model = new JamModel(config);
        this.model.addObserver(this);
        update(this.model, null);
    }

    /** Reads a command and executes corresponding method from model */
    public void run(){
        Scanner in = new Scanner(System.in);
        while (true){
            if (this.model.getCurrentConfig().isSolution()){
                System.out.println("The puzzle has been solved!");
            }
            System.out.println("Game command: ");
            String fields = in.nextLine();
            String[] input = fields.split("\\s+");
            if (input.length > 0){
                if (input[0].startsWith("q")){
                    break;
                }
                else if (input[0].startsWith("h")){
                    this.model.hint();
                    displayMessage();
                }
                else if (input[0].startsWith("l")){
                    this.model.load(input[1]);
                    displayMessage();
                }
                else if (input[0].startsWith("s")){
                    int startRow = Integer.parseInt(input[1]);
                    int startCol = Integer.parseInt(input[2]);
                    System.out.print("Destination: ");
                    input = in.nextLine().split("\\s+");
                    this.model.select(startRow, startCol, Integer.parseInt(input[0]), Integer.parseInt(input[1]));
                    displayMessage();
                }
                else if (input[0].startsWith("r")){
                    this.model.reset();
                    displayMessage();
                }
                else{
                    displayHelp();
                }
            }
        }
    }

    /**
     * Prints on standard output help for the game including commands.
     */
    public void displayHelp(){
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

    /** Prints on standard output the message contained in the client data */
    public void displayMessage(){
        System.out.println(this.model.getData().getMessage());
    }

    /**
     * @return String that prints the board when printed
     */
    public String displayBoard(){
        JamConfig config = this.model.getCurrentConfig();
        StringBuilder output = new StringBuilder();
        output.append("\n");
        output.append("\s".repeat(3));

        for (int col = 0; col < config.getNumCols(); col++){
            output.append(col);
            output.append("\s");
        }

        output.append("\n");
        output.append("\s".repeat(2));
        output.append("--".repeat(Math.max(0, config.getNumCols())));
        output.append("\n");

        for (int row = 0; row < config.getNumRows(); ++row) {
            output.append(row);
            output.append("|\s");
            for (int col = 0; col < config.getNumCols(); ++col) {
                output.append(config.getBoard()[row][col]);
                output.append("\s");
            }
            output.append("\n");
        }
        return output.toString();
    }



    @Override
    public void update(JamModel jamModel, JamClientData jamClientData) {
        System.out.println(displayBoard());
    }

    /**
     * Runs a PTUI version of the Jam puzzle
     *
     * @param args command line args containing the filename to be read
     * @throws IOException if there is any issue reading in the file
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        }
        JamPTUI PTUI = new JamPTUI(new JamConfig(args[0]));
        PTUI.run();

    }
}
