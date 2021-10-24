import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;



public class sudokuCell{
    private static boolean isAnythingDirectlySelected;

    private boolean isDirectlySelected;
    private boolean isSelectedViaRowOrCol;
    private boolean isSelectedViaSameNum;
    private boolean isActualNumLabelVisible = true;
    private boolean isUserNumVisible = false;
    private boolean isGiven= true;
    private boolean isUserNumCorrect = true;

    private Label actualNum = new Label("");
    private Label userNum = new Label("");
    private Pane sudokuBoxCell = new Pane(actualNum,userNum);

    //Max size of box = how big is the sudoku box in pixels, ex. 3x3 is 225 pixels, so each cell is 75 pixels.
    sudokuCell(int sizeOfBox,double maxSizeOfBox){
        double maxCellSize = maxSizeOfBox / sizeOfBox;
        sudokuBoxCell.setMaxSize(maxCellSize,maxCellSize);
        sudokuBoxCell.setMinSize(maxCellSize,maxCellSize);

        userNum.setFont(Font.font((50-sizeOfBox*6)));
        userNum.layoutXProperty().bind(sudokuBoxCell.widthProperty().subtract(userNum.widthProperty()).divide(2));
        userNum.layoutYProperty().bind(sudokuBoxCell.heightProperty().subtract(userNum.heightProperty()).divide(2));
        //todo change the color scheme of the entire thing later.
        userNum.setStyle("-fx-text-fill: rgba( 218, 165, 32, 1)");

        actualNum.setFont(Font.font((50-sizeOfBox*6)));
        //this centers the Label in the Pane, super annoying. //https://stackoverflow.com/questions/36854031/how-to-center-a-label-on-a-pane-in-javafx
        actualNum.layoutXProperty().bind(sudokuBoxCell.widthProperty().subtract(actualNum.widthProperty()).divide(2));
        actualNum.layoutYProperty().bind(sudokuBoxCell.heightProperty().subtract(actualNum.heightProperty()).divide(2));
    }

    //if selected, or even indirectly selected.
    //sudokuBoxCell.setStyle("-fx-background-color: rgba( 165, 42, 42, 0.5)"); The cell opacity is visible.
    //otherwise
    //sudokuBoxCell.setStyle("-fx-background-color: rgba( 165, 42, 42, 0)"); The cell opacity is transparent

    //change the visibility, if its already set that way, return false, otherwise return true.

    //todo fix all the sloppy methods between this and the controller.

    /**
     ACTUAL NUM METHODS BELOW
     */

    //This initializes the cell to have the label.
    public void initializeLabel(int num){
        actualNum.setText(String.valueOf(num));
        isGiven = true;
        isUserNumCorrect = true;
    }

    //this needs to be a boolean return as we use it to initialize the board.
    public void setIsActualNumVisible(boolean isVisible){
        isActualNumLabelVisible = isVisible;
    }

    //returns if the actualNum is visible.
    public boolean getIsActualNumVisible(){
        return isActualNumLabelVisible;
    }

    //hides the actual numbers,  if it returned false, we already had the number hidden.
    public boolean hideActualNum() {
        if (isActualNumLabelVisible) {
            isActualNumLabelVisible = false;
            isGiven = false;
            actualNum.setVisible(false);
            isUserNumCorrect = false;
            return true;
        } else {
            return false;
        }
    }

    //This will show the actualNum, returns true if
    public void showActualNum(){
        if(!isGiven && !isActualNumLabelVisible){
            isActualNumLabelVisible = true;
            actualNum.setStyle("-fx-text-fill: rgba( 0, 0, 0, 1)");
            actualNum.setVisible(true);
            isUserNumCorrect = true;

            userNum.setVisible(false);
            isUserNumVisible = false;
        }
    }

    /**
     USER NUM METHODS BELOW.
     */

