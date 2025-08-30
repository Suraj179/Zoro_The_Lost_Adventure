package entities;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] pirateArr;
    private ArrayList<Pirate> pirates = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        pirates = LoadSave.GetCrabs();
        System.out.println("Size of Crabs: " + pirates.size());
    }

    public void update(int[][] lvlData, Player player) {
        for (Pirate p : pirates) {
            if (p.isActive())
                p.update(lvlData, player);
        }
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawPirates(g, xLvlOffset, yLvlOffset);
    }

    private void drawPirates(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Pirate p : pirates) {
            if (p.isActive()) {
                g.drawImage(pirateArr[p.getEnemyState()][p.getAniIndex()],
                        (int) p.getHitbox().x - xLvlOffset - PIRATE_DRAWOFFSET_X + p.flipX(),
                        (int) p.getHitbox().y - yLvlOffset - PIRATE_DRAWOFFSET_Y,
                        PIRATE_WIDTH * p.flipW(), PIRATE_HEIGHT, null);
                p.drawHitbox(g, xLvlOffset, yLvlOffset);
                p.drawAttackBox(g, xLvlOffset, yLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Pirate p : pirates) {
            if (attackBox.intersects(p.getHitbox())) {
                p.hurt(10);
                return;
            }
        }
    }

    private void loadEnemyImgs() {
        // crabbyArr= new BufferedImage[5][9];
        pirateArr = new BufferedImage[4][6];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.PIRATE_SPRITE);
        for (int j = 0; j < pirateArr.length; j++) {
            for (int i = 0; i < pirateArr[j].length; i++) {
                pirateArr[j][i] = temp.getSubimage(i * PIRATE_WIDTH_DEFAULT, j * PIRATE_HEIGHT_DEFAULT,
                        PIRATE_WIDTH_DEFAULT, PIRATE_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAllEnemies(){
        for(Pirate p: pirates){
            p.resetEnemy();
        }
    }

}
