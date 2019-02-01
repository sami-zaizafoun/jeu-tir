package graphics;

import static java.lang.Thread.sleep;
import modele.*;

/**
 * View console of the game. This view is not displaying grid viewed by the current player.
 * @author quentindeme
 */
public class ViewConsole implements ModelListener{

    private final Tile[] entities;
    private final Game game;
    private Player playerToPlay;
    private ThreadPlay threadPlay = null;
    /**
     * View console's constructor.
     * @param entities entities to display. (constituating the map)
     * @param game Model to listen.
     */
    public ViewConsole(Tile[] entities, Game game){
        this.entities = entities;
        this.game = game;
        game.addListener(this);
    }

    /**
     * Display basic view (equivalent to paintComponent method in View class)
     */
    public void display(){
        System.out.println("On crée un nouveau Thread"+ThreadPlay.counterInstance);
        threadPlay = new ThreadPlay(game);
        System.out.println("\033[H\033[2J");
        System.out.println("================ STRATEGY GAME =================\n");
        System.out.println("Tour " + game.getGrid().getTurnNumber());
        System.out.println(playerToPlay.getName() + "\n");
        System.out.println(playerToPlay.getView() + "\n");//player's view

        threadPlay.start();
    }

    /**
     * Update the view when game has changed
     * @param source
     */
    @Override
    public void update(Object source) {
        playerToPlay = game.getGrid().getPlayerToPlay();
        if(threadPlay != null){
            threadPlay.interrupt();
            threadPlay = null;
        }
        try {
            sleep(100);
        } catch (InterruptedException ex) {
            //Logger.getLogger(ViewConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
        display();
    }
}
