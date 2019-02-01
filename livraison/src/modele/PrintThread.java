package modele;

/**
* Thread used to improve the console display if a human is playing.
*/
public class PrintThread implements Runnable {
    private final RealGrid grid;
    private final Player active_player;

    /** Class constructor.
     * @param grid
     * The game grid.
     * @param active_player
     * The player currently playing his turn.
    */
    public PrintThread(RealGrid grid, Player active_player) {
        this.grid = grid;
        this.active_player = active_player;
    }

    /** 
     * Prints the console interface with alternating grid displays, making the active player blink.
     */
    @Override
    public synchronized void run() {
        while(true) {
            try {
                System.out.println("\033[H\033[2J");
                System.out.println("Tour " + grid.getTurnNumber());
                System.out.println(active_player.getName() + "\n");
                System.out.println(active_player.getView().toStringForThread() + "\n");//vues joueur
                System.out.println("# : mur");
                System.out.println("; : mine");
                System.out.println("3 : bombe (délai avant détonation)");
                System.out.println(". : bonus");
                System.out.println("@ : joueur (€ si bouclier actif)");
                System.out.println("\n" + active_player.printStats());
                System.out.println(active_player.printControls());
                Thread.sleep(800);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
