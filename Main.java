import javax.swing.SwingUtilities;
import javax.swing.JFrame;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CMG1 game = new CMG1();
            game.setExtendedState(JFrame.MAXIMIZED_BOTH);
            game.setVisible(true);
        });
    }
}
