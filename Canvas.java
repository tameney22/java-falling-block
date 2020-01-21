import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Canvas extends  JPanel {
    boolean game_over = false;
    int player_size = 50;
    int width = Game.WIDTH;
    int height = Game.HEIGHT;
    Random rand = new Random();

    Character player_pos = new Character((Game.WIDTH/2), Game.HEIGHT - (3 * player_size));
    int enemy_size = 50;

    List<Character> enemyList = new ArrayList<Character>();

    int SPEED = 10;
    Color BLUE = Color.BLUE;
    Color RED = Color.RED;
    int font_size = 50;

    int score = 0;

    public Canvas() {
        super();
        setBackground(Color.BLACK);

        InputMap iMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap aMap = this.getActionMap();

        iMap.put(KeyStroke.getKeyStroke("D"), "move");
        iMap.put(KeyStroke.getKeyStroke("A"), "move");
        iMap.put(KeyStroke.getKeyStroke("W"), "move");
        iMap.put(KeyStroke.getKeyStroke("S"), "move");

        aMap.put("move", move);
    }

    Action move = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("a")){
                if(player_pos.getX() > 0 && player_pos.getX() <= width - player_size){
                    player_pos.changeX(-player_size);
                }
            } else if (command.equals("d")) {
                if(player_pos.getX() >= 0 && player_pos.getX() < width - player_size){
                    player_pos.changeX(player_size);
                }
            } else if (command.equals("w")) {
                player_pos.changeY(-player_size);
            } else if (command.equals("s")) {
                player_pos.changeY(player_size);
            }

            repaint();
        }
    };

    @Override
   public void paintComponent(Graphics g) {
      // Highly recommended to allow super to draw first
      super.paintComponent(g);

      if(!game_over) {
            //draw the player
            g.setColor(BLUE);
            g.fillRect(player_pos.getX(), player_pos.getY(), player_size, player_size);
            //draw the enemies
            g.setColor(RED);
            for (Character enemy_pos: enemyList) {
                g.fillRect(enemy_pos.getX(), enemy_pos.getY(), enemy_size, enemy_size);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimeRoman", Font.BOLD, font_size - 10));
            g.drawString("Score: " + score, width - 250, height - font_size - 15);
            
        } else {
            String text = "GAME OVER!";
            String text1 = "Your Score: " + score;
            g.setColor(Color.WHITE);
            g.setFont(new Font("TimeRoman", Font.BOLD, font_size));
            g.drawString(text, (width / 2) - ((font_size * text.length()) / 3), height / 2);
            g.drawString(text1, (width / 2) - ((font_size * text1.length()) / 4), (int)(height * 1.75) / 3);
            
        }
    }

    public void mainGame() throws InterruptedException {
        
        while(!game_over) {
            Thread.sleep(30);
            setLevel();
            generateEnemies();

            dropEnemies();
            if(checkCollision()) {
                game_over = true;
            }
            repaint();
        }
    }

    public void generateEnemies() {
        int delay = rand.nextInt(100);
        if(enemyList.size() < 10 && delay < 10) {
            int x = rand.nextInt(width - enemy_size);
            int y = 0;
            enemyList.add(new Character(x,y));
        }
    }

    public void dropEnemies() {
        Iterator<Character> i = enemyList.iterator();
        while(i.hasNext()) {
            Character enemy_pos = i.next();
            if ((enemy_pos.getY() >= 0) && (enemy_pos.getY() < height)) {
                enemy_pos.incY(SPEED);
            } else {
                i.remove();
                score++;
            }

        }
        
    }

    public boolean checkCollision() {
        for(Character enemy_pos: enemyList) {
            int p_x = player_pos.getX();
            int p_y = player_pos.getY();

            int e_x = enemy_pos.getX();
            int e_y = enemy_pos.getY();

            if (((e_x >= p_x) && (e_x < (p_x + player_size))) || (((p_x >= e_x) && (p_x < (e_x + enemy_size))))) {
                if (((e_y >= p_y) && (e_y < (p_y + player_size))) || (((p_y >= e_y) && (p_y < e_y + enemy_size)))){
                    return true;
                }
            }
        }
        return false;
    }

    public void setLevel() {
        
        if (score < 20) {
            SPEED = 5;
        } else if (score < 40) {
            SPEED = 8;
        } else if (score < 60) {
            SPEED = 10;
        } else {
            SPEED = 15;
        }
    }

}

class Character {
    private int x, y;

    public Character(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void incY(int addend) {
        this.y += addend;
    }

    public void changeX(int change) {
        this.x += change;
    }

    public void changeY(int change) {
        this.y += change;
    }
}