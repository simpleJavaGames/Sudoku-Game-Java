import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class sudokuGameController{
    @FXML
    GridPane sudokuBoardGridPane;
    @FXML
    AnchorPane sudokuBoardBackgroundAnchor;
    @FXML
    Button hint;
    @FXML
    Button giveUpButton;
    @FXML
    AnchorPane youWinDim;
    @FXML
    Label youWinLabel;

    private int sizeOfBoard; //Small3x3 = 9,Medium 4x4 = 16,Large 5x5 = 25
    private int difficulty; //0 = easy, 1 = medium , 2= hard, this will change the number of hints the player gets.
    private sudokuBox[][] sudokuBox;
    private int sizeOfBox;
    private Random randomNumGenerator = new Random();
    private int currentlySelectedRowPos; //nothing is selected.
    private int currentlySelectedRowCellPos;//nothing is selected.
    private int currentlySelectedColPos;//nothing is selected.
    private int currentlySelectedColCellPos;//nothing is selected.
    private boolean running = true;
    private boolean keyDelayIsActive = false;

    public void initialize(){
        sudokuBoardBackgroundAnchor.setVisible(false);
        sudokuBoardBackgroundAnchor.setMinSize(800,800);
        sudokuBoardGridPane.setMinSize(715,715);
        sudokuBoardGridPane.setMaxSize(715,715);

        youWinDim.setMinSize(800,800);
        youWinDim.setMaxSize(800,800);
        youWinDim.setVisible(false);

        youWinLabel.setStyle("-fx-text-fill: rgba( 255, 223, 0, 1)");
        youWinLabel.setVisible(false);

        new Thread(){
            @Override
            public void run(){
                while(running){
                    if(keyDelayIsActive) keyDelayIsActive = false;
                    try{Thread.sleep(100);} catch (Exception e){e.printStackTrace();}
                }
            }
        }.start();
    }

    public void initializeSudokuBoard(int sizeOfBoard, int difficulty, Scene scene){
        this.sizeOfBoard = sizeOfBoard;
        this.difficulty = difficulty;

        sizeOfBox = (int)Math.round(Math.sqrt(sizeOfBoard));
        System.out.println("The size of each box is "+sizeOfBox+"x"+sizeOfBox);

        if(sizeOfBoard == 9 || sizeOfBoard == 16 || sizeOfBoard == 25){
            sudokuBox = new sudokuBox[sizeOfBox][sizeOfBox];
            //this code will create 9 sudoku boxes and then put them into a 2D array based on where they are.
            for(int colPos=0;colPos<sizeOfBox;colPos++){
                for(int rowPos=0;rowPos<sizeOfBox;rowPos++){
                    sudokuBox[rowPos][colPos] = new sudokuBox(sizeOfBox);
                    for(int cellRowPos=0;cellRowPos<sizeOfBox;cellRowPos++){
                        for(int cellColPos=0;cellColPos<sizeOfBox;cellColPos++){
                            final int newCellRowPos = cellRowPos;
                            final int newCellColPos = cellColPos;
                            final int newRowPos = rowPos;
                            final int newColPos = colPos;
                            //todo DON'T MAKE A BUNCH OF MOUSE EVENTS, MAKE ONE MOUSE EVENT FOR THE OUTER GRID PANE. meh, we can optimze this later.
                            sudokuBox[rowPos][colPos].getSudokuCell(cellRowPos,cellColPos).getSudokuBoxCellPane().setOnMouseClicked(new EventHandler<MouseEvent>() {
                                public void handle(MouseEvent event) {
                                    //we clicked a different box.
                                    if(currentlySelectedRowPos != newRowPos || currentlySelectedRowCellPos != newCellRowPos || currentlySelectedColPos != newColPos || currentlySelectedColCellPos != newCellColPos){
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).clearAllSelectionStatus();
                                        for(int i=0;i<sizeOfBox;i++){
                                            for(int j=0;j<sizeOfBox;j++){
                                                sudokuBox[currentlySelectedRowPos][i].getSudokuCell(currentlySelectedRowCellPos, j).clearSameRowCol();
                                            }
                                        }
                                        for(int i=0;i<sizeOfBox;i++){
                                            for(int j=0;j<sizeOfBox;j++){
                                                sudokuBox[i][currentlySelectedColPos].getSudokuCell(j,currentlySelectedColCellPos).clearSameRowCol();
                                            }
                                        }

                                        currentlySelectedRowPos = newRowPos;
                                        currentlySelectedRowCellPos = newCellRowPos;
                                        currentlySelectedColPos = newColPos;
                                        currentlySelectedColCellPos = newCellColPos;
                                    }
                                    //we clicked the same box.
                                    if(sudokuBox[newRowPos][newColPos].getSudokuCell(newCellRowPos, newCellColPos).changeSelection()){
                                        for(int i=0;i<sizeOfBox;i++){
                                            for(int j=0;j<sizeOfBox;j++){
                                                sudokuBox[newRowPos][i].getSudokuCell(newCellRowPos, j).isSelectedViaSameRowCol();
                                            }
                                        }
                                        for(int i=0;i<sizeOfBox;i++){
                                            for(int j=0;j<sizeOfBox;j++){
                                                sudokuBox[i][newColPos].getSudokuCell(j,newCellColPos).isSelectedViaSameRowCol();
                                            }
                                        }
                                    }else{
                                        //clear all other selections.
                                        for(int i=0;i<sizeOfBox;i++){
                                            for(int j=0;j<sizeOfBox;j++){
                                                sudokuBox[newRowPos][i].getSudokuCell(newCellRowPos, j).clearAllSelectionStatus();
                                            }
                                        }
                                        for(int i=0;i<sizeOfBox;i++){
                                            for(int j=0;j<sizeOfBox;j++){
                                                sudokuBox[i][newColPos].getSudokuCell(j,newCellColPos).clearAllSelectionStatus();
                                            }
                                        }
                                    }

                                }
                            });
                        }
                    }
                    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if (running && !keyDelayIsActive) {
                                switch (keyEvent.getCode()) {
                                    case DIGIT1:
                                    case NUMPAD1:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(1);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT2:
                                    case NUMPAD2:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(2);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT3:
                                    case NUMPAD3:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(3);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT4:
                                    case NUMPAD4:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(4);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT5:
                                    case NUMPAD5:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(5);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT6:
                                    case NUMPAD6:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(6);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT7:
                                    case NUMPAD7:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(7);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT8:
                                    case NUMPAD8:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(8);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case DIGIT9:
                                    case NUMPAD9:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).setUserNum(9);
                                        isAllUserChoicesCorrect();
                                        break;
                                    case ESCAPE:
                                    case NUMPAD0:
                                    case BACK_SPACE:
                                    case DIGIT0:
                                        sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).hideUserNum();
                                        break;
                                    default:
                                        System.out.println("Invalid input.");
                                }
                                keyDelayIsActive = true;
                            }
                        }
                    });
                    sudokuBoardGridPane.add(sudokuBox[rowPos][colPos].getSudokuBox(),colPos,rowPos);
                }
            }

            //this sets the background anchor to be the same size as the gridpane(so we have a background.)
            sudokuBoardBackgroundAnchor.setMinSize(sudokuBoardGridPane.getWidth(),sudokuBoardGridPane.getHeight());
            sudokuBoardBackgroundAnchor.setVisible(true);
            createSudokuAndAssignValues();
            hideRandomValues();
            System.out.println(sudokuBoardGridPane.getHeight());
        }else{
            System.out.println("Invalid board size");
            return;
        }

    }


    //this converts the 1D array we got from graves' algorithm to our 2d array and assigns it to each box.
    public void createSudokuAndAssignValues(){
        sudokuGenerator s = new sudokuGenerator(sizeOfBoard,sizeOfBox);
        int[] tempArray = s.generateGrid(); //tempArray will be size of sizeOfBoard * sizeOfBoard.

        //todo this is a quadruple nested loop, try to find more optimal way to do this dummy thing.
        int counter =0;
        for(int i=0;i< sudokuBox.length;i++){
            for(int k=0;k<sizeOfBox;k++){
                for(int j=0;j< sudokuBox[1].length;j++){
                    for(int l=0;l<sizeOfBox;l++){
                        sudokuBox[i][j].addActualNumToBox(k,l,tempArray[counter++]);
                    }
                }
            }
        }
    }

    public void hideRandomValues(){
        int numToHide =0;
        switch (difficulty) {
            case 0-> numToHide = 43;
            case 1-> numToHide = 51;
            case 2-> numToHide = 58;
        }

        while(numToHide >0){
            int boxRowPos = randomNumGenerator.nextInt(9);
            int boxColPos =  randomNumGenerator.nextInt(9);
            if(sudokuBox[boxRowPos/3][boxColPos/3].hideActualNum(boxRowPos%3,boxColPos%3)){
                sudokuBox[boxRowPos/3][boxColPos/3].getSudokuCell(boxRowPos%3,boxColPos%3).setIsActualNumVisible(false);
                numToHide--;
            }
        }
    }

    @FXML
    protected void onHintButtonPressed(){
        if(!running) return;
        if(sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).getIsDirectlySelected()){
            sudokuBox[currentlySelectedRowPos][currentlySelectedColPos].getSudokuCell(currentlySelectedRowCellPos, currentlySelectedColCellPos).showActualNum();
            isAllUserChoicesCorrect();
        }
    }

    @FXML
    protected void onGiveUpButtonPressed(){
        if(!running) return;
        for(int colPos=0;colPos<sizeOfBox;colPos++) {
            for (int rowPos = 0; rowPos < sizeOfBox; rowPos++) {
                for (int cellRowPos = 0; cellRowPos < sizeOfBox; cellRowPos++) {
                    for (int cellColPos = 0; cellColPos < sizeOfBox; cellColPos++) {
                        sudokuBox[rowPos][colPos].getSudokuCell(cellRowPos,cellColPos).showActualNum();
                    }
                }
            }
        }
        isAllUserChoicesCorrect();
    }

    public boolean isAllUserChoicesCorrect(){
        for(int colPos=0;colPos<sizeOfBox;colPos++) {
            for (int rowPos = 0; rowPos < sizeOfBox; rowPos++) {
                for (int cellRowPos = 0; cellRowPos < sizeOfBox; cellRowPos++) {
                    for (int cellColPos = 0; cellColPos < sizeOfBox; cellColPos++) {
                        if(!sudokuBox[rowPos][colPos].getSudokuCell(cellRowPos,cellColPos).getIsCorrect()) return false; //Game's not over yet.
                    }
                }
            }
        }
        endTheGame();
        System.out.println("YOU WIN");
        return true;
    }

    public void endTheGame(){
        System.out.println("Game Over");
        youWinDim.setVisible(true);
        youWinLabel.setVisible(true);
        running = false;
    }

}
