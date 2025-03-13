package MineSweepers;

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
    static int mins = 0;//number of mines
    static int firstX, firstY, changeX, changeY;
    static Image bomb = new ImageIcon("src/Minesweepers/Images/Bomb.png").getImage().getScaledInstance(20,20, Image.SCALE_DEFAULT);
    Icon bombIcon = new ImageIcon(bomb);
    JFrame frame = new JFrame();
    public static final int x = 700;

    public void run() {//Sets up the JFrame but also generates the grid and board
        frame.setSize(x, x);
        Scanner fin = null;
        String filename = "src/Files/EasySmall";
        try {//Reads the written file to find the rows, cols, and mines for the selected modes
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
        }//while
        game = new minesweeperOOP[rows][cols];
        game = genGrid(game, mins);
        for (int i = 0; i < game.length; i++) {
            for (int t = 0; t < game[0].length; t++) {
                frame.add(game[i][t]);//Adds each component JButton to the JFrame
            }
        }
        frame.setVisible(true);
    }

    public minesweeperOOP[][] genGrid(minesweeperOOP[][] cells, int mines) {//Generates each button/cell
        int cols = cells[0].length;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new minesweeperOOP();
            }
        }

        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < cells.length * cells[0].length; i++) list.add(i);
        Collections.shuffle(list);
        list = list.subList(0, mines);

        for (int a : list) {
            int row = a / cols;
            int col = a % cols;
            cells[row][col].setMine(true);
        }

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                doMinesAround(cells, i, j);
            }
        }
        return cells;
    }

    private void doMinesAround(minesweeperOOP[][] cells, int row, int col) {//This finds the number of mines around and sets the value of mines around of the button to that
        int count = 0;
        boolean top = false, bottom = false, left = false, right = false;
        if (row == 0) top = true;
        if (row == cells.length - 1) bottom = true;
        if (col == 0) left = true;
        if (col == cells[0].length - 1) right = true;
        if (!(top)) {
            if (!left && cells[row - 1][col - 1].isMine()) count++;
            if (cells[row - 1][col].isMine()) count++;
            if (!right && cells[row - 1][col + 1].isMine()) count++;
        }
        if (!bottom) {
            if (!left && cells[row + 1][col - 1].isMine()) count++;
            if (cells[row + 1][col].isMine()) count++;
            if (!right && cells[row + 1][col + 1].isMine()) count++;
        }
        if (!left)
            if (cells[row][col - 1].isMine()) count++;
        if (!right)
            if (cells[row][col + 1].isMine()) count++;
        cells[row][col].setMinesAround(count);
    }

    public void reveal(minesweeperOOP[][] cells, int row, int col) {//Reveals the button
        revealeds++;
        cells[row][col].setRevealed(true);
        cells[row][col].setEnabled(false);
        if (cells[row][col].isMine())
            gameLost = true;
        if (cells[row][col].getMinesAround() == 0) {
            revealAround(cells, row, col);
        } else {
            if (cells[row][col].getMinesAround() != 0) {
                cells[row][col].setText(String.valueOf(cells[row][col].getMinesAround()));
            }
        }
    }

    public void revealAround(minesweeperOOP[][] cells, int row, int col) {//Reveals each cell around those that have 0 mines
        boolean top = false, bottom = false, left = false, right = false;
        if (row == 0) top = true;
        if (row == cells.length - 1) bottom = true;
        if (col == 0) left = true;
        if (col == cells[0].length - 1) right = true;
        if (!(top)) {
            if (!left && cells[row - 1][col - 1].isRevealed()) reveal(cells, row - 1, col - 1);
            if (cells[row - 1][col].isRevealed()) reveal(cells, row - 1, col);
            if (!right && cells[row - 1][col + 1].isRevealed()) reveal(cells, row - 1, col + 1);
        }
        if (!bottom) {
            if (!left && cells[row + 1][col - 1].isRevealed()) reveal(cells, row + 1, col - 1);
            if (cells[row + 1][col].isRevealed()) reveal(cells, row + 1, col);
            if (!right && cells[row + 1][col + 1].isRevealed()) reveal(cells, row + 1, col + 1);
        }
        if (!left)
            if (cells[row][col - 1].isRevealed()) reveal(cells, row, col - 1);
        if (!right)
            if (cells[row][col + 1].isRevealed()) reveal(cells, row, col + 1);
    }

    public minesweeperOOP() {//Constructor for each cell
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
        return !revealed;
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


    private final ActionListener actionListener = actionEvent -> {//ActionListener for each Button
        firstY = game[0][0].getY();
        changeY = game[1][1].getY()-firstY;
        firstX = game[0][0].getX();
        changeX = game[1][1].getX()-firstX;

        reveal(game, (getY() - firstY) / changeY, (getX() - firstX)/ changeX);
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
        if (revealeds == (rows * cols) - mins || gameLost)
        {
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
