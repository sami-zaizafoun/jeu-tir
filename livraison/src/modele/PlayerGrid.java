package modele;

/**
  * Proxy of RealGrid allowing a separate view for each player
*/
public class PlayerGrid implements Grid {

    private final RealGrid model;
    private final Player client;
    private boolean alt_string =false;

    /** Class constructor.
      * @param model
      * The game's grid whose instance is the proxy
      * @param client
      * The player destined to that instance
    */
    public PlayerGrid(RealGrid model, Player client) {
        this.model = model;
        this.client = client;
    }

    /** Getter method of RealGrid.
      * @return RealGrid related to a proxy.
    */
    public RealGrid getModel() {
        return this.model;
    }

    /** Determines if the player can see a Tile in question.
      * @param t
      * Tile to test.
      * @return test result.
    */
    public boolean playerCanSee(Tile t) {
        return (t.isVisible() || (!(t.isVisible()) && (((Weapon)t).getOwner().equals(this.client))));
    }

    /** Modified getter of the tile with the coordinates in argument, can change the tile's content
      * if the player is not supposed to see it.
      * @param x
      * Square ordinate.
      * @param y
      * Square abscissa.
      * @return The tile's content of the player is allowed to see it, otherwise an empty tile.
    */
    @Override
    public Tile getTileAt(int x, int y) {
        Tile test = model.getTileAt(x,y);
        return playerCanSee(test) ? test : new FreeTile(x,y);
    }

    /** Method delegation to RealGrid for code readability purposes.
      * @param t
      * Tile to mutate.
    */
    @Override
    public void setTileAt(Tile t) {
        model.setTileAt(t);
    }

    /** Method delegation to RealGrid for code readability purposes.
      * @param b
      * Bomb to add.
    */
    public void addBomb(Bomb b) {
        model.addBomb(b);
    }

    /** Provides a string representing what the proxy client is allowed to see.
      * @return a string display of the game grid suited for the client.
    */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i=0 ; i<model.getGrid().length ; i++) {
            if (i>0 && i%model.getWidth() == 0) {
                res.append("\n");
            }
            if (playerCanSee(model.getGrid()[i])) {
                res.append(model.getGrid()[i]);
            } else {
                res.append(new FreeTile(-1,-1).toString());//affichage de FreeTile si la case est une bombe ou une mine adverse
            }
        }
        return res.toString();
    }

    /** Alternate display method used in PrintThread.
      * This method makes the active player blink when a human is playing.
      * @return two strings alternating to crate a blinking animation in command prompt.
    */
    public String toStringForThread() {
        StringBuilder res_thread = new StringBuilder(this.toString());
        int position = client.getX()+(client.getY()*(this.getModel().getWidth()+1));
        res_thread.replace(position,position+1,new FreeTile(-1,-1).toString());
        alt_string=!alt_string;
        return alt_string ? this.toString() : res_thread.toString();
    }
}
