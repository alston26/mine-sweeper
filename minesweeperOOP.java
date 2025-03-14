import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.List;

public class minesweeperOOP extends JButton implements Runnable, ActionListener {
    public static void main(String[] args) {
        Runnable ex = new minesweeperOOP();
        ex.run();
    }

    static minesweeperOOP[][] game;
    static boolean gameGoing = true, gameLost = false;
    static int revealeds = 0;
    static int rows = 0;
    static int cols = 0;
    static int mins = 0; // number of mines
    static int firstX, firstY, changeX, changeY;
    static Image bomb = new ImageIcon("Images/Bomb.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
    Icon bombIcon = new ImageIcon(bomb);
    JFrame frame = new JFrame();
    public static final int x = 700;

    private static boolean firstClick = true; // Flag to track the first click
    private static int firstClickRow = -1, firstClickCol = -1; // Position of the first click

    public void run() { // Sets up the JFrame but also generates the grid and board
        frame.setSize(x, x);
        Scanner fin = null;
        String filename = "EasySmall";
        try { // Reads the written file to find the rows, cols, and mines for the selected modes
            fin = new Scanner(new File(filename));
        } catch (Exception ex) {
            System.out.print(ex);
        }
        while (fin.hasNext()) {
            try {
                String s = fin.next();
                String k = fin.next();
                String l = fin.next();
                rows = Integer.parseInt(s);
                cols = Integer.parseInt(k);
                mins = Integer.parseInt(l);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            frame.setLayout(new GridLayout(rows, cols));
        } // while
        game = new minesweeperOOP[rows][cols];
        game = genGrid(game); // Generate grid without bombs initially
        for (int i = 0; i < game.length; i++) {
            for (int t = 0; t < game[0].length; t++) {
                frame.add(game[i][t]); // Adds each component JButton to the JFrame
            }
        }
        frame.setVisible(true);
    }

    public minesweeperOOP[][] genGrid(minesweeperOOP[][] cells) { // Generates each button/cell without bombs
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new minesweeperOOP();
            }
        }
        return cells;
    }

    private void placeMines(minesweeperOOP[][] cells, int mines, int safeRow, int safeCol) { // Places mines randomly, avoiding the safe cell and its neighbors
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < cells.length * cells[0].length; i++) {
            int row = i / cols;
            int col = i % cols;
            // Exclude the safe cell and its adjacent cells
            if (Math.abs(row - safeRow) > 1 || Math.abs(col - safeCol) > 1) {
                list.add(i);
            }
        }
        Collections.shuffle(list);
        list = list.subList(0, mines);

        for (int a : list) {
            int row = a / cols;
            int col = a % cols;
            cells[row][col].setMine(true);
        }

        // Calculate the number of mines around each cell
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                doMinesAround(cells, i, j);
            }
        }

        // Ensure the first clicked cell has zero adjacent mines
        cells[safeRow][safeCol].setMinesAround(0);
    }

    private void doMinesAround(minesweeperOOP[][] cells, int row, int col) { // Calculates the number of mines around a cell
        int count = 0;
        for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, cells.length - 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, cells[0].length - 1); j++) {
                if (cells[i][j].isMine()) {
                    count++;
                }
            }
        }
        cells[row][col].setMinesAround(count);
    }

    public void reveal(minesweeperOOP[][] cells, int row, int col) { // Reveals the button
        if (cells[row][col].isRevealed()) {
            return; // If the cell is already revealed, do nothing
        }

        cells[row][col].setRevealed(true);
        cells[row][col].setEnabled(false);
        revealeds++;

        if (cells[row][col].isMine()) {
            gameLost = true;
            return;
        }

        if (cells[row][col].getMinesAround() == 0) {
            // Recursively reveal all adjacent cells if this cell has no adjacent mines
            revealAround(cells, row, col);
        } else {
            // If the cell has adjacent mines, display the number of mines
            cells[row][col].setText(String.valueOf(cells[row][col].getMinesAround()));
        }
    }

    public void revealAround(minesweeperOOP[][] cells, int row, int col) { // Reveals all adjacent cells
        for (int i = Math.max(0, row - 1); i <= Math.min(row + 1, cells.length - 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(col + 1, cells[0].length - 1); j++) {
                if (!cells[i][j].isRevealed()) {
                    reveal(cells, i, j); // Recursively reveal the cell
                }
            }
        }
    }

    public minesweeperOOP() { // Constructor for each cell
        mine = false;
        revealed = false;
        addActionListener(actionListener);
        setText(" ");
    }

    private int minesAround;
    private boolean mine, revealed;

    public boolean isMine() {
        return mine;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    @Override
    public String toString() {
        if (revealed) {
            if (minesAround == 0)
                return " ";
            return Integer.toString(minesAround);
        }
        return " ";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    private final ActionListener actionListener = actionEvent -> { // ActionListener for each Button
        if (firstClick) {
            // Get the position of the first click
            firstClickRow = -1;
            firstClickCol = -1;
            outerLoop:
            for (int i = 0; i < game.length; i++) {
                for (int j = 0; j < game[0].length; j++) {
                    if (game[i][j] == this) {
                        firstClickRow = i;
                        firstClickCol = j;
                        break outerLoop;
                    }
                }
            }

            // Place mines after the first click, ensuring the first cell and its neighbors are safe
            placeMines(game, mins, firstClickRow, firstClickCol);
            firstClick = false; // Mark that the first click has occurred

            // Reveal the first clicked cell and its adjacent cells
            reveal(game, firstClickRow, firstClickCol);
        } else {
            // Reveal the cell
            firstY = game[0][0].getY();
            changeY = game[1][1].getY() - firstY;
            firstX = game[0][0].getX();
            changeX = game[1][1].getX() - firstX;

            reveal(game, (getY() - firstY) / changeY, (getX() - firstX) / changeX);
        }

        System.out.println(revealeds + " " + ((rows * cols) - mins) + " " + mins);
        if (gameLost) {
            JOptionPane.showMessageDialog(frame, "Game Over");
            for (int i = 0; i < game.length; i++) {
                for (int j = 0; j < game[0].length; j++) {
                    game[i][j].setEnabled(false);
                }
            }
        }
        if (revealeds == (rows * cols) - mins)
            JOptionPane.showMessageDialog(frame, "You have won!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        if (revealeds == (rows * cols) - mins || gameLost) {
            gameLost = false;
            for (int i = 0; i < game.length; i++) {
                for (int j = 0; j < game[0].length; j++) {
                    if (game[i][j].isMine()) {
                        game[i][j].setIcon(bombIcon);
                    }
                }
            }
        }
    };
}