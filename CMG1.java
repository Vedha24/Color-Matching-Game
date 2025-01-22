import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CMG1 extends JFrame {
    private JPanel targetPanel, userColorPanel;
    private JSlider redSlider, greenSlider, blueSlider;
    private JButton checkButton, restartButton, resetButton;
    private Color targetColor;
    private int attempts;

    public CMG1() {
        
        setTitle("Color Matching Game");
        setSize(800, 600);  // Initial size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Custom JPanel for the instruction dialog using GridBagLayout for better control
        JPanel instructionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel instructionLabel = new JLabel("<html><body style='width: 375px; text-align: center;'>"
            + "Instructions:<br>"
            + "1. Adjust the sliders to match the target color displayed at the top and click on check.<br>"
            + "2. You have 3 attempts to match the color.<br>"
            + "3. Your score will be based on how close your color is to the target.<br><br>"
            + "Good luck!"
            + "</body></html>");
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add instruction label and start button with GridBagLayout constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        instructionPanel.add(instructionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        instructionPanel.add(startButton, gbc);

        // Create the instruction dialog
        JDialog instructionDialog = new JDialog(this, "How to Play", true);
        instructionDialog.setContentPane(instructionPanel);
        instructionDialog.setSize(500, 400);
        instructionDialog.setLocationRelativeTo(this);

        // Start button action to close the dialog and proceed to the game
        startButton.addActionListener(e -> instructionDialog.dispose());

        // Show the dialog
        instructionDialog.setVisible(true);

        // Generate a random target color
        targetColor = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));

        // Target color display
        targetPanel = new JPanel();
        targetPanel.setBackground(targetColor);
        targetPanel.setPreferredSize(new Dimension(800, 150)); // This is flexible for resizing
        add(targetPanel, BorderLayout.NORTH);

        // User color display
        userColorPanel = new JPanel();
        userColorPanel.setBackground(Color.BLACK);
        userColorPanel.setPreferredSize(new Dimension(800, 150)); // Flexible size
        add(userColorPanel, BorderLayout.CENTER);

        // Sliders for RGB
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(3, 2, 10, 10));
        redSlider = createSlider();
        greenSlider = createSlider();
        blueSlider = createSlider();

        // Add sliders and labels
        sliderPanel.add(new JLabel("Red"));
        sliderPanel.add(redSlider);
        sliderPanel.add(new JLabel("Green"));
        sliderPanel.add(greenSlider);
        sliderPanel.add(new JLabel("Blue"));
        sliderPanel.add(blueSlider);
        add(sliderPanel, BorderLayout.WEST);

        // Initialize attempt counter
        attempts = 0;

        // Check button
        checkButton = new JButton("Check");
        checkButton.addActionListener(e -> {
            if (attempts < 3) {
                int score = calculateColorDifference(targetColor, new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue()));
                showFeedbackPopup(score);
                attempts++;

                // Disable the button after 3 tries
                if (attempts == 3) {
                    checkButton.setEnabled(false);
                    JOptionPane.showMessageDialog(CMG1.this, "Game Over! You've used all your tries.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    showRestartResetButtons();
                }
            }
        });
        add(checkButton, BorderLayout.EAST);

        // Update the user color immediately when sliders are moved
        redSlider.addChangeListener(e -> updateUserColor());
        greenSlider.addChangeListener(e -> updateUserColor());
        blueSlider.addChangeListener(e -> updateUserColor());
    }

    private JSlider createSlider() {
        JSlider slider = new JSlider(0, 255);
        slider.setMajorTickSpacing(51);
        slider.setMinorTickSpacing(17);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(250, 40));
        return slider;
    }

    private void updateUserColor() {
        int r = redSlider.getValue();
        int g = greenSlider.getValue();
        int b = blueSlider.getValue();
        userColorPanel.setBackground(new Color(r, g, b));
    }

    private int calculateColorDifference(Color target, Color userColor) {
        int rDiff = Math.abs(target.getRed() - userColor.getRed());
        int gDiff = Math.abs(target.getGreen() - userColor.getGreen());
        int bDiff = Math.abs(target.getBlue() - userColor.getBlue());

        int totalDiff = rDiff + gDiff + bDiff;
        int maxPossibleDiff = 255 * 3;
        int score = (int) ((1 - (totalDiff / (double) maxPossibleDiff)) * 100);

        return score;
    }

    private void showFeedbackPopup(int score) {
        String message;
        if (score == 100) {
            message = "Perfect match! You nailed it!";
        } else if (score >= 80) {
            message = "Great job! You're really close!";
        } else if (score >= 60) {
            message = "Not bad, but there's still room for improvement!";
        } else if (score >= 40) {
            message = "You're getting there, but it's not quite right yet.";
        } else if (score >= 20) {
            message = "Keep trying, you're far from the target!";
        } else {
            message = "Very far off! Try again!";
        }

        JOptionPane.showMessageDialog(this, message, "Your Score: " + score + "%", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRestartResetButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Restart button
        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        buttonPanel.add(restartButton);

        // Reset button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());
        buttonPanel.add(resetButton);

        add(buttonPanel, BorderLayout.SOUTH);
        validate();
        repaint();
    }

    private void restartGame() {
        targetColor = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        targetPanel.setBackground(targetColor);
        userColorPanel.setBackground(Color.BLACK);

        redSlider.setValue(0);
        greenSlider.setValue(0);
        blueSlider.setValue(0);

        attempts = 0;
        checkButton.setEnabled(true);

        getContentPane().remove(restartButton.getParent());
        validate();
        repaint();
    }

    private void resetGame() {
        redSlider.setValue(0);
        greenSlider.setValue(0);
        blueSlider.setValue(0);
        userColorPanel.setBackground(Color.BLACK);

        attempts = 0;
        checkButton.setEnabled(true);

        getContentPane().remove(restartButton.getParent());
        validate();
        repaint();
    }
}
