package TreeDisplay;

/*

 A           ------  1
 /  \
 /      \
 B        C       ------  2
 / \         \
 /     \         \
 D       E         H  ------  3
 / \       /
 /     \    /
 F       G  I     ------  4
 */
public class UseTreeDisplay {

    public Node buildTree() {
        Node btF = new Node("F");
        Node btG = new Node("G");
        Node btE = new Node("E", btF, btG);

        Node btD = new Node("D");
        Node btB = new Node("B", btD, btE);

        Node btH = new Node("H", new Node("I"), null);
        Node btC = new Node("C", null, btH);

        Node btA = new Node("A", btB, btC);

        return btA;
    }

    public static void main(String[] args) {
        UseTreeDisplay utd = new UseTreeDisplay();
        TreeDisplay display = new TreeDisplay("Test");
        Node root = utd.buildTree();
        display.setRoot(root);
    }
}