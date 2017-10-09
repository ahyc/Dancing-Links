import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;

public class NodeListTest {
  public static NodeList nodeList;

  // Method to print the grid from the file:
  private static void printGrid(String[][] array) {
    System.out.println("The grid is as follows:");
    for(int row = 0; row < array.length; row++) {
      for(int col = 0; col < array[0].length; col++) {
        System.out.printf(array[row][col] + " ");
      }
      System.out.println();
    }
    System.out.println();
  }

  // Method to print the nodes of each row in the grid:
  private static void printGridNodes(Map<Integer, ArrayList<Integer>> rowNodes) {
    System.out.println("The grid nodes are as follows: ");
    for (Map.Entry<Integer, ArrayList<Integer>> entry : rowNodes.entrySet()) {
      System.out.printf("Row: " + entry.getKey() + ", Columns: ");
      entry.getValue().forEach(colIndex -> System.out.printf(nodeList.getColumnName(colIndex) + " "));
      System.out.println();
    }
    System.out.println();
  }

  public static void main(String args[]) {
    String fileName = "exactCover.txt";
    nodeList = new NodeList();
    String array[][];

    try(Stream<String> lines = Files.lines(Paths.get(fileName))) {
      array = lines.map(line -> line.split("\\s++"))
                        .toArray(String[][]::new);

      // Add column nodes to the right of the linked list:
      char colName = 'A';
      for(int col = 0; col < array[0].length; col++) {
        nodeList.addColumnNode(String.valueOf(colName));
        colName++;
      }

      // Add row nodes to the circular doubly linked list:
      Map<Integer, ArrayList<Integer>> rowNodes = new HashMap<Integer, ArrayList<Integer>>();
      Queue<Integer> queue = new ArrayDeque<Integer>();
  	  for(int row = 0; row < array.length; row++) {
    		for(int col = 0; col < array[0].length; col++) {
    			if(array[row][col].equals("1"))
    				queue.add(col);
    		}
        rowNodes.put((row), new ArrayList<Integer>(queue));
    		nodeList.addRowNodes(queue);
  	  }

      // printGrid(array);
      printGridNodes(rowNodes);
      // nodeList.printColumnSizes();

      nodeList.solve();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
