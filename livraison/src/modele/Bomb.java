package modele;

/**
* Class that extends Tile, represents a bomb.
*/
public class Bomb extends Mine {

    private int delay = GameConfig.BOMB_DELAY; //Number of turns before explosion
    private int range = GameConfig.BOMB_RANGE;

    /**
    * Class constructor.
    * @param owner
    * Player that planted the bomb.
    */
    public Bomb(Player owner) {
        super(owner);
        this.damage=GameConfig.BOMB_DAMAGE;
        this.visible=(GameConfig.BOMB_VISIBILITY == 1);
    }

    public Bomb(Player owner, int x, int y) {
        super(owner,x,y);
        this.damage=GameConfig.BOMB_DAMAGE;
        this.visible=(GameConfig.BOMB_VISIBILITY == 1);

    }

    /** Decreases the bomb timer. */
    public void tick() {
        this.delay--;
    }

    /** Explosion method used if a player steps on the bomb.
      * Similar to mine explosion, except for the removal
      * from the list of active bombs.
      * @param g
      * The grid in which the explosion takes place.
      * @param p
      * The player that stepped on the bomb.
      */
    @Override
    public void explode(RealGrid g, Player p) {
        super.explode(g,p);
        g.removeBomb(this);
    }

    /** Explosion method used if the bomb timer has reached 0.
      * @param g
      * The grid in which the explosion takes place.
      */
    public void explode(RealGrid g) {
        if (this.delay==0) {
            for (Tile t : g.getNeighbouringTiles(this, this.range)) {
                if (!(t.isWalkable())) {
                  try {
                    ((Player)t).takeDamage(this.damage);
                  } catch (ClassCastException not_a_player) {}
                }
            }
            g.setTileAt(new FreeTile(this.x,this.y));
            g.removeBomb(this);
        }
    }

    /**
    * hashCode() Override.
    * Necessary for the well functioning of equals' Override.
    * @return The object's hashcode.
    */
    @Override
    public int hashCode() {
        int code=11;
        code+=77*code+this.owner.getX();
        code+=77*code+this.owner.getY();
        return code;
    }

    /**
    * equals Override.
    * Checks the equality of the coordinates.
    * @param o
    * The object to compare to this bomb.
    * @return Equality test result.
    */
    @Override
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        if (!(o instanceof Bomb)) {
            return false;
        }
        Bomb b = (Bomb)o;
        return this.owner.equals(b.getOwner());
    }

    /**
    * Returns the representation of the bomb.
    * @return A character representing the bomb.
    */
    @Override
    public String toString() {
        return ""+this.delay;
    }

    public int getDelay() {
        return delay;
    }
}
