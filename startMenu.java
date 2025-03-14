import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class startMenu extends JPanel implements Runnable {

    public void run() {
        // Create the main frame
        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1365, 767);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Create a main panel with a BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240)); // Light gray background

        // Add a title label
        JLabel titleLabel = new JLabel("Minesweeper", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 48));
        titleLabel.setForeground(new Color(50, 50, 50)); // Dark gray text
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create a center panel for the game options
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add game mode selection
        JLabel gameModeLabel = new JLabel("Gamemodes:");
        gameModeLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(gameModeLabel, gbc);

        String[] gameModes = {"Small Board", "Medium Board", "Large Board"};
        gameMode = new JComboBox<>(gameModes);
        gameMode.setFont(new Font("Verdana", Font.PLAIN, 20));
        gameMode.setSelectedItem("Small Board");
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(gameMode, gbc);

        // Add difficulty selection
        JLabel difficultyLabel = new JLabel("Difficulty:");
        difficultyLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(difficultyLabel, gbc);

        String[] difficulties = {"Easy", "Normal", "Hard"};
        difficulty = new JComboBox<>(difficulties);
        difficulty.setFont(new Font("Verdana", Font.PLAIN, 20));
        difficulty.setSelectedItem("Easy");
        gbc.gridx = 1;
        gbc.gridy = 1;
        centerPanel.add(difficulty, gbc);

        // Add the center panel to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add a play button
        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Verdana", Font.BOLD, 24));
        playButton.setBackground(new Color(50, 150, 250)); // Blue background
        playButton.setForeground(Color.BLACK); // White text
        playButton.setFocusPainted(false); // Remove focus border
        playButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        playButton.addActionListener(new PlayListener());

        // Create a panel for the play button to center it
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        buttonPanel.add(playButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JComboBox<String> gameMode;
    private static JComboBox<String> difficulty;

    public class PlayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String filename = "EasySmall";
            try (PrintWriter fout = new PrintWriter(filename)) {
                int[] m = getValues();
                for (int j : m) {
                    fout.write(j + " ");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            // Start the Minesweeper game
            Runnable ex = new minesweeperOOP();
            SwingUtilities.invokeLater(ex::run);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new startMenu());
    }

    public static int[] getValues() {
        int t = difficulty.getSelectedIndex();
        int q = gameMode.getSelectedIndex();
        int m = 10 + q * 5;
        int l = (int) (m * m * (0.1 + (t * 0.03)));
        return new int[]{m, m, l};
    }
}