import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class NodeListTest {
  public static NodeList nodeList;

  private static void printGrid(String[][] array) {
    System.out.println();
    for(int row = 0; row < array.length; row++) {
      for(int col = 0; col < array[0].length; col++) {
        System.out.printf(array[row][col] + " ");
      }
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
      printGrid(array);

      char colName = 'A';
      for(int col = 0; col < array[0].length; col++) {
        nodeList.addColumnNode(String.valueOf(colName));
        colName++;
      }

      for(int row = 0; row < array.length; row++) {
        Node first = null;    Node leftNode = null;
        for(int col = 0; col < array[0].length; col++) {

          if(array[row][col].equals("1")) {
            if(first == null) {
              nodeList.addRowNode(nodeList.getColumn(col));
              first = nodeList.getRow(nodeList.getColumn(col), row);
              leftNode = first;
            }
            else {
              nodeList.addRowNode(nodeList.getColumn(col), first, leftNode);
              leftNode = nodeList.getRow(nodeList.getColumn(col), row);
            }

          }
        }
      }

      nodeList.print();
    } catch (IOException e) {
      e.printStackTrace();
    }


    //
    // nodeList.addColumnNode("A");
    // nodeList.addColumnNode("B");
    // nodeList.addColumnNode("C");
    //
    // nodeList.print();
    // System.out.println(nodeList.getCounter());
    //
    // nodeList.addRowNode(nodeList.getColumn(0));
    //
    // nodeList.addRowNode(nodeList.getColumn(2), nodeList.getRow(nodeList.getColumn(0), 0), nodeList.getRow(nodeList.getColumn(0), 0));
    //
    // nodeList.addRowNode(nodeList.getColumn(0));
    //
    // nodeList.addRowNode(nodeList.getColumn(1), nodeList.getRow(nodeList.getColumn(0), 1), nodeList.getRow(nodeList.getColumn(0), 1));
    //
    // nodeList.print();
    //
    // System.out.println(nodeList.getRowNames(nodeList.getColumn(0), 0));
  }
}
