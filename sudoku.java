import SudokuSolver.sudokuGrid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class sudoku {
  public static void main(String args[]) {
    /**
     * File containing sudoku puzzle
     */
    String fileName = (args.length == 0)? "sudoku.txt": args[0];

    /**
     * Read from sudoku file and create a 2D arrayList of the sudoku grid
     */
    try(Stream<String> lines = Files.lines(Paths.get("SudokuGrids", fileName))) {
      sudokuGrid grid = new sudokuGrid(lines);
      grid.solve();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}