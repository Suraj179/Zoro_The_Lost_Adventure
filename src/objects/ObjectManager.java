package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Levels.Level;
import entities.Player;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import static utilz.Constants.ObjectConstant.*;
import static utilz.Constants.PlayerConstants.ATTACK1;
import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.*;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage spikeImg, cannonBallImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikesTouched(Player p) {
        for (Spike s : spikes)
            if (s.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
    }

    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else if (p.getObjType() == BLUE_POTION)
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {// check if object got hit
        for (GameContainer gc : containers)
            if (gc.isActive() && !gc.doAnimation) {
                if (gc.getHitbox().intersects(attackbox)) {
                    gc.setAnimation(true);
                    int type = 0; // default container type is box
                    if (gc.getObjType() == BARREL)
                        type = 1;
                    potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2),
                            (int) (gc.getHitbox().y - gc.getHitbox().height * 1 / 3),
                            type));
                    return;

                }
            }
    }

    public void checkCannonBallHit(Rectangle2D.Float attackbox) {
        for (Projectile p : projectiles) {
            if (p.isActive()) {
                if (p.getHitbox().intersects(attackbox)) {
                    p.setDir(-1 * p.getDir());
                }
            }
        }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        projectiles.clear();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];

        for (int j = 0; j < potionImgs.length; j++)
            for (int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);
            }

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);

        for (int i = 0; i < cannonImgs.length; i++)
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);

    }

    public void update(int[][] lvlData, Player player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();
        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p : projectiles) {
            if (p.isActive())
                p.updatePos();
            if (p.isActive() && p.getHitbox().intersects(player.getHitbox())) {
                player.changeHealth(-20);//
                p.setActive(false);
            } else if (IsProjectileHittingLevel(p, lvlData)) {
                p.setActive(false);
            }

        }
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) (Math.abs(player.getHitbox().x - c.getHitbox().x));
        return absValue <= Game.TILES_SIZE * 7;
    }

    private boolean isPlayerInfrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;
        } else if (c.getObjType() == CANNON_RIGHT) {
            if (c.getHitbox().x < player.getHitbox().x)
                return true;
        }
        return false;

    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true);

            c.update();
            if (c.getAniIndex() == 4 && c.getAniTick() == 0)
                shootCannon(c);
        }
        /*
         * we will check the following only if Cannon is not Animating
         * tileY is same
         * ifPlayer is in range
         * line of sight
         * if all true the shoot the cannon
         */
    }

    private void shootCannon(Cannon c) {
        int dir = 1;// default diraction is right, ball move toward right direction
        if (c.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
    }

    public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
        drawPotions(g, xLvlOffset, yLvlOffset);
        drawContainer(g, xLvlOffset, yLvlOffset);
        drawTraps(g, xLvlOffset, yLvlOffset);
        drawCannons(g, xLvlOffset, yLvlOffset);
        drawProjectiles(g, xLvlOffset, yLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y - yLvlOffset),
                        CANNON_BALL_WIDTH, CANNON_BALL_HIGHT, null);
    }

    private void drawCannons(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;
            if (c.getObjType() == CANNON_RIGHT) {// we will flip the image if cannon should face right side else we will
                                                 // not flip
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y - yLvlOffset), width, CANNON_HEIGHT,
                    null);

        }
    }

    private void drawTraps(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Spike s : spikes)
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset),
                    (int) (s.getHitbox().y - s.getyDrawOffset() - yLvlOffset), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    private void drawContainer(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjType() == BOX)
                    type = 0;

                else if (gc.getObjType() == BARREL)
                    type = 1;

                g.drawImage(containerImgs[type][gc.getAniIndex()],
                        (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                        (int) gc.getHitbox().y - gc.getyDrawOffset() - yLvlOffset,
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null);
            }

    }

    private void drawPotions(Graphics g, int xLvlOffset, int yLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == BLUE_POTION)
                    type = 0;

                else if (p.getObjType() == RED_POTION) {
                    type = 1;
                }

                g.drawImage(potionImgs[type][p.getAniIndex()],
                        (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                        (int) p.getHitbox().y - p.getyDrawOffset() - yLvlOffset,
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null);

            }
    }

    public void resetAllObjects() {
        System.out.println("Size of Arrays: " + potions.size() + " - " + containers.size());
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (Potion p : potions)
            p.reset();
        for (GameContainer gc : containers)
            gc.reset();
        for (Cannon c : cannons)
            c.reset();

        // System.out.println("Size of Arrays after: "+ potions.size() + " -
        // "+containers.size());
    }
}
