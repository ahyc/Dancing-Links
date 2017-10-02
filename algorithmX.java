import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;

public class algorithmX {
  private static List<Integer> algoX(List<List<Integer>> matrix, List<Integer> result) {
    if(matrix.isEmpty()) {  return result;  }

    List<Integer> colCount = getColCount(matrix);
    int minColCount = Collections.min(colCount);

    // for(Integer col: colCount) {
    //   System.out.printf(col + " ");
    // }
    // System.out.println();

    if(minColCount == 0){  return null;  }
    else {
      int minIndex = colCount.indexOf(minColCount) + 1;

      /**
       * Select row with '1' in the minimum-index column (nondeterministically),
       * and get all columns with '1' in the selected row
       */
      HashMap<Integer, List> oneMap = new HashMap<Integer, List>();
      for(List<Integer> row: matrix) {
        if(row.get(minIndex) == 1) {
          List<Integer> delColumns = new ArrayList<Integer>();

          for(int i = 1; i < row. size(); i++) {
            if(row.get(i) == 1) { delColumns.add(i);  }
          }
          Collections.reverse(delColumns);

          oneMap.put(row.get(0), delColumns);
        }
      }

      oneMap.forEach((k,v) -> System.out.println(k +" " + v));

      // List<Integer> delColumns = new ArrayList<Integer>();
      // for(List<Integer> row: matrix) {
      //   if(row.get(minIndex) == 1) {
      //     // Add row number to the partial solution:
      //     result.add(row.get(0));
      //
      //     for(int i = 1; i < row. size(); i++) {
      //       if(row.get(i) == 1) { delColumns.add(i);  }
      //     }
      //     Collections.reverse(delColumns);
      //     break;
      //   }
      // }

      /**
       * For loop to remove all row and columns with '1' similar to that of the partial solution
       */
      // for(Iterator<List<Integer>> itrRow = matrix.iterator(); itrRow.hasNext(); ) {
      //   List<Integer> row = itrRow.next();
      //   for(Iterator<Integer> itrCol = delColumns.iterator(); itrCol.hasNext(); ) {
      //     Integer col = itrCol.next();
      //     // Remove row if there is a corresponding '1' in the columns to-be-deleted
      //     if(row.get(col) == 1) {
      //       itrRow.remove();
      //       break;
      //     }
      //     else {  row.remove(col.intValue()); }
      //   }
      // }

      System.out.println();
      matrix.forEach(System.out::println);
      // return algoX(matrix, result);
      return null;
    }
  }

  /**
   * Method to get the minimum column-count
   * @params columnTotal  arraylist of the column-count
   * @return minimum      minimum int value from column-count
   */
  private static int minColumnCount(List<Integer> columnTotal) {
    int minimum = Collections.min(columnTotal.subList(1, columnTotal.size()));
    return minimum;
  }

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

      matrix.forEach(System.out::println);

      List<Integer> result = new ArrayList<Integer>();
      result = algoX(matrix, result);

      if(result == null) {
        System.out.println("The result is null");
      }
      else {
        for(Integer row: result) {
          System.out.println(row);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}