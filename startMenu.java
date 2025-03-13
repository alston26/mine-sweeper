package MineSweepers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class startMenu extends JPanel implements Runnable{//Creates the Start Menu for the minesweeper game

    public void run(){//Runs the program here
        JPanel panel = new JPanel();
        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        panel.setLayout(null);

        String[] difficulties = {"Easy", "Normal", "Hard"};
        String[] gameModes = {"Small Board", "Medium Board", "Large Board"};

        JButton playButton = new JButton("Play");
        playButton.setBounds(590, 500, 220, 25);
        playButton.setFont(new Font("Verdana", Font.PLAIN, 20));
        playButton.addActionListener(new startMenu.PlayListener());
        panel.add(playButton);

        JLabel gameModePrompt = new JLabel("Gamemodes: ");
        gameModePrompt.setBounds(200, 400, 240, 25);
        gameModePrompt.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(gameModePrompt);

        gameMode = new JComboBox<String>(gameModes);
        gameMode.setBounds(340, 400, 240, 25);
        gameMode.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(gameMode);
        gameMode.setSelectedItem("Small Board");

        JLabel difficultyPrompt = new JLabel("Difficulty: ");
        difficultyPrompt.setBounds(800, 400, 140, 25);
        difficultyPrompt.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(difficultyPrompt);

        difficulty = new JComboBox<String>(difficulties);
        difficulty.setBounds(910, 400, 290, 25);
        difficulty.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(difficulty);
        difficulty.setSelectedItem("Easy");

        frame.add(panel);
        frame.setSize(1365, 767);
        frame.setVisible(true);
    }//Sets up all the components needed in the JFrame
    private static JComboBox<String> gameMode;
    private static JComboBox<String> difficulty;
    public class PlayListener implements ActionListener {//Actionlister that looks for when the play button is pressed
        @Override
        public void actionPerformed(ActionEvent event) {
            String filename = "src/Files/EasySmall";
            PrintWriter fout = null;
            try{//creates a file that is then read by the minesweeperOOP file
                fout = new PrintWriter(filename);
            }
            catch(Exception ex){
                System.out.print(ex);
            }
            try {
                int [] m = getValues();
                for (int j : m) {
                    assert fout != null;
                    fout.write(j + " ");
                }
            }
            catch(Exception ex){
                System.out.println("Try Again");
            }
            fout.close();
            Runnable ex = new minesweeperOOP();
            SwingUtilities.invokeLater(() -> ex.run());//Runs minesweeperOOP
        }
    }

    public static void main(String [] args) {
        Runnable xe = new startMenu();
        xe.run();
    }
    public static int[] getValues() {//Gets the value of the selected difficulty and gamemode
        int t = difficulty.getSelectedIndex();
        int q = gameMode.getSelectedIndex();
        int m = 10 + q*5;
        int l = (int)(m*m*(0.1+(t*0.03)));
        return new int[]{m, m, l};
    }
}
