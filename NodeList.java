import java.util.ArrayList;

public class NodeList {
  private Node head;
  private static int columnCounter;

  public NodeList() {
    head = new Node();
  }

  public void print() {
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
  }

  public static int getCounter() {
    return columnCounter;
  }

  public static int getRowCounter(Node C) {
    return C.getSize();
  }

  public static void incrementCounter() {
    columnCounter++;
  }

  public static void decrementCounter() {
    columnCounter--;
  }

  // Get all columns with a '1' in a specific row:
  public ArrayList<String> getRowNames (Node C, int index) {
    if(index < 0 || index > C.getSize())
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

  public Node getColumn (int index) {
    if(index < 0 || index > getCounter()-1)
      return null;

    Node current = head.getR();
    for(int i = 0; i < index; i++) {
      current = current.getR();
    }
    return current;
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

  public void addColumnNode (String N) {
    Node header = head;
    Node newNode = new Node(N);

    while(header.getR() != head) {
      header = header.getR();
    }

    newNode.setL(header);     newNode.setR(head);
    header.setR(newNode);     head.setL(newNode);
    incrementCounter();
  }

  // Add new row node at the end of a specific column:
  public void addRowNode (Node C) {
    Node header = C;
    Node newNode = new Node(C);

    while(header.getD() != C) {
      header = header.getD();
    }

    newNode.setU(header);   newNode.setD(C);
    header.setD(newNode);   C.setU(newNode);
  }

  // Add new row node to the right of a specific row node:
  public void addRowNodeRight (Node C, Node firstNode, Node L) {
    Node rowNode = new Node(C);
    rowNode.setL(L);              L.setR(rowNode);
    rowNode.setR(firstNode);      firstNode.setL(rowNode);
  }

  private class Node {
    private Node L;
    private Node R;
    private Node U;
    private Node D;
    private Node C;
    private int S;
    private String N;

    // Header node:
    public Node() {
      this.L = this;
      this.R = this;
    }

    // Row node:
    public Node (Node C) {
      this.L = this;
      this.R = this;
      this.C = C;

      C.incrementS();
    }

    // Column node:
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