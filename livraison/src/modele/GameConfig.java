package modele;

import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.io.*;

/** Class containing all static parameters used in the game, read from a text file.*/
public final class GameConfig {

    public static int RANDOMIZED_PLAYER_ORDER;
    public static int PLAYER_BASE_HP;
    public static int PLAYER_BASE_AP;
    public static int PLAYER_FOV;
    public static int MOVE_COST;
    public static int SHIELD_COST;
    public static int PLANT_COST;
    public static int FIRE_COST;
    public static int BOMB_BASE_COUNT;
    public static int BOMB_DAMAGE;
    public static int BOMB_DELAY;
    public static int BOMB_RANGE;
    public static int BOMB_VISIBILITY;
    public static int MINE_BASE_COUNT;
    public static int MINE_DAMAGE;
    public static int MINE_VISIBILITY;
    public static int RIFLE_BASE_AMMO;
    public static int RIFLE_DAMAGE;
    public static int RIFLE_RANGE;
    public static int BONUS_VALUE;

    public GameConfig() {
        this.assignParameters();
    }

    /** Reads a text file and assigns the static variables with the values stored in the file.*/
    public void assignParameters() {
        ArrayList<Integer> parameters = new ArrayList<>();
        try {
            Path file_path = FileSystems.getDefault().getPath("config.txt");
            List<String> d = Files.readAllLines(file_path, StandardCharsets.UTF_8);
            for (String line : d) {
                String[] s = line.split("=");
                parameters.add(Integer.parseInt(s[1]));
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        GameConfig.RANDOMIZED_PLAYER_ORDER=parameters.get(0);
        GameConfig.PLAYER_BASE_HP=parameters.get(1);
        GameConfig.PLAYER_BASE_AP=parameters.get(2);
        GameConfig.PLAYER_FOV=parameters.get(3);
        GameConfig.MOVE_COST=parameters.get(4);
        GameConfig.SHIELD_COST=parameters.get(5);
        GameConfig.PLANT_COST=parameters.get(6);
        GameConfig.FIRE_COST=parameters.get(7);
        GameConfig.BOMB_BASE_COUNT=parameters.get(8);
        GameConfig.BOMB_DAMAGE=parameters.get(9);
        GameConfig.BOMB_DELAY=parameters.get(10);
        GameConfig.BOMB_RANGE=parameters.get(11);
        GameConfig.BOMB_VISIBILITY=parameters.get(12);
        GameConfig.MINE_BASE_COUNT=parameters.get(13);
        GameConfig.MINE_DAMAGE=parameters.get(14);
        GameConfig.MINE_VISIBILITY=parameters.get(15);
        GameConfig.RIFLE_BASE_AMMO=parameters.get(16);
        GameConfig.RIFLE_DAMAGE=parameters.get(17);
        GameConfig.RIFLE_RANGE=parameters.get(18);
        GameConfig.BONUS_VALUE=parameters.get(19);
    }
}
