package TreeDisplay;

public class Node implements Cloneable {

    public Node left;
    public Node right;
    String symbol;

    public Node(String symbol, Node left, Node right) {
        this.symbol = symbol;
        this.left = left;
        this.right = right;
    }

    public Node(String symbol) {
        this(symbol, null, null);
    }

    public Node(char symbol) {
        this(String.valueOf(symbol), null, null);
    }

    public void setLeft(Node n) {
        this.left = n;
    }

    public void setRight(Node n) {
        this.right = n;
    }

    public char getValue() {
        return symbol.charAt(0);
    }

    public void setValue(char c)
    {
        this.symbol = String.valueOf(c);
    }
    
    public Object Clone()throws CloneNotSupportedException {
        return super.clone();
    }
    
    
}