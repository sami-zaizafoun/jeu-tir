package modele;

import java.util.*;

/** Factory class for player creation.*/
public final class PlayerFactory {

    private static PlayerFactory instance = null;
    public static int nb_instances = 0;
    public static ArrayList<Weapon> inventory = new ArrayList<Weapon>();

    /** Singleton class constructor.*/
    private PlayerFactory() {
        super();
        weapons();
    }

    /** Creates an instance if none exists, or returns a pointer to the instance.*/
    public final static PlayerFactory getInstance() {
        if (PlayerFactory.instance == null) {
            PlayerFactory.instance = new PlayerFactory();
        }
        return PlayerFactory.instance;
    }

    /** Makes a static list of all weapons in the game used to create map keys in player      *
      * loadouts.
      * @return the list of weapons a player can use.
      */
    public ArrayList<Weapon> weapons() {
        inventory.add(new Rifle(new Player(new RealGrid(),"","")));
        inventory.add(new Mine(new Player(new RealGrid(),"","")));
        inventory.add(new Bomb(new Player(new RealGrid(),"","")));
        return inventory;
    }

    /** General player creation method.
      * @param g
      * The game grid where the player will play.
      * @param classname
      * Class to which the player belongs.
      * @param hp_mod
      * Value of the bonus (or malus) life points given to the player,
      * compared to the base value in the config file.
      * @param ap_mod
      * Action points modifier.
      * @param rifle_ammo_mod
      * Starting rifle ammunition modifier.
      * @param bombs_mod
      * Starting bomb count modifier.
      * @param mines_mod
      * Starting mine count modifier.
      * @return an instance of Player.
      */
    public Player build(RealGrid g, String classname, int hp_mod, int ap_mod, int rifle_ammo_mod, int bombs_mod, int mines_mod) {
        PlayerFactory.nb_instances++;
        Player p = new Player(g,"Player "+PlayerFactory.nb_instances, classname);
        p.setLife(p.getLife()+hp_mod);
        p.setBaseEnergy(p.getBaseEnergy()+ap_mod);
        p.addWeapon(PlayerFactory.inventory.get(0), GameConfig.RIFLE_BASE_AMMO+rifle_ammo_mod);
        p.addWeapon(PlayerFactory.inventory.get(1), GameConfig.MINE_BASE_COUNT+mines_mod);
        p.addWeapon(PlayerFactory.inventory.get(2), GameConfig.BOMB_BASE_COUNT+bombs_mod);
        return p;
    }

    /* Player classes*/
    public Player buildBasic(RealGrid g) {
        return build(g,"Basic",0,0,0,0,0);
    }

    public Player buildTank(RealGrid g) {
        return build(g,"Tank",20,0,0,0,0);
    }

    public Player buildMarksman(RealGrid g) {
        return build(g,"Marksman",-5,4,30,-5,-2);
    }

    public Player buildEngineer(RealGrid g) {
        return build(g,"Engineer",0,0,-15,5,3);
    }
}
