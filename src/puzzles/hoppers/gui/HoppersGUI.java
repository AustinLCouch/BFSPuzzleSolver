package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * GUI version of the Hoppers puzzle.
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, HoppersClientData> {

    /** model in MVC pattern to be used by the view and controller */
    private HoppersModel model;
    /** text displayed at the top of the GUI window */
    private Label indicator = new Label("");
    /** stage passed into */
    private Stage stage;
    /** BorderPane to be set into the stage */
    private BorderPane borderPane;
    /** indicates if a single piece has been selected */
    private boolean isSelected = false;
    /** start row of the initially selected piece */
    private int startRow;
    /** start column of the initially selected piece */
    private int startCol;

    /** the resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    //IMAGES FOR BUTTONS
    /** red frog image */
    private final Image redFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            RESOURCES_DIR + "red_frog.png")), 75, 75, false, false);
    /** green frog image */
    private final Image greenFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            RESOURCES_DIR + "green_frog.png")), 75, 75, false, false);
    /** water image */
    private final Image water= new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            RESOURCES_DIR + "water.png")), 75, 75, false, false);
    /** lily pad image */
    private final Image lilyPad = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            RESOURCES_DIR + "lily_pad.png")), 75, 75, false, false);

    /**
     * Creates a new HoppersModel instance and adds the GUI as a observer
     *
     * @throws IOException if something goes wrong when reading the file
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(new HoppersConfig(filename));
        this.model.addObserver(this);
    }

    /**
     * @return a GridPane of buttons that represent the game board
     */
    private GridPane makeBoard() {
        GridPane buttonBoard = new GridPane();
        char[][] currentBoard = this.model.getCurrentConfig().getBoard();

        for (int row = 0; row < HoppersConfig.numRows; ++row) {
            for (int col = 0; col < HoppersConfig.numCols; ++col) {

                char currentChar = currentBoard[row][col];
                HoppersButton button = new HoppersButton(row, col);
                button.setMinSize(75, 75);
                button.setMaxSize(75, 75);

                if (currentChar == HoppersConfig.EMPTY) {
                    button.setGraphic(new ImageView(lilyPad));
                } else if (currentChar == HoppersConfig.GREEN_FROG) {
                    button.setGraphic(new ImageView(greenFrog));
                } else if (currentChar == HoppersConfig.RED_FROG) {
                    button.setGraphic(new ImageView(redFrog));
                } else {
                    button.setGraphic(new ImageView(water));
                }

                buttonBoard.add(button, col, row);

                button.setOnAction(event -> {
                    if (!this.model.isPresent(button.row, button.col) && !this.isSelected) {
                        displayMessage();
                    } else {
                        if (!this.isSelected) {
                            this.startRow = button.row;
                            this.startCol = button.col;
                            this.isSelected = true;
                            this.indicator.setText("Selected (" + button.row + ", " + button.col + ")");
                        } else {
                            this.model.select(this.startRow, this.startCol, button.row, button.col);
                            this.isSelected = false;
                        }
                    }
                });
            }
        }
        return buttonBoard;
    }

    /**
     * Private helper class to represent a button in the Hoppers puzzle
     */
    private class HoppersButton extends Button {
        /** current row of the piece */
        private int row;
        /** current column of the piece */
        private int col;

        /**
         * Creates a button with a current row and column
         * @param row row of the button
         * @param col column of the button
         */
        public HoppersButton(int row, int col) {
            super();
            this.row = row;
            this.col = col;
        }
    }

    /**
     * @return returns a FlowPane of gameplay functionality buttons
     */
    private FlowPane makeButtons() {
        FlowPane buttons = new FlowPane();

        Button load = new Button("Load"); // make into fields?
        Button reset = new Button("Reset");
        Button hint = new Button("Hint");

        load.setFont(Font.font("Helvetica", 18));
        reset.setFont(Font.font("Helvetica", 18));
        hint.setFont(Font.font("Helvetica", 18));

        load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file to load");
            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
            currentPath += File.separator + "data" + File.separator + "hoppers";
            fileChooser.setInitialDirectory(new File(currentPath));
            File file = fileChooser.showOpenDialog(this.stage);
            if (file != null) {
                String[] files = file.toString().split(File.separator);
                String filename =
                        files[files.length - 3] + File.separator +
                        files[files.length - 2] + File.separator +
                        files[files.length - 1];
                this.model.load(filename);
            }
        });
        reset.setOnAction(event -> {
            this.model.reset();
            this.isSelected = false;
        });
        hint.setOnAction(event -> this.model.hint());

        buttons.getChildren().add(load);
        buttons.getChildren().add(reset);
        buttons.getChildren().add(hint);

        return buttons;
    }

    /**
     * Displays the message contained in the client data
     */
    private void displayMessage() {
        this.indicator.setText(this.model.getData().getMessage());
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        this.borderPane = new BorderPane();
        BorderPane top = new BorderPane();
        BorderPane bottom = new BorderPane();

        GridPane buttonBoard = makeBoard();
        FlowPane gameplayButtons = makeButtons();

        this.indicator.setFont(Font.font("Helvetica", 20));
        this.indicator.setAlignment(Pos.CENTER);
        top.setCenter(this.indicator);

        buttonBoard.setAlignment(Pos.CENTER);

        gameplayButtons.setAlignment(Pos.CENTER);
        bottom.setCenter(gameplayButtons);

        this.borderPane.setTop(top);
        this.borderPane.setCenter(buttonBoard);
        this.borderPane.setBottom(bottom);

        Scene scene = new Scene(borderPane);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.setTitle("Hoppers");
        this.stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, HoppersClientData hoppersClientData) {
        displayMessage();
        GridPane buttonBoard = makeBoard();
        buttonBoard.setAlignment(Pos.CENTER);
        this.borderPane.setCenter(buttonBoard);
        this.stage.sizeToScene();
    }

    /**
     * Launches the JavaFX application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
