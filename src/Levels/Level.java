package Levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Pirate;
import main.Game;
import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetPlayerSpawn;
import static utilz.HelpMethods.GetCrabs;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Pirate> pirates;

    private int lvlTilesWide;
    private int maxTilesOffsetX;
    private int maxLvlOffsetX;

    private int lvlTilesHight;
    private int maxTilesOffsetY;
    private int maxLvlOffsetY;

    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffsetX = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = maxTilesOffsetX * Game.TILES_SIZE;
        lvlTilesHight = img.getHeight();
        maxTilesOffsetY = lvlTilesHight - Game.TILES_IN_HEIGHT;
        maxLvlOffsetY = maxTilesOffsetY * Game.TILES_SIZE;
    }

    private void createEnemies() {
        pirates = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public int getLvlOffsetY() {
        return maxLvlOffsetY;
    }

    public ArrayList<Pirate> getCrabs() {
        return pirates;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
