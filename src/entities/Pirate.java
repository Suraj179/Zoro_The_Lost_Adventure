package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import main.Game;

public class Pirate extends Enemy {

    //AttackBox
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;
    private int flipOffset=28;

    public Pirate(float x, float y) {
        super(x, y, PIRATE_WIDTH, PIRATE_HEIGHT, PIRATE);
        initHitbox(x, y, PIRATE_HITBOX_WIDTH, PIRATE_HITBOX_HEIGHT);
        
        initAttackBox();
    }   

    private void initAttackBox() {
       attackBox = new Rectangle2D.Float(x,y,(int)(15 * Game.SCALE), (int)(15 * Game.SCALE));
    }

    private void updateAttackBox(){
        if(walkDir == RIGHT){
            attackBox.x = hitbox.x + hitbox.width ;
        }else if(walkDir == LEFT){
            attackBox.x = hitbox.x - hitbox.width + (int)(Game.SCALE * 10);
        }
        attackBox.y = hitbox.y +(Game.SCALE * 10);
    }

    public void update(int[][] lvlData, Player player) {
        updateAttackBox();
        updateBehavior(lvlData, player);
        updateAnimationTick();
    }

    private void updateBehavior(int[][] lvlData, Player player) {
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
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;

                    if(aniIndex ==2 && !attackChecked)//it ensure that in one Attack loop, enemy hit only once to player
                        checkPlayerHit(attackBox,player);
                    break;

                case HIT:
                    break;

            }
        }
    }
    public void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset){
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - xLvlOffset), (int)(attackBox.y - yLvlOffset), (int)attackBox.width, (int)attackBox.height);
    }

    public int flipX(){
        if(walkDir==RIGHT)
            return width+flipOffset;
        else return 0;
    }
    public int flipW(){
        if(walkDir == RIGHT)
            return -1;
        else return 1;
    }

}
