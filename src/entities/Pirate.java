package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

import main.Game;

public class Pirate extends Enemy {

    public Pirate(float x, float y) {
        super(x, y, PIRATE_WIDTH, PIRATE_HEIGHT, PIRATE);
        initHitbox(x, y, PIRATE_HITBOX_WIDTH, PIRATE_HITBOX_HEIGHT);
        // initHitbox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);
        else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player))
                        turnTowardPlayer(player);
                    if (isplayerCloseForAttack(player))
                        newState(ATTACK);

                    move(lvlData);
                    break;
            }
        }
    }

}
