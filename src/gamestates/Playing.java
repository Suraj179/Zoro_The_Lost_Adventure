package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import Levels.LevelManager;
import entities.EnemyManager;
import entities.Player;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.util.Random;

import static utilz.Constants.Environment.*;
import static utilz.Constants.PlayerConstants.*;

public class Playing extends State implements Statemethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    // private Projectile projectiles;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private int yLvlOffset;
    private int upBorder = (int) (0.3 * Game.GAME_HEIGHT);
    private int downBorder = (int) (0.7 * Game.GAME_HEIGHT);
    private int maxLvlOffsetY;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean lvlCompleted = false;
    private boolean playerDying=false;

    // the become true when A or D is pressed. J and Attack 3 works only when left
    // and rightpressed is true
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++)
            smallCloudsPos[i] = (int) ((90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE)));

        calcLvlOffset();
        loadStartLevel();

    }

    public void loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffsetX();
        maxLvlOffsetY = levelManager.getCurrentLevel().getLvlOffsetY();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);

        player = new Player(160 * Game.SCALE, 330 * Game.SCALE, (int) (64 * Game.SCALE), (int) (64 * Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
        } 
        else if (gameOver){
            gameOverOverlay.update();
        }
        else if (playerDying){//gameOver will be true iff playerDying is true
            player.update();
        }else {
            levelManager.update();
            objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorderX();
            checkCloseToBorderY();
        }
    }

    private void checkCloseToBorderY() {
        int playerY = (int) player.getHitbox().y;
        int diff = playerY - yLvlOffset;

        if (diff > downBorder)
            yLvlOffset += diff - downBorder;
        else if (diff < upBorder)
            yLvlOffset += diff - upBorder;

        if (yLvlOffset > maxLvlOffsetY)
            yLvlOffset = maxLvlOffsetY;
        else if (yLvlOffset < 0)
            yLvlOffset = 0;
    }

    private void checkCloseToBorderX() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);

        levelManager.draw(g, xLvlOffset, yLvlOffset);
        player.render(g, xLvlOffset, yLvlOffset);
        enemyManager.draw(g, xLvlOffset, yLvlOffset);
        objectManager.draw(g, xLvlOffset, yLvlOffset);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++)
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (170 * Game.SCALE),
                    BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        for (int i = 0; i < smallCloudsPos.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i],
                    SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying=false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void checkObjectHit(Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }
    
    public void checkCannonBallHit(Float attackBox) {
        objectManager.checkCannonBallHit(attackBox);
    }

    public void checkEnemyHit(Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox){
        objectManager.checkObjectTouched(hitbox);
    }

    public void checkSpikesTouched(Player p) {
        objectManager.checkSpikesTouched(p);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1){
                player.setAttacking(true);
                player.setAttackType(ATTACK1);
            }
            else if (e.getButton()==MouseEvent.BUTTON3)
                player.powerAttack();
            

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
            } else if (lvlCompleted) {
                levelCompletedOverlay.mousePressed(e);
            }
        }else
            gameOverOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            } else if (lvlCompleted) {
                levelCompletedOverlay.mouseReleased(e);
            }
        }else
            gameOverOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if (lvlCompleted) {
                levelCompletedOverlay.mouseMoved(e);
            }else
                gameOverOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused) {
                pauseOverlay.mouseDragged(e);
            }
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if(levelCompleted)
            game.getAudioPlayer().lvlCompleted();
    }

    public void setMaxLvlOffset(int lvlOffsetX, int lvlOffsetY) {
        this.maxLvlOffsetX = lvlOffsetX;
        this.maxLvlOffsetY = lvlOffsetY;

    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    public ObjectManager getObjectManager(){
        return objectManager;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println("leftPressed: " + leftPressed + "   Right Pressed: " + rightPressed);
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:
                    leftPressed = true;
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_A:
                    leftPressed = true;
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = true;
                    player.setRight(true);
                    break;
                case KeyEvent.VK_D:
                    rightPressed = true;
                    player.setRight(true);
                    break;
                case KeyEvent.VK_F:
                    player.setAttacking(true);
                    player.setAttackType(ATTACK1);
                    break;
                case KeyEvent.VK_J:
                    if ((leftPressed && !rightPressed) || (rightPressed && !leftPressed)) {
                        player.setAttacking(true);
                        player.setAttackType(ATTACK3);
                    }
                    break;
                case KeyEvent.VK_R:
                    player.setAttacking(true);
                    player.setAttackType(ATTACK2);
                    break;
                case KeyEvent.VK_E:
                    player.setAttacking(true);
                    player.setAttackType(ATTACK4);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (!gameOver)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    leftPressed = false;
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_A:
                    leftPressed = false;
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = false;
                    player.setRight(false);
                    break;
                case KeyEvent.VK_D:
                    rightPressed = false;
                    player.setRight(false);
                    break;
                case KeyEvent.VK_J:
                    player.setAttacking(false);
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
            }
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying= playerDying;
    }

    

  

   
}
