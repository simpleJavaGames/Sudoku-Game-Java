module com.schoolaccount32.sudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.schoolaccount32.sudoku to javafx.fxml;
    exports com.schoolaccount32.sudoku;
    exports com.schoolaccount32.sudoku.Controllers;
    opens com.schoolaccount32.sudoku.Controllers to javafx.fxml;
    exports com.schoolaccount32.sudoku.SudokuBoard;
    opens com.schoolaccount32.sudoku.SudokuBoard to javafx.fxml;
}