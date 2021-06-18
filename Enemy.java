import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Enemy {
    private double x, y;
    private static double r;

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    private double dx;
    private double dy;

    public double getRad() {
        return rad;
    }

    private double rad;

    public double getSpeed() {
        return speed;
    }

    private double speed;
    private int health, type;
    private int rank;
    private Color color;
    private boolean ready, dead;
    public static Random rand = new Random();

    public String getRand_int() {
        return rand_int;
    }

    private String rand_int;

    public static int rand_indx = rand.nextInt(6);
    public static ArrayList<Color> colors = new ArrayList<>();
    public static Map<Color, String> color_ops = Map.of(Color.PINK, "Pink", Color.BLUE, "Blue", Color.MAGENTA,
            "Magenta", Color.YELLOW, "Yellow", Color.GRAY, "Gray", Color.GREEN, "Green", Color.ORANGE, "Orange");
    private Color[] map_keys = { Color.PINK, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GRAY, Color.GREEN,
            Color.ORANGE };

    public Enemy(int type, int rank) {
        this.type = type;
        this.rank = rank;
        this.rand_int = "" + (2 + rand.nextInt(100));
        int flag;
        while (true) {
            flag = 0;
            this.color = map_keys[rand.nextInt(6)];
            for (int j = 0; j < colors.size(); j++) {
                if (color == colors.get(j)) {
                    flag = 1;
                    break;
                }
            }
            if (color != Color.WHITE && flag != 1) {
                colors.add(this.color);
                break;
            }
        }
        speed = 1 + rand.nextInt(4);
        r = 15;
        health = 1;
        x = Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4;
        y = -r;
        double angle;
        while (true) {
            angle = Math.random() * 140 + 20;
            if (angle != 270 && angle > 30) {
                break;
            }
        }
        rad = Math.toRadians(angle);
        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;
        ready = false;
        dead = false;
    }

    public Enemy(Enemy tmp) {
        this.type = tmp.type;
        this.rank = tmp.rank;
        this.health = 1;
        this.rand_int = tmp.rand_int;
        this.r = tmp.r;
        this.color = tmp.color;
        colors.add(tmp.color);
        speed = 4;
        x = Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4;
        y = -r;
        double angle;
        while (true) {
            angle = Math.random() * 140 + 20;
            if (angle != 270 && angle > 30) {
                break;
            }
        }
        rad = Math.toRadians(angle);
        dx = Math.cos(rad) * speed;
        dy = Math.sin(rad) * speed;
        ready = false;
        dead = false;

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double getR() {
        return r;
    }

    public boolean isDead() {
        return dead;
    }

    public Color getColor() {
        return color;
    }

    public void hit() {
        health--;
        if (health <= 0) {
            dead = true;
        }
    }

    public void update() {
        x += dx;
        y += dy;
        if (!ready) {
            if (x > y && x < GamePanel.WIDTH - r && y > r && y < GamePanel.HEIGHT - r) {
                ready = true;
            }
        }
        if (x < r && dx < 0)
            dx = -dx;
        if (y < r && dy < 0)
            dy = -dy;
        if (x > GamePanel.WIDTH - r && dx > 0)
            dx = -dx;
        if (y > GamePanel.HEIGHT - r && dy > 0)
            dy = -dy;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int) (x - r), (int) (y - r), (int) (2 * r), (int) (2 * r));

        g.setStroke(new BasicStroke(3));
        g.setColor(color.darker());
        g.drawOval((int) (x - r), (int) (y - r), (int) (2 * r), (int) (2 * r));
        g.setStroke(new BasicStroke(1));
        FontMetrics fm = g.getFontMetrics();
        double textWidth = fm.getStringBounds(rand_int, g).getWidth();
        g.setColor(Color.WHITE);
        g.drawString(rand_int, (int) (x - textWidth / 2), (int) (y + fm.getMaxAscent() / 2));
    }
}