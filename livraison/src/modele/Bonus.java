package modele;

import java.awt.image.BufferedImage;

/**
* Class that extends Tile, represents a bonus.
*/

public class Bonus extends Tile {

  private final int value = GameConfig.BONUS_VALUE;

    /**
    * Class constructor.
    * @param x
    * Square ordinate.
    * @param y
    * Square abscissa.
    */
    public Bonus(int x, int y) {
        super(x,y);
    }

    public Bonus(int x, int y, BufferedImage img){
        super(x,y,img);
    }

    public int getValue() {
        return this.value;
    }

    /** Permanently increases the amount of action points restored to
      * a player each turn.
      * @param p
      * Player receiving the boost.
      */
    public void boost(Player p) {
      //p.setEnergy(p.getEnergy()+this.value);
      p.setBaseEnergy(p.getBaseEnergy()+1);
    }

    /**
    * Returns the representation of the bonus.
    * @return A character representing the bonus.
    */
    @Override
    public String toString() {
        return ".";
    }
}
