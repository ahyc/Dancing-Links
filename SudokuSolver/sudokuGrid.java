package SudokuSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class sudokuGrid {
  // Class variables for the 2D sudoku grid and grid size:
  private List<List<Integer>> grid;
  private final int size;

  /**
   * Constructor to initialise the 2D sudoku grid and its size
   * @params  lines       Lines read from the sudoku text file
   */
  public sudokuGrid(Stream<String> lines) {
    this.grid = lines.map(line -> Arrays.stream(line.split("\\s"))
                                                          .map(Integer::parseInt)
                                                          .collect(Collectors.toList()))
                                      .collect(Collectors.toList());
    this.size = (int) Math.sqrt(grid.get(0).size());
  }

  /**
   * Method to return size of the grid
   * @return  Size of the sudoku grid
   */
  private int getSize() {
    return this.size;
  }

  /**
   * Method to get box number from the row and column numbers
   * @params      row       Row Number of cell
   *              col       Column Number of cell
   * @return      Box number of cell
   */
  private int getBoxNo(int row, int col) {
    return ((col-1)/getSize()) + (1 + (getSize() * ((row-1)/getSize())));
  }

  /**
   * Method to print the grid in sudoku format
   */
  private void printGrid() {
    System.out.print("The sudoku grid is: \n\n");
    int row = 0;
    for(List<Integer> line: grid) {
      int col = 0;
      for(Integer number: line) {
        if(number == 0)
          System.out.printf("_ ");
        else
          System.out.printf(number + " ");

        if((++col) % 3 == 0 && col < (getSize() * getSize()))
          System.out.printf("| ");
      }
      System.out.println();
      if((++row) % 3 == 0 && row < (getSize() * getSize()))
        System.out.println("- - - - - - - - - - -");
    }
    System.out.println();
  }

  /**
   * Method to solve the sudoku puzzle using the Dancing links alogorithm
   */
  public void solve() {
    // Print given sudoku puzzle:
    printGrid();

    sudokuDLX nodes = new sudokuDLX(getSize());

    // Create row nodes for all elements in the grid:
    List<int[]> filledCells = new ArrayList<int[]>();
    for(int row = 1; row < grid.size() + 1; row++) {
      for(int col = 1; col < grid.get(0).size() + 1; col++) {
        int[] cell = {row, col, getBoxNo(row, col)};
        int num = grid.get(row-1).get(col-1);

        if(num != 0) {
          // If cell number is given, add cell to 'filledCells' list:
          nodes.addRowNodes(cell, num, getSize());
          filledCells.add(new int[]{row, col, num});
        }
        else {
          // If cell is empty, create row nodes for every potential number in cell:
          for(int i = 1; i < 10; i++) {
            nodes.addRowNodes(cell, i, getSize());
          }
        }
      }
    }

    // Cover all columns and rows for each cell with a given number:
    for(int[] cell: filledCells) {
      nodes.coverNodes(nodes.getColumnFromName("Row: " +cell[0] + "; Col: " + cell[1]));
    }

    // Apply dancing links algorithm to the doubly linked list:
    nodes.search();

    // Replace elements in sudoku grid with the solution:
    List<int[]> solutions = nodes.getSolutions();
    for(int[] cell: solutions) {
      grid.get(cell[0]-1).set(cell[1]-1, cell[2]);
    }

    // Print puzzle solution:
    printGrid();

  }
}