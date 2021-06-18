import java.awt.*;

public class Player {
    private int x, y, r, SCALE = 2;
    private int dx, dy, speed;
    private int lives;
    private boolean up, down, left, right, firing = false;
    private Color color1;
    private Color color2;
    private long firingTimer = System.nanoTime(), firingDelay = 200;
    private boolean recovering;
    private long recoveryTimer;
    private int score = 0;
    public boolean auto = false;

    public void setScore(int x) {
        score += x;
        if (score < 0) {
            score = 0;
        }
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player() {
        x = GamePanel.WIDTH / 2;
        y = GamePanel.HEIGHT / 2;
        dy = 0;
        dx = 0;
        speed = 5;
        lives = 3;
        r = 5;
        color1 = Color.BLACK;
        color2 = Color.RED;
        firing = false;
        firingTimer = System.nanoTime();
        firingDelay = 200;
        recovering = false;
        recoveryTimer = 0;
    }

    public boolean isRecovering() {
        return recovering;
    }

    public void die() {
        lives--;
        recovering = true;
        recoveryTimer = System.nanoTime();
    }

    public int getLives() {
        return lives;
    }

    public int getR() {
        return r;
    }

    public void setLeft(boolean b) {
        left = b;
    }

    public void setRight(boolean b) {
        right = b;
    }

    public void setUp(boolean b) {
        up = b;
    }

    public void setDown(boolean b) {
        down = b;
    }

    public void setFiring(boolean b) {
        firing = b;
    }

    public void update() {
        if (left) {
            dx = -speed;
        }
        if (right) {
            dx = speed;
        }
        if (up) {
            dy = -speed;
        }
        if (down) {
            dy = speed;
        }
        x += dx;
        y += dy;
        if (x < r)
            x = r;
        if (y < r)
            y = r;
        if (x > GamePanel.WIDTH - r)
            x = GamePanel.WIDTH - r;
        if (y > GamePanel.HEIGHT - r)
            y = GamePanel.HEIGHT - r;
        dx = 0;
        dy = 0;
        if (firing) {
            long elapsed = (System.nanoTime() - firingTimer) / 1000000;
            if (elapsed > firingDelay) {
                GamePanel.bullets.add(new Bullet(270, x, y));
                firingTimer = System.nanoTime();
            }
        }
        long p_elapsed = (int) ((System.nanoTime() - recoveryTimer) * Math.pow(10, -6));
        if (p_elapsed > 3000) {
            recovering = false;
            recoveryTimer = 0;
        }
    }

    public void Draw(Graphics2D g) {
        if (recovering) {
            g.setColor(color2);
            g.fillOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(3));
            g.setColor(color2.darker());
            g.drawOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(color1);
            g.fillOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(3));
            g.setColor(color1.darker());
            g.drawOval(x - r, y - r, 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
        System.out.println(x + " " + y);
    }

    public void aimbot(boolean bool) {
        if (bool) {
            x = GamePanel.WIDTH / 2;
            y = (GamePanel.HEIGHT / 2) + (GamePanel.HEIGHT / 3);

            double x = 0, y = 0, dx = 0, dy = 0, speed = 0;
            for (int i = 0; i < GamePanel.enemies.size(); i++) {
                if (GamePanel.rand_color == GamePanel.enemies.get(i).getColor()) {
                    dx = GamePanel.enemies.get(i).getDx();
                    dy = GamePanel.enemies.get(i).getDy();
                    x = GamePanel.enemies.get(i).getX();
                    y = GamePanel.enemies.get(i).getY();
                    speed = GamePanel.enemies.get(i).getSpeed();
                    break;
                }
            }

            int count = 0;
            double tmpx = x, distx, tmpy_1 = y, tmpy = y, diste, distb, disty, tmpx_0 = x;
            long t_e, t_b;
            while ((tmpx > (this.x - 40) && tmpx < (this.x + 40))) {
                tmpx += dx;
                tmpy += dy;
                count++;
                double floor = Math.floor(tmpx);
                double ceil = Math.ceil(tmpx);
                System.out.println(this.x + " " + "\t" + floor + " " + ceil);
                if ((floor + 1 == this.x || floor - 1 == this.x || ceil + 1 == this.x || ceil - 1 == this.x
                        || floor == this.x || ceil == this.x)
                        && (y < this.y && ceil <= GamePanel.HEIGHT && floor <= GamePanel.HEIGHT)) {
                    distx = Math.abs(this.x - tmpx_0);
                    disty = Math.abs(tmpy - tmpy_1);
                    distb = Math.sqrt((distx * distx) + (Math.abs(tmpy - this.y) * Math.abs(tmpy - this.y)));
                    diste = Math.sqrt((Math.abs(tmpx_0 - tmpx) * Math.abs(tmpx_0 - tmpx)) + (disty * disty));
                    t_e = (long) (diste / speed);
                    t_b = (long) (distb / 14);
                    new java.util.Timer().schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            setFiring(true);
                        }
                    }, (long) (Math.abs(t_e - t_b)));
                    new java.util.Timer().schedule(new java.util.TimerTask() {
                        @Override
                        public void run() {
                            setFiring(false);
                        }
                    }, 300);
                    break;
                }
            }
        } else {
            if (auto) {
                x = GamePanel.WIDTH / 2;
                y = GamePanel.HEIGHT / 2;
            }
        }
    }

}
