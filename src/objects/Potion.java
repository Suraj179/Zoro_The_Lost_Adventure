package objects;

import main.Game;

public class Potion extends GameObject {
    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        initHitbox(7, 14);
        xDrawOffset = (int) (3 * Game.SCALE);// x distance blw border of image and actual image
        yDrawOffset = (int) (2 * Game.SCALE);

    }

    public void update() {
        updateAnimationTick();
    }
}
