import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends Canvas implements Runnable, KeyListener {
    public static int WIDTH = 800;
    public static int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 1;
    public static final String TITLE = "Bubble Shooter";
    private Thread thread;
    private boolean running = false;
    private BufferedImage image;
    private Graphics2D g;
    private int FPS = 30;
    private double averageFPS;
    public static Player player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;
    public static Color rand_color;
    private int tmp;
    private long startTime, URDTimeMilli, waitTime;
    private boolean flag = false;

    Thread t1 = new Thread(new Runnable() {
        @Override
        public void run() {
            while (running) {
                if (flag) {
                    player.aimbot(true);
                    if (URDTimeMilli != 0) {
                        try {
                            Thread.sleep(Math.abs(waitTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    player.aimbot(false);
                }
            }

        }
    });

    public GamePanel() {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        requestFocus();
    }

    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();

        }
        addKeyListener(this);
    }

    public void run() {
        running = true;
        image = new BufferedImage(WIDTH * SCALE, HEIGHT * SCALE, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        player = new Player();
        bullets = new ArrayList<Bullet>();
        enemies = new ArrayList<Enemy>();
        for (int i = 0; i < 6; i++) {
            enemies.add(new Enemy(1, 1));
        }
        tmp = Enemy.rand.nextInt(Integer.parseInt(enemies.get(Enemy.rand_indx).getRand_int()) - 1);

        int frameCount = 0;
        long maxFrameCount = 30;
        long totalTime = 0;
        long targetTime = 1000 / FPS;
        t1.start();
        while (running) {
            startTime = System.nanoTime();
            gameUpdate();
            gameRender();
            gameDraw();
            URDTimeMilli = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - URDTimeMilli;
            try {
                Thread.sleep(Math.abs(waitTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == maxFrameCount) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    private void gameUpdate() {
        player.update();
        for (int i = 0; i < bullets.size(); i++) {
            boolean remove = bullets.get(i).update();
            if (remove) {
                bullets.remove(i);
                i--;
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).update();
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            double bx = b.getX();
            double by = b.getY();
            double br = b.getR();
            for (int j = 0; j < enemies.size(); j++) {
                Enemy e = enemies.get(j);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();

                double dx = bx - ex; // x2 - x1
                double dy = by - ey; // y2 - y1
                double dist = Math.sqrt(dx * dx + dy * dy);// distance between 2 points root(( x2 - x1 )^2 - ( y2 - y1
                                                           // )^2)

                if (dist < br + er) {
                    e.hit();
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }
        Enemy tmp_e;
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).isDead()) {
                if (enemies.get(i).getColor() == rand_color) {
                    player.setScore(2);
                } else {
                    player.setScore(-1);
                }
                tmp_e = enemies.get(i);
                if (i == Enemy.rand_indx) {
                    player.setScore(4);
                    Enemy.rand_indx = 1 + Enemy.rand.nextInt(5);
                    tmp = Enemy.rand.nextInt(Integer.parseInt(enemies.get(Enemy.rand_indx).getRand_int()) - 1);
                }
                Enemy.colors.remove(enemies.get(i).getColor());
                enemies.remove(i);
                enemies.add(new Enemy(tmp_e));
            }
        }
        if (!player.isRecovering()) {
            int px = player.getX();
            int py = player.getY();
            int pr = player.getR();
            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
                double ex = e.getX();
                double ey = e.getY();
                double er = e.getR();
                double dx = px - py;
                double dy = py - ey;
                double dist = Math.sqrt(Math.pow(dx, 2) * Math.pow(dy, 2));
                if (dist < pr + er) {
                    player.die();
                }
            }
        }

    }

    private void gameRender() {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
        g.setColor(Color.BLACK);
        player.Draw(g);
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        // Waves
        g.setColor(Color.BLACK);
        g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
        rand_color = Enemy.colors.get((int) (Math.random()));

        String s = "Kill the " + Enemy.color_ops.get(rand_color) + " Also what is " + tmp + " + "
                + (Integer.parseInt(enemies.get(Enemy.rand_indx).getRand_int()) - tmp);
        int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
        /**
         * for (int i = 0; i < player.getLives(); i++) { g.setColor(Color.RED);
         * g.fillOval(20 + (20 * i), 20, player.getR() * 2, player.getR() * 2);
         * g.setColor(Color.RED.darker()); g.fillOval(20 + (20 * i), 20, player.getR() *
         * 2, player.getR() * 2); g.setStroke(new BasicStroke(1)); }
         **/
        g.setColor(Color.BLUE);
        g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        g.drawString("Score: " + player.getScore(), WIDTH - 100, 30);

    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
        int ketCode = key.getKeyCode();
        if (ketCode == KeyEvent.VK_LEFT) {
            player.setLeft(true);
        }
        if (ketCode == KeyEvent.VK_RIGHT) {
            player.setRight(true);
        }
        if (ketCode == KeyEvent.VK_UP) {
            player.setUp(true);
        }
        if (ketCode == KeyEvent.VK_DOWN) {
            player.setDown(true);
        }
        if (ketCode == KeyEvent.VK_Z) {
            player.setFiring(true);
        }
        if (ketCode == KeyEvent.VK_X) {
            flag = true;
            player.auto = true;
        }
        if (ketCode == KeyEvent.VK_C) {
            flag = false;
            player.auto = false;
        }
    }

    public void keyReleased(KeyEvent key) {
        int ketCode = key.getKeyCode();
        if (ketCode == KeyEvent.VK_LEFT) {
            player.setLeft(false);
        }
        if (ketCode == KeyEvent.VK_RIGHT) {
            player.setRight(false);
        }
        if (ketCode == KeyEvent.VK_UP) {
            player.setUp(false);
        }
        if (ketCode == KeyEvent.VK_DOWN) {
            player.setDown(false);
        }
        if (ketCode == KeyEvent.VK_Z) {
            player.setFiring(false);
        }

    }

}