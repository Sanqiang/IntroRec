
import java.util.ArrayList;
import java.util.Arrays;

public class Board implements Comparable<Board> {

    public int[][] Tiles = null;
    public int Moves = 0;

    public Board(int[][] tiles) {
        int height = tiles.length;
        int width = tiles[0].length;
        Tiles = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tiles[row][col] = tiles[row][col];
            }
        }

        //this.Tiles = tiles.clone();
        Moves = 0;
    }

    public Board(int[][] tiles, int moves) {
        this(tiles);
        Moves = moves;
    }

    public int dimension() {
        int height = Tiles.length;
        int width = Tiles[0].length;
        return height * width;
    }

    public int hamming() {
        int hamming = 0;
        int height = Tiles.length;
        int width = Tiles[0].length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int correctNum = row * width + col + 1 == height * width ? 0 : row * height + col + 1;
                if (correctNum != Tiles[row][col]) {
                    ++hamming;
                }
            }
        }
        return hamming + Moves;
    }

    public int mahattan() {
        int mahattan = 0;
        int height = Tiles.length;
        int width = Tiles[0].length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int num = Tiles[row][col];
                if (num == 0) {
                    int correctRow = height - 1;
                    int correctCol = width - 1;
                    mahattan += (correctRow - row + correctCol - col);
                } else {
                    int correctRow = (num - 1) / width;
                    int correctCol = (num % width == 0 ? width : num % width) - 1;
                    mahattan += Math.abs(correctRow - row) + Math.abs(correctCol - col);
                }
            }
        }
        return mahattan;
    }

    public void printStatus() {
        int height = Tiles.length;
        int width = Tiles[0].length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(Tiles[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println("Current Moves : " + Moves);
        System.out.println();
    }

    private void swap(int col1, int row1, int col2, int row2) {
        int temp = this.Tiles[row1][col1];
        this.Tiles[row1][col1] = this.Tiles[row2][col2];
        this.Tiles[row2][col2] = temp;
    }

    public Iterable<Board> neighors() {
        ArrayList<Board> list = new ArrayList<Board>();
        int height = Tiles.length;
        int width = Tiles[0].length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (Tiles[row][col] == 0) {
                    int newmoves = Moves + 1;
                    if (row - 1 >= 0) {
                        Board nb = new Board(Tiles, newmoves);
                        nb.swap(col, row, col, row - 1);
                        list.add(nb);
                    }
                    if (col - 1 >= 0) {
                        Board nb = new Board(Tiles, newmoves);
                        nb.swap(col, row, col - 1, row);
                        list.add(nb);
                    }
                    if (row + 1 <= height - 1) {
                        Board nb = new Board(Tiles, newmoves);
                        nb.swap(col, row, col, row + 1);
                        list.add(nb);
                    }
                    if (col + 1 <= width - 1) {
                        Board nb = new Board(Tiles, newmoves);
                        nb.swap(col + 1, row, col, row);
                        list.add(nb);
                    }
                    break;
                }
            }
        }
        return list;
    }

    public boolean isSolved() {
        return mahattan() == 0;
    }

    public int compareTo(Board t) {
        int dif = this.hamming() - t.hamming() + this.mahattan() - t.mahattan();
        if (dif == 0) {
            return 0;
        } else if (dif > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Arrays.deepHashCode(this.Tiles);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        int[][] t = ((Board) obj).Tiles;
        int height = Tiles.length;
        int width = Tiles[0].length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (Tiles[row][col] != t[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
}
