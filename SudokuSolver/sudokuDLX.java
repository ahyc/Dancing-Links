package SudokuSolver;

import java.util.stream.Stream;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class sudokuDLX {
  private Node head;
  private static int columnCounter;

  // List of int arrays containing cell solutions:
  private List<int[]> cells;

  // Minimise branching factor (S) boolean:
  private final boolean factor = true;

  /**
   * Constructor to initialise the doubly linked list with constraint column nodes
   * @params  size       Size of the sudoku grid
   */
  public sudokuDLX(int size) {
    head = new Node();
    addColumnConstraints(size);
  }

  /**
   * Method to add all row nodes that solve the puzzle to the solution list
   * @params  solution       ArrayDeque of nodes that are part of the solution
   */
  private void addSolution(ArrayDeque<Node> solution) {
    Iterator<Node> rowNodes = solution.iterator();
    while(rowNodes.hasNext()) {
      Node rowNode = rowNodes.next();
      cells.add(getCellInfo(rowNode));
    }
  }

  /**
   * Method to get the row number, column number and cell number from row Node
   * @params  r       Row node that is part of the solution
   * @return          int array containing cell information
   */
  private int[] getCellInfo (Node r) {
    String colName = r.getC().getName();
    while(!(colName.contains("Row:") && colName.contains("Col:"))) {
      r = r.getL();
      colName = r.getC().getName();
    }

    String info = colName.replaceAll("[^0-9]", "");
    info += (r.getL().getC().getName()).replaceAll("[^0-9]", "").substring(1);

    return Stream.of(info.split("")).mapToInt(Integer::parseInt).toArray();
  }

  /**
   * Method to get the solution from the dancing links algorithm
   * @return      List of int arrays with cell information that make up the solution
   */
  public List<int[]> getSolutions() {
    return cells;
  }

  /**
   * Method to get the column name of a specific column index
   * @params      index       Index of column Node
   * @return                  Name of the column Node
   */
  private String getColumnName (int index) {
    if(index < 1 || index > columnCounter)
      return null;

    Node current = head.getR();
    for(int i = 1; i < index; i++) {
      current = current.getR();
    }
    return current.getName();
  }

  /**
   * Method to get Column Node from its column node
   * @params      name        Column Node name with format "Row: x; Col: y"
   * @return                  Column Node with given name
   */
  public Node getColumnFromName (String name) {
    Node current = head.getR();
    for(int i = 0; i < columnCounter/4; i++) {
      if(current.getName().equals(name)) {
        return current;
      }
      else {
        current = current.getR();
      }
    }
    return null;
  }

  /**
   * Method to get the column node at a specific index
   * @params      index       Index of column Node
   * @return                  Column Node at given index
   */
  private Node getColumn (int index) {
    if(index < 1 || index > columnCounter)
      return null;

    Node current = head.getR();
    for(int i = 1; i < index; i++) {
      current = current.getR();
    }
    return current;
  }

  /**
   * Method to deterministically choose a column object, depending on the S-factor boolean
   * @params      factor      S-factor boolean
   * @return                  Column Node (Column Node of the smallest size if factor is true)
   */
  private Node getColumnObject(boolean factor) {
    Node column = head.getR();
    if(factor) {
      int s = Integer.MAX_VALUE;
      Node col = column;

      while(column != head) {
        if(column.getSize() < s) {
          col = column;
          s = column.getSize();
        }
        column = column.getR();
      }
      return col;
    }
    else {
      return column;
    }
  }

  /**
   * Method to add a new column node to the left of the header node
   * @params      N       Name of the column node
   */
  private void addColumnNode (String N) {
    Node lastColumnNode = head.getL();
    Node newNode = new Node(N);

    newNode.setL(lastColumnNode);     newNode.setR(head);
    lastColumnNode.setR(newNode);     head.setL(newNode);
    columnCounter++;
  }

  /**
   * Add new row node at the end of a specific column
   * @params      newNode       Node which is inserted to the end of the column
   */
  private void addRowNodeToColumn(Node newNode) {
    Node C = newNode.getC();
    Node lastNode = C.getU();

    newNode.setU(lastNode);   newNode.setD(C);
    lastNode.setD(newNode);   C.setU(newNode);
  }

  /**
   * Add new row node to the left of a specific row node
   * @params     C              Column node of the new node
   *             firstNode      First node of the row
   */
  private void addRowNodeRight (Node C, Node firstNode) {
    Node rowNode = new Node(C);
    Node lastRowNode = firstNode.getL();

    rowNode.setL(lastRowNode);		lastRowNode.setR(rowNode);
    rowNode.setR(firstNode);      	firstNode.setL(rowNode);

    addRowNodeToColumn(rowNode);
  }

  /**
   * Create a new row of nodes with given cell information
   * @params    cell           int array with the row, column and box information
   *            number         number of the cell at given location
   *            size           size of the sudoku grid
   */
  public void addRowNodes(int[] cell, int number, int size) {
    Node firstNode = new Node(getColumn(((cell[0]-1)*(size*size))+cell[1]));
    addRowNodeToColumn(firstNode);

    for(int i = 0; i < cell.length; i++) {
      int columnNo = ((int) Math.pow(size, 4) * (i+1)) + ((cell[i]-1) * (int) Math.pow(size,2)) + number;
      addRowNodeRight(getColumn(columnNo), firstNode);
    }
  }

  /**
   * Add constraint column nodes to the linked list (for 4 constraints)
   * @params    size           size of the sudoku grid
   */
  private void addColumnConstraints(int size) {
    // Row-Column Constraint:
    for(int row = 1; row < (size * size)+1; row++) {
      for(int col = 1; col < (size * size)+1; col++) {
        addColumnNode("Row: " + row + "; Col: " + col);
      }
    }

    // Row-Number Constraint:
    for(int row = 1; row < (size * size)+1; row++) {
      for(int num = 1; num < (size * size)+1; num++) {
        addColumnNode("Row: " + row + "; Num: " + num);
      }
    }

    // Column-Number Constraint:
    for(int col = 1; col < (size * size)+1; col++) {
      for(int num = 1; num < (size * size)+1; num++) {
        addColumnNode("Col: " + col + "; Num: " + num);
      }
    }

    // Box-Number Constraint:
    for(int box = 1; box < (size * size)+1; box++) {
      for(int num = 1; num < (size * size)+1; num++) {
        addColumnNode("Box: " + box + "; Num: " + num);
      }
    }
  }

  /**
   * Method to find the solution to the sudoku puzzle
   */
  public void search() {
    ArrayDeque<Node> solution = new ArrayDeque<Node>();
    cells = new ArrayList<int[]>();
    solution = search(solution);
  }

  /**
   * Recursive method to find all cell solutions to the exact cover problem using DLX
   * @params        solution        ArrayDeque containing all the partial solutions
   * @return        Empty ArrayDeque
   */
  private ArrayDeque<Node> search(ArrayDeque<Node> solution) {
    // Print current solution if doubly linked list is empty:
    if(head.getR() == head) {
      addSolution(solution);
      return solution;
    }
    else {
      // Choose column object c:
      Node c = getColumnObject(factor);

      // Cover column c:
      coverColumnNode(c);

      for(Node r = c.getD(); r != c; r = r.getD()) {
        solution.add(r);
        for(Node j = r.getR(); j != r; j = j.getR()) {
          coverColumnNode(j.getC());
        }

        // Recursively apply method to the partial solution:
        search(solution);

        // Uncover the rows and columns of the column object row:
        r = solution.removeLast();
        c = r.getC();
        for(Node j = r.getL(); j != r; j = j.getL()) {
          uncoverColNode(j.getC());
        }
      }
      uncoverColNode(c);
      return solution;
    }
  }

  /**
   * Method to cover a specific column and its associated row nodes
   * @params        C        Column Node to be covered
   */
  private void coverColumnNode(Node c) {
    c.getL().setR(c.getR());
    c.getR().setL(c.getL());

    for(Node i = c.getD(); i != c; i = i.getD()) {
      for(Node j = i.getR(); j != i; j = j.getR()) {
        j.getU().setD(j.getD());
        j.getD().setU(j.getU());

        j.getC().decrementS();
      }
    }
  }

  /**
   * Method to cover all rows and columns associated with column node
   * @params        C        Column Node to be covered
   */
  public void coverNodes(Node c) {
    // Cover column c:
    coverColumnNode(c);

    for(Node r = c.getD(); r != c; r = r.getD()) {
      for(Node j = r.getR(); j != r; j = j.getR()) {
        coverColumnNode(j.getC());
      }
    }
  }

  /**
   * Method to uncover a specific column and its associated row nodes
   * @params        C        Column Node to be uncovered
   */
  private void uncoverColNode(Node c) {
    for(Node i = c.getU(); i != c; i = i.getU()) {
      for(Node j = i.getL(); j != i; j = j.getL()) {
        j.getC().incrementS();

        j.getD().setU(j);
        j.getU().setD(j);
      }
    }

    c.getR().setL(c);
    c.getL().setR(c);
  }


  /**
   * Inner class for nodes
   */
  private class Node {
    private Node L;
    private Node R;
    private Node U;
    private Node D;
    private Node C;
    private int S;
    private String N;

    // Header node constructor:
    public Node() {
      this.L = this;
      this.R = this;
    }

    // Row node constructor:
    public Node (Node C) {
      this.L = this;
      this.R = this;
      this.C = C;

      C.incrementS();
    }

    // Column node constructor:
    public Node (String N) {
      this.U = this;
      this.D = this;
      this.C = C;
      this.N = N;
      this.S = 0;
    }

    private void setL(Node node) {
      this.L = node;
    }

    private void setR(Node node) {
      this.R = node;
    }

    private void setU(Node node) {
      this.U = node;
    }

    private void setD(Node node) {
      this.D = node;
    }

    private void setName(String N) {
      this.N = N;
    }

    private Node getL() {
      return L;
    }

    private Node getR() {
      return R;
    }

    private Node getU() {
      return U;
    }

    private Node getD() {
      return D;
    }

    private Node getC() {
      return C;
    }

    private int getSize() {
      return S;
    }

    private String getName() {
      return N;
    }

    private void incrementS() {
      this.S++;
    }

    private void decrementS() {
      this.S--;
    }
  }
}