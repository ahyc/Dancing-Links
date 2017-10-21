import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class algoX {
  /**
   * Method to get the count of each column
   * @params matrix     original matrix of the problem
   * @return total      count of all columns in list form
   */
  private static List<Integer> getColCount(List<List<Integer>> matrix) {
    List<Integer> total = new ArrayList<Integer>();
    for(int i = 1; i < matrix.get(0).size(); i++) {
      int colTotal = 0;
      for(List<Integer> row: matrix) {
        colTotal += row.get(i);
      }
      total.add(colTotal);
    }
    return total;
  }

  /**
   * Method to get all rows, and their columns with '1', that are part of the column with the lowest column-count
   * @params  matrix          original matrix of the problem
   *          minColCount     the minimum column-count integer
   * @return  oneMap          Hash map of row keys and their columns with '1' as values
   */
  private static HashMap<Integer, List<Integer>> findRowCol(List<List<Integer>> matrix, int minColCount) {
    HashMap<Integer, List<Integer>> oneMap = new HashMap<Integer, List<Integer>>();
    for(List<Integer> row: matrix) {
      if(row.get(minColCount) == 1) {
        List<Integer> delColumns = IntStream.range(1, row.size())
                                            .filter(i -> row.get(i) == 1)
                                            .boxed()
                                            .collect(Collectors.toList());
        Collections.reverse(delColumns);
        oneMap.put(row.get(0), delColumns);
      }
    }
    return oneMap;
  }

  /**
   * Method to remove all corresponding rows and columns of the chosen row
   * @params  matrix         original matrix of the problem
   *          columns        list of columns with '1' in the chosen row
   * @return  tempMatrix     reduced matrix
   */
  private static List<List<Integer>> removeRowCol(List<List<Integer>> matrix, List<Integer> columns) {
    List<List<Integer>> tempMatrix = new ArrayList<List<Integer>>();
    for(List<Integer> row: matrix) {
      if(IntStream.range(0, row.size()).filter(i -> columns.contains(i)).map(i -> row.get(i).intValue()).sum() == 0) {
        List<Integer> tempList = IntStream.range(0, row.size())
                                          .filter(i -> !columns.contains(i))
                                          .mapToObj(row::get)
                                          .collect(Collectors.toList());
        tempMatrix.add(tempList);
      }
    }
    return tempMatrix;
  }

  /**
   * Method to check if the solution found is valid
   * @params  matrix      original matrix of the problem
   *          result      potential solution to the exact cover problem
   * @return  boolean
   */
  private static boolean isSolution(List<List<Integer>> matrix, List<Integer> result) {
    List<List<Integer>> solution = new ArrayList<List<Integer>>();
    for(Integer row: result) {
      solution.add(matrix.get(row.intValue()));
    }
    List<Integer> total = getColCount(solution);
    if(Collections.min(total) < 1 || Collections.max(total) > 1) return false;
    else return true;
  }

  /**
   * Recursive method to solve the exact cover solution
   * @params  matrix              reduced matrix of the problem
   *          solution            list containing the partial solution
   * @return  'solve' method      passes reduced matrix and updated 'solution' as params
   */
  private static List<Integer> solve(List<List<Integer>> matrix, List<Integer> solution) {
    if(matrix.isEmpty()) {  return solution;  }
    else {
      List<Integer> colCount = getColCount(matrix);
      int minColIndex = colCount.indexOf(Collections.min(colCount)) + 1;
      if(Collections.min(colCount) == 0) {  return null;  }

      List<Integer> delColumns = new ArrayList<Integer>();
      for(List<Integer> row: matrix) {
        if(row.get(minColIndex) == 1) {
          // Add row number to the partial solution:
          solution.add(row.get(0));

          for(int i = 1; i < row. size(); i++) {
            if(row.get(i) == 1) { delColumns.add(i);  }
          }
          Collections.reverse(delColumns);
          break;
        }
      }

      List<List<Integer>> reduced = removeRowCol(matrix, delColumns);

      return solve(reduced, solution);
    }
  }

  /**
   * Method to split the solution into branches and find all solutions to the problem
   * @params  matrix      original matrix of the problem
   * @return  solution    List containing all solutions
   */
  private static List<List<Integer>> solve(List<List<Integer>> matrix) {
    List<List<Integer>> solution = new ArrayList<List<Integer>>();

    List<Integer> colCount = getColCount(matrix);
    int minColIndex = colCount.indexOf(Collections.min(colCount)) + 1;
    if(Collections.min(colCount) == 0) {  return null;  }

    for(Map.Entry<Integer, List<Integer>> entry : findRowCol(matrix, minColIndex).entrySet()){
      List<Integer> list = new ArrayList<Integer>();
      list.add(entry.getKey());

      List<Integer> result = solve(removeRowCol(matrix, entry.getValue()), list);

      if(result != null && isSolution(matrix, result)) {
        Collections.sort(result);
        solution.add(result);
      }
    }

    return solution;
  }

  public static void main(String args[]) {
    /**
     * File containing exact cover matrix
     */
    String fileName = "exactCover1.txt";

    /**
     * Read from exact cover file and create a 2D array list of the matrix
     */
    try(Stream<String> lines = Files.lines(Paths.get(fileName))) {
      List<List<Integer>> matrix = lines.map(line -> Arrays.stream(line.split("\\s+"))
                                                            .map(Integer::parseInt)
                                                            .collect(Collectors.toList()))
                                        .collect(Collectors.toList());

      /**
       * Adds row-index column to the left of the list
       */
      int index = 0;
      for(List<Integer> row: matrix) {
        row.add(0, index);
        index++;
      }

      List<List<Integer>> result = solve(matrix);
      if(result == null) {
        System.out.println("There were no solutions to this exact cover problem.");
      }
      else {
        System.out.println("The solution(s) to the exact cover problem: ");
        result.forEach(System.out::println);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}