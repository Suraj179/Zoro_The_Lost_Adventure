package objects;

import main.Game;

public class Cannon extends GameObject {

    private int tileY;

    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
        // reAdjusting the Cannon position
        hitbox.x -= (int) (4 * Game.SCALE);
        hitbox.y += (int) (6 * Game.SCALE);

        // xDrawOffset=(int)(Game.SCALE);

    }

    public void update() {
        if (doAnimation)// doAnimation is true iff cannon see player
            updateAnimationTick();
    }

    public int getTileY() {
        return tileY;
    }
    // public void draw(Graphics g){

    // }
}
