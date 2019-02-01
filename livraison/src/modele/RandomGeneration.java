package modele;

import java.util.*;

/** A grid generation stategy based on randomness.*/
public class RandomGeneration implements GridStrategy {

  private final RealGrid client;

  public RandomGeneration(RealGrid client) {
        this.client = client;
  }

    /** Fills the game grid with random tiles, with protections
      * to ensure every player can move.
      */
    @Override
    public void generate() {
        Tile[] random_grid=client.getGrid();
        Random r = new Random();
        for (int i=0 ; i<random_grid.length ; i++) {
            Tile n = new FreeTile(i%client.getWidth(),i/client.getWidth());
            double nr = r.nextDouble();
            if (nr < 0.2) {
                n = new Bonus(i%client.getWidth(),i/client.getWidth());
            } else if (nr >= 0.2 && nr < 0.5) {
                n = new Wall(i%client.getWidth(),i/client.getWidth());
            }
            random_grid[i]=n;
        }

        for (Player p : client.getPlayers()) {
            int rx = r.nextInt(client.getWidth());
            int ry = r.nextInt(client.getHeight());
            p.setPosition(rx,ry);
            client.setTileAt(p);
            for (Tile t : client.getNeighbouringTiles(p,1)) {
                if (!(t instanceof Player)) {
                    client.setTileAt(new FreeTile(t.getX(),t.getY()));
                }
            }
        }
        client.setGrid(random_grid);
    }
}
