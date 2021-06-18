import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        GamePanel game = new GamePanel();
        JFrame window = new JFrame(GamePanel.TITLE);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(game);
        window.pack();
        window.setVisible(true);
    }
}