    //This method changes the cell to show the userNum, given that it's not already shown/given.
    public void setUserNum(int num){
        if(isGiven){
            System.out.println("The value is already given.");
            userNum.setVisible(false);
            isUserNumVisible = false;
            return; //Can't update something that's given.
        }
        if(isActualNumLabelVisible){
            System.out.println("The actual num is visible, cannot show userNum");
            userNum.setVisible(false);
            isUserNumVisible = false;
            return; //we already have the answer.
        }
        userNum.setText(String.valueOf(num));
        if(String.valueOf(Integer.parseInt(userNum.getText())).equals(String.valueOf(Integer.parseInt(actualNum.getText())))){
            showActualNum();
        }else{
            userNum.setStyle("-fx-text-fill: rgba( 255, 0, 0, 1)");
            isUserNumCorrect = false;
        }
        isUserNumVisible = true;
        userNum.setVisible(true);
    }

    public void hideUserNum(){
        isUserNumVisible = false;
        userNum.setVisible(false);
        isUserNumCorrect = false;
    }

    //This method is kind of redundant as setting the userNum will show it, but it's just in case.
    public void showUserNum(){
        if(isGiven){
            System.out.println("The value is already given.");
            userNum.setVisible(false);
            isUserNumVisible = false;
            return;
        }
        if(isActualNumLabelVisible){
            System.out.println("The actual num is visible, cannot show userNum");
            userNum.setVisible(false);
            isUserNumVisible = false;
            return;
        }
        isUserNumVisible = true;
        userNum.setVisible(true);
    }

    /**
     CELL SELECTION/CONTROL METHODS BELOW.
     */


    //This method will clear all selection status from our cell.
    public void clearAllSelectionStatus(){
        isSelectedViaRowOrCol = false;
        isDirectlySelected = false;
        isAnythingDirectlySelected = false;
        isSelectedViaSameNum = false;
        sudokuBoxCell.setStyle("-fx-background-color: rgba( 221, 188, 149, 0)");
    }

    //This method should be called for all the rows and columns of the selected cell.
    //This method will clear all the sameRow/Col highlights. for the cell.
    public void clearSameRowCol(){
        if(!isDirectlySelected){
            isSelectedViaRowOrCol = false;
            sudokuBoxCell.setStyle("-fx-background-color: rgba( 221, 188, 149, 0)");
        }
    }

    //This method should be called for all the rows and columns of the selected cell.
    //This method will give a more transparent blue than the directly selected cell and will not affect directly selected.
    public void isSelectedViaSameRowCol(){
        if(!isAnythingDirectlySelected){
            System.out.println("Nothing is selected. Time to debug ;).");
            return;
        }
        isSelectedViaRowOrCol = true;
        if(!isDirectlySelected) sudokuBoxCell.setStyle("-fx-background-color: rgba( 221, 188, 149, 0.3)");
    }

    //This method will deselect the specific box.
    //This method is kind of useless as clearAllSelectionStatus covers it, but maybe we might need it in a edge case.
    public void deselect(){
        isDirectlySelected = false;
        isAnythingDirectlySelected = false;
        sudokuBoxCell.setStyle("-fx-background-color: rgba( 221, 188, 149, 0)");
    }


    //This method will change our directly selected box.
    public boolean changeSelection(){
        if(!isAnythingDirectlySelected){
            isAnythingDirectlySelected = true;
        }

        //if the cell is already selected, toggle off.
        //if true is returned, then we clicked the same square, otherwise we did not.
        if(isDirectlySelected){
            isAnythingDirectlySelected = false;
            clearAllSelectionStatus();
            return false;
        }else{
            isDirectlySelected = true;
            sudokuBoxCell.setStyle("-fx-background-color: rgba( 221, 188, 149, 0.5)");
            return true;
        }
    }


    public boolean getIsDirectlySelected(){
        return isDirectlySelected;
    }

    public boolean getIsCorrect(){
        return isUserNumCorrect;
    }

    public boolean getIsAnythingDirectlySelected(){
        return isAnythingDirectlySelected;
    }

    public int getValueOfCell(){
        return Integer.parseInt(actualNum.getText());
    }

    public Pane getSudokuBoxCellPane() {
        return sudokuBoxCell;
    }
}
