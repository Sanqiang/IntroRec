/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algoassign2;

import TreeDisplay.Node;
import TreeDisplay.TreeDisplay;
import java.util.*;

/**
 *
 * @author sanqiangzhao
 */
public class AlgoAssign2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        // TODO code application logic here
        //Make tree
        String exp = "(!(!(!(!(M)))))";
        Node root = drawTree(exp);
        TreeDisplay display = new TreeDisplay("Test");
        display.setRoot(root);
        //Validate
        HashMap<Character, Boolean> map = new HashMap<Character, Boolean>();
        map.put('M', Boolean.TRUE);
        map.put('B', Boolean.TRUE);
        map.put('C', Boolean.FALSE);
        System.out.println(verify(root, map));
        //DeMorgan's
        exp = "(!(!(!(!(M)))))";
        root = drawTree(exp);
        display = new TreeDisplay(exp);
        display.setRoot(root);
        Node ancestor = new Node('*');
        ancestor.right = root;
        Demorgan(root, ancestor,null);
        display.setRoot(ancestor.right);
    }

    private static void Demorgan(Node root, Node parent, Node ancester) throws CloneNotSupportedException {
        if (root == null || root.getValue() == '*') { //* means deleted
            return;
        }
        Demorgan(root.left, root, parent);
        Demorgan(root.right, root, parent);
        if (parent == null || ancester == null) {
            return;
        }
        char ch = root.getValue(), chParent = parent.getValue();
        if (ch == '!' && chParent == '!') {
            parent.setValue('*');
            ancester.right = root.right;
        } else if (chParent == '!' && (ch == '^' || ch == 'v')) {
            Node leftMinus = new Node('!'), rightMinus = new Node('!');
            parent.setValue(ch == 'v' ? '^' : 'v');
            root.setValue('*');
            leftMinus.right = root.left;
            rightMinus.right = root.right;
            parent.left = leftMinus;
            parent.right = rightMinus;
            ancester.right = parent;
        } else if ((chParent == '^' && ch == 'v' && parent.left == root && parent.right.getValue() <= 'Z' && parent.right.getValue() >= 'A')
                || (chParent == '^' && ch == 'v' && parent.right == root && parent.left.getValue() <= 'Z' && parent.left.getValue() >= 'A')
                || (chParent == 'v' && ch == '^' && parent.left == root && parent.right.getValue() <= 'Z' && parent.right.getValue() >= 'A')
                || (chParent == 'v' && ch == '^' && parent.right == root && parent.left.getValue() <= 'Z' && parent.left.getValue() >= 'A')) {
            parent.setValue(ch);
            root.setValue('*');
            Node boLeft = new Node(chParent), boRight = new Node(chParent), Single = null, BiLeft = root.left, BiRight = root.right;
            if (parent.left == root) {
                Single = parent.right;
                boLeft.left = root.left;
                boLeft.right = (Node) Single.Clone();
                boRight.left = root.right;
                boRight.right = (Node) Single.Clone();
            } else {
                Single = parent.left;
                boLeft.left = (Node) Single.Clone();
                boLeft.right = root.left;
                boRight.left = (Node) Single.Clone();
                boRight.right = root.right;
            }
            parent.left = boLeft;
            parent.right = boRight;
            ancester.right = parent;
        }
    }

    /*private static void Demorgan_old(Node root, Node parent, Node ancester) throws CloneNotSupportedException {
        if (root == null) {
            return;
        }
        char ch = root.getValue();
        char chParent = parent != null ? parent.getValue() : ' ';
        if (ch == '!' && chParent == '!') {
            ancester.right = root.right;
            Demorgan(ancester.right, ancester, null);
        } else if (chParent == '!' && (ch == '^' || ch == 'v')) {
            Node leftMinus = new Node('!'), rightMinus = new Node('!');
            Node biOp = new Node(ch == 'v' ? '^' : 'v');
            leftMinus.right = root.left;
            rightMinus.right = root.right;
            biOp.left = leftMinus;
            biOp.right = rightMinus;
            ancester.right = biOp;
            Demorgan(ancester.left, ancester, null);
            Demorgan(ancester.right, ancester, null);
        } else if ((chParent == '^' && ch == 'v' && parent.left == root && parent.right.getValue() <= 'Z' && parent.right.getValue() >= 'A')
                || (chParent == '^' && ch == 'v' && parent.right == root && parent.left.getValue() <= 'Z' && parent.left.getValue() >= 'A')
                || (chParent == 'v' && ch == '^' && parent.left == root && parent.right.getValue() <= 'Z' && parent.right.getValue() >= 'A')
                || (chParent == 'v' && ch == '^' && parent.right == root && parent.left.getValue() <= 'Z' && parent.left.getValue() >= 'A')) {
            Node topBi = new Node(ch), boLeft = new Node(chParent), boRight = new Node(chParent), Single = null, BiLeft = root.left, BiRight = root.right;
            if (parent.left == root) {
                Single = parent.right;
                boLeft.left = root.left;
                boLeft.right = (Node) Single.Clone();
                boRight.left = root.right;
                boRight.right = (Node) Single.Clone();
            } else {
                Single = parent.left;
                boLeft.left = (Node) Single.Clone();
                boLeft.right = root.left;
                boRight.left = (Node) Single.Clone();
                boRight.right = root.right;
            }
            topBi.left = boLeft;
            topBi.right = boRight;
            ancester.right = topBi;
            Demorgan(ancester.left, ancester, null);
            Demorgan(ancester.right, ancester, null);
        } else {
            Demorgan(root.left, root, parent);
            Demorgan(root.right, root, parent);
        }
    }*/

    private static boolean verify(Node root, HashMap<Character, Boolean> map) {
        if (root.getValue() == '!') {
            return !verify(root.right, map);
        } else if (root.getValue() == '^') {
            return verify(root.left, map) && verify(root.right, map);
        } else if (root.getValue() == 'v') {
            return verify(root.left, map) || verify(root.right, map);
        } else if (root.getValue() >= 'A' && root.getValue() <= 'Z') {
            return map.get(root.getValue());
        } else {
            //error
            return false;
        }
    }

    //!A A都放在！右邊
    private static Node drawTree(String exp) {
        Stack<Character> op = new Stack<Character>();
        Stack<Node> parenth = new Stack<Node>();
        for (int i = 0; i < exp.length(); i++) {
            char ch = exp.charAt(i);
            if (ch == '!' || ch == '^' || ch == 'v') {
                op.add(ch);
            } else if (ch >= 'A' && ch <= 'Z') {
                Node atom = new Node(ch);
                parenth.add(atom);
            } else if (ch == '(') {
            } else if (ch == ')') {
                if (op.isEmpty()) {
                    //break
                } else if (op.peek() == '!') {
                    Node minusOp = new Node(op.peek());
                    Node atom = null;
                    atom = parenth.pop();
                    minusOp.setRight(atom);
                    parenth.add(minusOp);
                    op.pop();
                } else if (op.peek() == '^' || op.peek() == 'v') {
                    Node biOp = new Node(op.peek());
                    Node leftAtom = null;
                    Node rightAtom = null;
                    Node right = parenth.pop();
                    Node left = parenth.pop();
                    biOp.setLeft(left);
                    biOp.setRight(right);
                    parenth.add(biOp);
                    op.pop();
                }
            }
        }
        return parenth.pop();
    }
}
