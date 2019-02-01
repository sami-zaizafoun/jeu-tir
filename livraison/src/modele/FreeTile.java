package modele;

import java.awt.image.BufferedImage;

/**
* Class that extends Tile, represents an empty tile.
*/
public class FreeTile extends Tile {

    /**
    * Class constructor.
    * @param x
    * Square ordinate.
    * @param y
    * Square abscissa.
    */
    public FreeTile(int x, int y){
        super(x,y);
    }

    public FreeTile(int x, int y, BufferedImage img){
        super(x,y,img);
    }

    /**
    * Returns the representation of an empty tile.
    * @return a character representing an empty tile.
    */

    @Override
    public String toString() {
        return "_";
    }

    public String printCoords() {
        return "(" + this.x + "," + this.y + ")";
    }
}
