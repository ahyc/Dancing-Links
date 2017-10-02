import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class sudokuGrid {
  /**
   * Method to print out all elements within a multi-dimensional array.
   * @params array  the array containing the sudoku grid
   */
  private static void printGrid(String[][] array) {
    System.out.println();
    for(int row = 0; row < array.length; row++) {
      for(int col = 0; col < array[0].length; col++) {
        System.out.printf(array[row][col] + " ");
        if((col+1) % 3 == 0 && (col+1) < 9)
          System.out.printf("| ");
      }
      System.out.println();
      if((row+1) % 3 == 0 && (row+1) < 9)
        System.out.println("- - - - - - - - - - -");
    }
    System.out.println();
  }

  public static void main(String args[]) {
    /**
     * File containing sudoku puzzle
     */
    String fileName = "sudoku.txt";

    /**
     * Read from sudoku file and create a 2D array of the sudoku grid
     */
    String sudokuArr[][];
    try(Stream<String> lines = Files.lines(Paths.get(fileName))) {
      sudokuArr = lines.map(line -> line.split("\\s"))
                        .toArray(String[][]::new);
      printGrid(sudokuArr);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

