import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.IOException;

public class startMenuController {

    ObservableList<String> boardSizeList = FXCollections.observableArrayList("3x3");
    //todo 4x4 and 5x5 NOT IMPLEMENTED YET. implement it via modifying graves' algorithm.
    //ObservableList<String> boardSizeList = FXCollections.observableArrayList("3x3", "4x4", "5x5");
    private int difficulty; //0 = easy,1 = medium,2=hard.

    @FXML
    private Button playButton;
    @FXML
    private ChoiceBox boardSizeChoiceBox;
    @FXML
    private RadioButton radioButtonEasy;
    @FXML
    private RadioButton radioButtonMedium;
    @FXML
    private RadioButton radioButtonHard;

    @FXML
    protected void onRadioButtonPressed(){
        if(radioButtonEasy.isSelected()) difficulty = 0;
        else if (radioButtonMedium.isSelected()) difficulty = 1;
        else if (radioButtonHard.isSelected()) difficulty = 2;
    }

    @FXML
    protected void initialize() {
        boardSizeChoiceBox.setValue("3x3");
        boardSizeChoiceBox.setItems(boardSizeList);
    }

    //todo implement functionality for bigger sudoku boards
    @FXML
    protected void onPlayButtonClicked() throws IOException {
        String boardSize = (String) boardSizeChoiceBox.getValue();
        int boardSizeInt = 3;

        switch (boardSize) {
            case "3x3" -> boardSizeInt = 9;
            case "4x4" -> boardSizeInt = 16;
            case "5x5" -> boardSizeInt = 25;
            default -> {
                System.out.println("Error, boardsize not found");
                return;
            }
            //Start the actual game based on user board size
        }

        //creates a new Stage for the game.
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sudokuGame.fxml"));
            Parent root = loader.load();

            sudokuGameController sudokuGameController = loader.getController();

            Stage stage = (Stage) playButton.getScene().getWindow();
            Scene scene = new Scene(root,800,800);
            stage.setScene(scene);
            stage.setResizable(false);
            sudokuGameController.initializeSudokuBoard(boardSizeInt,difficulty,scene); //this passes the scene so we can listen to keyboard.
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
