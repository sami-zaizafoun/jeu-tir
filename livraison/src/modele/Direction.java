package modele;

import java.util.ArrayList;

/**
 *  Class used for movements. We use it by adding a direction's value to the object's position.
 */
public enum Direction {
    z("z",0,-1),
    s("s",0,1),
    q("q",-1,0),
    d("d",1,0);
    int x;
    int y;
    String repr;
    /**
    * Construct a new vector Direction
    * @param repr Representation of the direction
    * @param xx x value
    * @param yy y value
    */
    Direction(String repr, int xx, int yy){
        this.repr = repr;        
        this.x = xx;        
        this.y = yy;    
    }
    
    /**
    * Get x value
    * @return x int value of the direction
    */
    public int x() {
        return x;
    }

    /**
    * Get y value
    * @return y int value of the direction
    */
    public int y() {
        return y;
    }

    /**
     * Gets all the possible directions 
     * @return ArrayList of the Directions
     */
    public static ArrayList<Direction> getDirections(){
        ArrayList<Direction> listDir = new ArrayList<>();
        listDir.add(z);
        listDir.add(q);
        listDir.add(s);
        listDir.add(d);
        return listDir;
    }

    /**
     * Get the opposite vector of a given Direction
     * @param dir Direction from which we want the opposite
     * @return Direction opposite
     */
    public static Direction getOpposite(Direction dir) {
        if(dir.x == 0 && dir.y == -1) {
            return s;
        }else if(dir.x == 0 && dir.y == 1) {
            return z;
        }else if(dir.x == -1 && dir.y == 0) {
            return d;
        }else {
            return q;
        }
    }

    /**
     * String representation of a Direction
     * @return 
     */
    @Override
    public String toString() {
        return repr;
    }
}
