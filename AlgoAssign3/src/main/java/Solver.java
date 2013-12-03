
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sanqiangzhao
 */
public class Solver {

    public Board B = null;

    public Solver() {
        int[][] tiles = {
            {9, 2, 8, 11},
            {0, 5, 13, 7},
            {15, 1, 4, 10},
            {3, 14, 6, 12}
        };
        /*{
         {1, 2, 3, 4, 5, 7, 14},
         {8, 9, 10, 11, 12, 13, 6},
         {15, 16, 17, 18, 19, 20, 21},
         {22, 23, 24, 25, 26, 27, 28},
         {29, 30, 31, 32, 0, 33, 34},
         {36, 37, 38, 39, 40, 41, 35},
         {43, 44, 45, 46, 47, 48, 42}
         };*/
        /*{
         {11, 12, 13, 14, 0},
         {10, 7, 5, 4, 3},
         {9, 8, 6, 1, 2}
         };*/

        B = new Board(tiles);
        Queue<Board> q = new PriorityQueue<Board>();
        HashSet<Board> checked = new HashSet<Board>();
        q.add(B);
        B.printStatus();
        while (!q.isEmpty()) {
            Board pollboard = q.poll();
            pollboard.printStatus();
            checked.add(pollboard);
            for (Board nextboard : pollboard.neighors()) {
                if (nextboard.isSolved()) {
                    nextboard.printStatus();
                    return;
                }
                if (!checked.contains(nextboard)) {
                    q.add(nextboard);
                }

            }

        }

        //B.printStatus();
        //System.out.println(B.hamming());
        //System.out.println(B.mahattan());
    }

    public Solver(Board b) {
        this.B = b;
    }

    public int moves() {
        return 0;
    }

    public static void main(String[] args) {
        Solver s = new Solver();

    }
}
