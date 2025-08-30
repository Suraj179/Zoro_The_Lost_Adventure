package entities;

// import utilz.Constants;
import utilz.LoadSave;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.Game;

public class Player extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int playerAction = IDLE;
    // private int playerDir = -1;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float playerSpeed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 10 * Game.SCALE;// in video there was 21
    private float yDrawOffset = 17 * Game.SCALE;

    // Jumping / Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //StatusBarUI
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);

    private int healthBarWidth = (int)(150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth=maxHealth;
    private int healthWidth= healthBarWidth;

    //AttackBox
    private Rectangle2D.Float attackBox;

    private int flipX = 0;
    private int flipW = 1;
    private int flipOffset = 47;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        System.out.println(50-width);
        loadAnimation();
        initHitbox(x, y, (int) (13 * Game.SCALE), (int) (30 * Game.SCALE));// width should no be greater than
                                                                           // TILES_DEFAULT_SIZE
        // initHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox(){
        attackBox = new Rectangle2D.Float(x,y,(int)(20 * Game.SCALE), (int)(20*Game.SCALE));

    }

    private void updateAttackBox(){
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (10*Game.SCALE) ;
        }else if(left){
            attackBox.x = hitbox.x - hitbox.width -(20*Game.SCALE);
        }
        attackBox.y = hitbox.y +(Game.SCALE * 10);
    }


    public void update() {
        updateAttackBox();
        updateHealthBar();
        updatePosition();
        updateAnimationTick();
        setAnimation();

    }

    private void updateHealthBar() {
        healthWidth= (int)((currentHealth/(float) maxHealth) * healthBarWidth);
        
    }

    public void render(Graphics g, int xlvlOffset, int ylvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], 
                (int) (hitbox.x - xDrawOffset) - xlvlOffset + flipX,
                (int) (hitbox.y - yDrawOffset) - ylvlOffset, 
                width * flipW, height, null);
        drawHitbox(g, xlvlOffset, ylvlOffset);
        drawAttackBox(g, xlvlOffset, ylvlOffset);
        drawUI(g);

    }

    private void drawAttackBox(Graphics g, int xlvlOffset, int ylvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x - xlvlOffset, (int)attackBox.y - ylvlOffset, (int)attackBox.width, (int)attackBox.height);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updateAnimationTick() {
        aniTick++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(playerAction)) {
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            playerAction = JUMP;
        }

        if (attacking) {
            playerAction = ATTACK1;
        }

        if (startAni != playerAction) {
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePosition() {

        moving = false;
        if (jump) {
            jump();
        }

        if (!inAir) {
            if ((!left && !right) || (right && left)) {
                return;
            }
        }
        float xSpeed = 0;

        if (left){            
            xSpeed -= playerSpeed;
            flipX = width - flipOffset;
            flipW = -1;
        }
        if (right){
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        if (inAir) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);

            } else {

                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);

                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }
        moving = true;

    }

    private void jump() {
        if (inAir) {
            return;
        }
        inAir = true;

        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value){
        currentHealth +=value;
        if(currentHealth<=0){
            currentHealth=0;
        }
        else if(currentHealth>=maxHealth){
            currentHealth=maxHealth;
        }

    }

    private void loadAnimation() {

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[15][15];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 100, j * 100, 100, 100);
            }
        }
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
        
        
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData)) {
            inAir = true;

        }
    }

    public void resetDirBooleans() {
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public void setAttack(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
