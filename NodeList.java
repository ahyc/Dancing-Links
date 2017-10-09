import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Iterator;

public class NodeList {
  private Node head;
  private static int columnCounter;

  // Minimise branching factor (S) boolean:
  private boolean factor = true;

  // Node List constructor:
  public NodeList() {
    head = new Node();
  }

  // Method to get the number of nodes for each column:
  public void printColumnSizes() {
    System.out.println("The Column objects are as follows:");
    if(head != null) {
      Node current = head.getR();
      for(int i = 0; i < columnCounter; i++) {
        if(current != head)
          System.out.printf(current.getName() +": " + current.getSize()+ "   ");
        current = current.getR();
      }
      System.out.println();
    }
    System.out.println();
  }

  // Method to print the rows that solve the exact cover solution:
  public void printSolution(Queue<Node> solution) {
    System.out.println("A solution is: ");

    Iterator<Node> rows = solution.iterator();
    while(rows.hasNext()) {
      Node r = rows.next();
      System.out.printf("Row with Columns: " + r.getC().getName() + " ");

      for(Node temp = r.getR(); temp != r; temp = temp.getR()) {
        System.out.printf(temp.getC().getName() + " ");
      }
      System.out.println();
    }
  }

  // Method which calls the dancing links algorithm:
  public void solve() {
    ArrayDeque<Node> solution = new ArrayDeque<Node>();
    solution = search(solution);
  }

  // Recursive method to find all solutions to the exact cover problem using DLX:
  public ArrayDeque<Node> search(ArrayDeque<Node> solution) {
    // Print current solution if doubly linked list is empty:
    if(head.getR() == head) {
      printSolution(solution);
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

  // Get all columns with a '1' in a specific row:
  public ArrayList<String> getRowNames (Node C, int index) {
   if(index < 0 || index > C.getSize()-1)
      return null;

    ArrayList<String> colNames = new ArrayList<String>();
    Node current = getRow(C, index);
    Node first = current;

    while(true) {
      colNames.add(current.getC().getName());
      current = current.getR();
      if(current == first)
        break;
    }
    return colNames;
  }

  // Method to get the column node at a specific index:
  public Node getColumn (int index) {
    if(index < 0 || index > columnCounter-1)
      return null;

    Node current = head.getR();
    for(int i = 0; i < index; i++) {
      current = current.getR();
    }
    return current;
  }

  // Method to get the column name of a specific column index:
  public String getColumnName (int index) {
    if(index < 0 || index > columnCounter-1)
      return null;

    Node current = head.getR();
    for(int i = 0; i < index; i++) {
      current = current.getR();
    }
    return current.getName();
  }

  public Node getRow (Node C, int index) {
    if(index < 0 || index > C.getSize()-1)
      return null;

    Node current = C.getD();
    for(int i = 0; i < index; i++) {
      current = current.getD();
    }
    return current;
  }

  // Method to add a new column node to the left of the header node:
  public void addColumnNode (String N) {
    Node lastColumnNode = head.getL();
    Node newNode = new Node(N);

    newNode.setL(lastColumnNode);     newNode.setR(head);
    lastColumnNode.setR(newNode);     head.setL(newNode);
    columnCounter++;
  }

  // Add row of new nodes to specific columns:
  public void addRowNodes(Queue<Integer> queue) {
  	Node firstNode = new Node(getColumn(queue.remove()));
  	addRowNodeToColumn(firstNode);

  	while(queue.peek() != null) {
  		addRowNodeRight(getColumn(queue.remove()), firstNode);
  	}
  }

  // Add new row node at the end of a specific column:
  public void addRowNodeToColumn(Node newNode) {
    Node C = newNode.getC();
    Node lastNode = C.getU();

    newNode.setU(lastNode);   newNode.setD(C);
    lastNode.setD(newNode);   C.setU(newNode);
  }

  // Add new row node to the right of a specific row node:
  public void addRowNodeRight (Node C, Node firstNode) {
    Node rowNode = new Node(C);
    Node lastRowNode = firstNode.getL();

    rowNode.setL(lastRowNode);		lastRowNode.setR(rowNode);
    rowNode.setR(firstNode);      	firstNode.setL(rowNode);

    addRowNodeToColumn(rowNode);
  }

  // Method to deterministically choose a column object, depending on the S-factor boolean:
  public Node getColumnObject(boolean factor) {
    Node column = head.getR();
    if(column == head) {
      System.out.println("Grid is empty, returning null.");
      return null;
    }
    else {
      if(factor) {
        int s = Integer.MAX_VALUE;
        Node object = column;

        while(column != head) {
          if(column.getSize() < s) {
            object = column;
            s = column.getSize();
          }
          column = column.getR();
        }
        return object;
      }
      else {
        return column;
      }
    }
  }

  // Method to cover a specific column and its associated row nodes:
  public void coverColumnNode(Node c) {
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

  // Method to uncover a specific column and its associated row nodes:
  public void uncoverColNode(Node c) {
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

  // Inner class for nodes:
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