import java.awt.*;

import javax.swing.*;

class Game extends JFrame {
    static int WIDTH = 800;
    static int HEIGHT = 600;

    private Canvas canvas;

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        canvas = new Canvas();
        add(canvas);
    }

    public static void main(String args[]) {
        Game game = new Game();
        game.setVisible(true);
        try {
            game.canvas.mainGame();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}