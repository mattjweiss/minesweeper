import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;

// TODO documentaion
// TODO implement flags
// TODO formatting (fxml / css)

public class MineSweeperGUI extends Application {
    Stage stage;
    Scene titleScreen, gameScreen;
    int numMines = 20;
    int size = 10;
    MineSweeper game;
    Button[][] buttons;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        createTitleScreen();
        stage.setScene(titleScreen);
        stage.show();
    }

    void createTitleScreen() {
        // Container
        VBox vbox = new VBox();

        // MineSweeper Logo
        Text titleText = new Text();
        titleText.setFont(new Font(20));
        titleText.setText("M I N E S W E E P E R");

        // Entry for number of mines
        TextField mineSettingField = new TextField();
        mineSettingField.setPromptText("Enter a number of mines.");

        // Entry for size of board
        TextField sizeSettingField = new TextField();
        sizeSettingField.setPromptText("Enter a number of mines.");

        // Start button
        Button startBtn = new Button("Start");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                numMines = Integer.parseInt(mineSettingField.getText());//TODO fix
                size = Integer.parseInt(sizeSettingField.getText());
                game = new MineSweeper(numMines, size);
                createGameScreen();
                stage.setScene(gameScreen);
            }
        });

        // Construct the scene
        vbox.getChildren().addAll(titleText,
                                  mineSettingField,
                                  sizeSettingField,
                                  startBtn);
        titleScreen = new Scene(vbox);
    }

    void createGameScreen() {
        GridPane gridPane = new GridPane();
        buttons = new Button[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                buttons[i][j] = new Button();
                buttons[i][j].setText("x");
                buttons[i][j].setPrefWidth(30);
                buttons[i][j].setPrefHeight(30);
                final int x = i;
                final int y = j;
                buttons[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        game.guess(x,y);
                        drawBoard(game);
                    }
                });
                gridPane.add(buttons[i][j], i, j); 
            }
        }
        gameScreen = new Scene(gridPane);
    }

    void drawBoard(MineSweeper game) {
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                char val = game.getBoardValue(i,j);
                buttons[i][j].setText(Character.toString(val));
            }
        }
    }

}
