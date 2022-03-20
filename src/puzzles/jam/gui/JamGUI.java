package puzzles.jam.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.jam.model.Car;
import puzzles.jam.model.JamClientData;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;
import puzzles.jam.solver.Jam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * GUI version of the Jam puzzle.
 *
 * @author Austin Couch
 */
public class JamGUI extends Application  implements Observer<JamModel, JamClientData>  {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /** model in MVC pattern to be used by the view and controller */
    private JamModel model;
    /** text displayed at the top of the GUI window */
    private Label indicator = new Label("");
    /** stage for objects to be placed in */
    private Stage stage;
    /** BorderPane to be set into the stage */
    private BorderPane borderPane;
    /** indicates if a single piece has been selected */
    private boolean isSelected = false;
    /** start row of the initially selected piece */
    private int startRow;
    /** start column of the initially selected piece */
    private int startCol;

    /** hashmap to relate char cell names to colors */
    private HashMap<Character, String> colors;

    /** method to load all the  colors */
    private void loadColors() {
        this.colors = new HashMap<>();
        this.colors.put('A', "#81F781");
        this.colors.put('B', "#FE642E");
        this.colors.put('C', "#0101DF");
        this.colors.put('D', "#FF00FF");
        this.colors.put('E', "#AC58FA");
        this.colors.put('F', "#0B610B");
        this.colors.put('G', "#A4A4A4");
        this.colors.put('H', "#F5D0A9");
        this.colors.put('I', "#F3F781");
        this.colors.put('J', "#8A4B08");
        this.colors.put('K', "#0B6121");
        this.colors.put('L', "#FFFFFF");
        this.colors.put('O', "#FFFF00");
        this.colors.put('P', "#DA81F5");
        this.colors.put('Q', "#58ACFA");
        this.colors.put('R', "#088A08");
        this.colors.put('S', "#000000");
        this.colors.put('X', "#DF0101");
    }


    // for demonstration purposes
    private final static String X_CAR_COLOR = "#DF0101";
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int ICON_SIZE = 75;

    /**
     * Private helper class to represent a button in the jam puzzle
     */
    private class JamButton extends Button{
        /** current row of the cell */
        private int row;
        /** current row of the cell */
        private int col;
        /** current name of the cell */
        private char name;

        /**
         * Creates a button with a current row, column, and name
         * @param row row of the button
         * @param col column of the button
         * @param name name of the button
         */
        public JamButton(int row, int col, char name){
            this.row = row;
            this.col = col;
            this.name = name;
        }
    }

    /**
     * Creates a new JanModel instance and adds the GUI as an observer
     *
     * @throws IOException if something goes wrong when reading the file
     */
    public void init() throws IOException {
        loadColors();
        String filename = getParameters().getRaw().get(0);
        this.model = new JamModel(new JamConfig(filename));
        this.model.addObserver(this);
    }

    /** @return a GridPane of buttons that represent the game board */
    private GridPane makeBoard(){
        GridPane buttonBoard = new GridPane();
        JamConfig currentConfig = this.model.getCurrentConfig();
        char[][] currentBoard = currentConfig.getBoard();
        Car[] cars = this.model.getCurrentConfig().getCars();

        for (int row = 0; row < currentConfig.getNumRows(); ++row) {
            for (int col = 0; col < currentConfig.getNumCols(); ++col) {

                char currentCell = currentBoard[row][col];
                JamButton button = new JamButton(row, col, currentCell);
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);

                button.setText(Character.toString(currentCell));
                if (this.colors.containsKey(currentCell)){
                    button.setStyle("-fx-background-color: " + colors.get(currentCell) + ";");
                }

                buttonBoard.add(button, col, row);

                button.setOnAction(event -> {
                    if (this.model.isOpen(currentConfig, button.row, button.col) && !this.isSelected){
                        displayMessage();
                    }
                    else{
                        if(!isSelected){
                            this.startRow = button.row;
                            this.startCol = button.col;
                            this.isSelected = true;
                            this.indicator.setText("Selected (" + button.row + ", " + button.col + ")");
                        }
                        else{
                            this.model.select(this.startRow, this.startCol, button.row, button.col);
                            this.isSelected = false;
                        }
                    }
                });
            }
        }
        return  buttonBoard;
    }

    /**
     * @return returns a FlowPane of gameplay functionality buttons
     */
    public FlowPane makeButtons(){
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
            currentPath += File.separator + "data" + File.separator + "jam";
            fileChooser.setInitialDirectory(new File(currentPath));
            File file = fileChooser.showOpenDialog(this.stage);
            System.out.println(file);
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

    /** Displays the message contained in the client data */
    private void displayMessage(){
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
    public void update(JamModel jamModel, JamClientData jamClientData) {
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
            System.out.println("Usage: java JamPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
