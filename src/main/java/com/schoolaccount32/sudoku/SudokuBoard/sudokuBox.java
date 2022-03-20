package com.schoolaccount32.sudoku.SudokuBoard;

import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.HashSet;

public class sudokuBox{
    private HashSet<Integer> numAlreadyInBox = new HashSet<Integer>();
    private GridPane sudokuBox;
    private sudokuCell[][] cellPaneArray;
    private int[][] numbersInBox;
    private int sizeOfBox;

    public sudokuBox(int sizeOfIndividualBox){
        sizeOfBox = sizeOfIndividualBox;//these 2 are the same,although one can be used outside the constructor.
        numbersInBox = new int[sizeOfIndividualBox][sizeOfIndividualBox];
        cellPaneArray = new sudokuCell[sizeOfIndividualBox][sizeOfIndividualBox];
        sudokuBox = new GridPane();
        sudokuBox.setGridLinesVisible(true);
        sudokuBox.setAlignment(Pos.CENTER);
        sudokuBox.setStyle("-fx-background-color: #CDCDC0;");

        //this sets the minimum sudoku box size.
        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / sizeOfIndividualBox);
        for (int i = 0; i < sizeOfIndividualBox; i++) sudokuBox.getRowConstraints().add(rc);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / sizeOfIndividualBox);
        for (int i = 0; i < sizeOfIndividualBox; i++) sudokuBox.getColumnConstraints().add(cc);

        //set the size of all the total sudoku boxes lined up next to each other to be 675. 675/3 == 225. 765/4 == 168.75
        double maxSizeOfBox = 675d/sizeOfBox;
        if(sizeOfIndividualBox == 3 || sizeOfIndividualBox == 4 || sizeOfIndividualBox == 5){
            sudokuBox.setMinSize(maxSizeOfBox,maxSizeOfBox);
            sudokuBox.setMaxSize(maxSizeOfBox,maxSizeOfBox);
        }else{
            System.out.println("Invalid board size, something/someone messed up");
            return;
        }

        //What I'm trying to do is store all the nodes(Panes) of sudokuCells in the GridPane, and store the actual class
        //containing the Panes in a 2D array, so I can modify the values/call it without trying to pull from the GridPane.
        //because that's a big mess trying to pull the node and then trying to then change the label from the node.
        for(int cellRowPos=0;cellRowPos<sizeOfBox;cellRowPos++){
            for(int cellColPos=0;cellColPos<sizeOfBox;cellColPos++){
                sudokuCell s = new sudokuCell(sizeOfBox,maxSizeOfBox);
                sudokuBox.add(s.getSudokuBoxCellPane(),cellColPos,cellRowPos);
                cellPaneArray[cellRowPos][cellColPos] = s;
            }
        }
    }

    public void addActualNumToBox(int cellRowPos, int cellColPos, int number) {
        if (numbersInBox[cellRowPos][cellColPos] == 0 && numAlreadyInBox.add(number)) {
            numbersInBox[cellRowPos][cellColPos] = number;
            cellPaneArray[cellRowPos][cellColPos].initializeLabel(number);
        } else {
            //todo this is for debug, remove later.
            System.out.println("Adding " + number + " to rowPos " + cellRowPos + " and colPos " + cellColPos + " failed.");
        }
    }

    public boolean hideActualNum(int cellRowPos, int cellColPos){
        return cellPaneArray[cellRowPos][cellColPos].hideActualNum();
    }

    public void showActualNum(int cellRowPos, int cellColPos){
        cellPaneArray[cellRowPos][cellColPos].showActualNum();
    }

    public sudokuCell getSudokuCell(int cellRowPos,int cellColPos){
        return cellPaneArray[cellRowPos][cellColPos];
    }

    public GridPane getSudokuBox() {
        return sudokuBox;
    }

    public int[][] getNumbersInBox(){
        return numbersInBox;
    }
    public int getNumberInCell(int cellRowPos, int cellColPos){
        return numbersInBox[cellRowPos][cellColPos];
    }
}
