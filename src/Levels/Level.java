package Levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Pirate;
import main.Game;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;
import utilz.HelpMethods;

import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetPlayerSpawn;
import static utilz.HelpMethods.GetCrabs;

public class Level {
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Pirate> pirates;
    private ArrayList<Potion>potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;

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
        createPotions();
        createContainers();
        createSpikes();

        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void createSpikes() {
        spikes = HelpMethods.GetSpikes(img);
    }

    private void createContainers() {
       containers = HelpMethods.GetContainer(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
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

    public ArrayList<Potion> getPotions(){
        return potions;
    }

    public ArrayList<GameContainer> getContainers(){
        return containers;
    }

    public ArrayList<Spike> getSpikes(){
        return spikes;
    }
}
