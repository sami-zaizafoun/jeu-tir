package graphics;

import modele.*;
import java.util.logging.*;

/**
 * Thread used for bullet animation after a shot.
 */
public class BulletThread extends Thread {
    private int x;
    private int y;
    private int range;
    private final int speed = 10;
    private final int distance = 0;
    private Direction d;
    private Game game = null;
    private Player owner;
    private final boolean running = false;
    private SoundLoader sound;
    boolean over = false;
    private View observer;
    /**
     * Constructor of a new BulletThread.
     * @param x Shootman x position
     * @param y Shootman y position
     * @param range weapon range
     * @param d Shooting direction
     * @param p Player that is shooting
     */
    public BulletThread(int x, int y, int range, Direction d, Player p){
        this.x = x *64;
        this.y = y *64;
        this.range = (range-1) * 64;
        this.d = d;
        this.owner = p;
    }

    /**
     * Second constructor.
     */
    public BulletThread(){
        this(0,0,0,null,null);
    }

    /**
     * Define tasks in order to perform the animation.
     */
    @Override
    public void run(){  
        int counter = 0;        
        while(counter <= range){       
            this.x += d.x() * speed;            
            this.y += d.y() * speed;            
            int caseX = x/64;            
            int caseY = y/64; 
            observer.repaint();            
            try {            
                sleep(13);                
            } catch (InterruptedException ex) {                    
                Logger.getLogger(BulletThread.class.getName()).log(Level.SEVERE, null, ex);                
            }
            counter += speed;           
        }    
        over = true;         
        observer.repaint();
    }

    /**
     * Setup the thread before start it.
     * @param x Player's x coordinate.
     * @param y Player's y coordinate.
     * @param range Weapon's range.
     * @param d Shot's direction.
     */
    public void ResetThread(int x, int y, int range, Direction d){
        this.x = (x+d.x()) * 64;
        this.y = (y+d.y()) * 64;
        this.range = range* 64;
        this.d = d;
    }

    /**
     * Get x coordinate, corresponding to the bullet's coordinate.
     * @return x bullet's coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Get y coordinate, corresponding to the bullet's coordinate.
     * @return y bullet's coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Set the player that is shooting.
     * @param p Player that is shooting.
     */
    public void setPlayer(Player p){
        this.owner = p;
    }

    /**
    * Set the model of the Thread
    * @param g The model to follow.
    */
    public void setGame(Game g){
        this.game =g;
    }

    /**
    * Set the view that will be actualize.
    * @param view View to actualize.
    */
    public void setObserver(View view){
        this.observer = view;
    }
}
