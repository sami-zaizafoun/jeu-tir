package graphics;

import modele.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JPanel;

/**
 * Class representing player graphic view.
 * @author quentindeme
 */
public class View extends JPanel implements ModelListener{
    private static final long serialVersionUID = 7526471155622776147L;
    private Tile[] entities;
    private final Game game;
    private final Dimension sizeImg;
    private BulletThread anim;
    private boolean startAnim = true;
    private final Player player;
    private final GUI observer;

    /**
     * Constructor of a new View
     * @param entities Tile to render at the screen.
     * @param game Model to listen.
     * @param p Player to whom belongs the view.
     * @param observer The GUI containing the view. 
     */
    public View(Tile[] entities, Game game, Player p, GUI observer) {
        this.player = p;
        this.entities = entities;
        this.game = game;
        this.sizeImg = new Dimension(35,43);
        this.observer = observer;
        game.addListener(this);
    }

    /**
     * Display entities at the screen.
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int i = 0; i < game.getTileMap().size(); i++){
            ArrayList<Tile> list = game.getTileMap().get(i);
            try{
                for(Tile t : list){
                    int x = 64 * t.getX();
                    int y = 64 * t.getY();
                    if(player.getView().playerCanSee(t)){
                        if(t instanceof Bonus){
                            if(player.getX() == t.getX() && player.getY() == t.getY()){
                                ((Bonus) t).boost(player);
                                try{
                                    game.getTileMap().get(i).remove(t);
                                }catch(java.util.ConcurrentModificationException e){

                                }
                            }
                            g.drawImage(t.getImgRepr(), x+15 , y+15, this);
                        }else{
                            g.drawImage(t.getImgRepr(), x , y, this);
                        }

                    }
                }
            }catch(java.util.ConcurrentModificationException e){

            }
        }

        if(player.getLife() > 0){
                BufferedImage img = player.getImgRepr();

                ArrayList<ArrayList<Integer>> viewableTiles = player.visibleTiles();
                ArrayList<ArrayList<Integer>> visiblesTiles = player.visiblesTiles();

		for(int i = 0; i < game.getTileMap().size(); i++){
                    ArrayList<Tile> list = game.getTileMap().get(i);
                    for(Tile t : list){
                        boolean playerCanSee = player.getView().playerCanSee(t);
                        int xx = t.getX();
                        int yy = t.getY();
                        ArrayList<Integer> tmp = new ArrayList<Integer>(); tmp.add(0,t.getX()); tmp.add(1,t.getY());
                        boolean isInViewable = (viewableTiles.contains(tmp));
                        boolean isInVisibles = (visiblesTiles.contains(tmp));
                        if(playerCanSee && ( isInViewable || isInVisibles ) ){
                            g.drawImage(t.getImgRepr(), xx*64 , yy*64, this);
                        }else{
                            g.drawImage(ImagesLoader.fog, xx*64, yy*64, this);
                        }
                    }
                }

                Set<Player> players = game.getListPlayers().keySet();
                for(Player p: players){
                    if(p.getName() != player.getName()){
                        if( (p.getLife()>0) ){
                            ArrayList<Integer> tmp = new ArrayList<Integer>(); tmp.add(0,p.getX()); tmp.add(1,p.getY());
                            boolean playerCanSee = player.getView().playerCanSee(p);
                            boolean isInViewable = (viewableTiles.contains(tmp));
                            boolean isInVisibles = (visiblesTiles.contains(tmp));
                            if(playerCanSee && ( isInViewable || isInVisibles ) ){
                                displayPlayer(g,p);
                            }
                        }
                    }
                }

                int baseX = (int)(( 64 - sizeImg.getWidth())/2);
                int baseY = (int)(( 64 - sizeImg.getHeight())/2);

                int x = 64 * player.getX() + baseX;
                int y = 64 * player.getY() + baseY;
                displayBullet(g, player);
                displayPlayer(g, player);

                //Draw shield
                if(player.isShield_up()){
                    g.drawImage(ImagesLoader.shield, player.getX()*64, player.getY()*64, this);
                }

                drawActionPoint(player,g);
                if(player.isPlanting()){
                    displayPlantPoints(g,player);
                }

                ArrayList<Direction> possibleMoves = player.possibleMoves();

                if(player.getEnergy() == 0 || !player.getAsTurn()){
                    g.setColor(new Color(255,0,0));
                    g.drawRect(64 * (player.getX()), 64 * (player.getY()), 64, 64);
                }

                if(player.isSelected()){
                    if(player.getEnergy() != 0){
                        g.setColor(new Color(0,255,0));
                        for(Direction d : possibleMoves){
                            g.fillOval(64 * (player.getX()+d.x()) + 17, 64* (player.getY()+d.y()) +17 , 30, 30);
                        }
                    }
                }
            }

        displayBomb(g);
        Grid grid = game.getGrid();


    if(observer.playerToPlay.getName() != null || player.getName() != null){
        observer.setTitle("Shooter Game ("+player.getName()+"). Tour de: "+observer.playerToPlay.getName());
    }

    if(game.getGrid().gameIsOver() && player.getLife() > 0){
        g.setColor(Color.GREEN);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("VICTOIRE", 300, 416);
    }else if(game.getGrid().gameIsOver() && player.getLife() <= 0){
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        g.drawString("DEFAITE", 300, 416);
    }
}

    public void displayPlantPoints(Graphics g, Player p){
        for(int i = p.getX()-1;  i <= p.getX()+1; i++){
            for(int j = p.getY()-1 ; j <= p.getY()+1; j++){
                if(!((i == p.getX()) && j==p.getY()) && game.getGrid().getTileAt(i, j) instanceof FreeTile){
                    g.setColor(Color.RED);
                    g.fillOval(i*64 +17, j*64+17, 30, 30);
                }
            }
        }
    }

    /**
     * Display bomb when it belong to the player.
     * @param g graphics.
     */
    public void displayBomb(Graphics g){
        for(Tile t : player.getView().getModel().getGrid()){
            if(t instanceof Bomb){
                if(((Bomb) t).getOwner() == player){
                    g.setColor(Color.GREEN);
                    g.drawString(Integer.toString(((Bomb) t).getDelay()), t.getX()*64 +28, t.getY()*64 + 20);
                    g.drawImage(ImagesLoader.bomb, t.getX()*64 +12 , t.getY()*64 +12, this);
                }
            }else if(t instanceof Mine){
                if(((Mine) t).getOwner() == player){
                    g.drawImage(ImagesLoader.mine, t.getX()*64 +12 , t.getY()*64 +12, this);
                }
            }
        }
    }

    /**
     * Display bullet when the player is shooting.
     * @param g graphics
     * @param p Player that is shooting.
     */
    public void displayBullet(Graphics g, Player p){
        g.setColor(Color.BLACK);
        if(p.isShooting()){
            if(startAnim){
                anim = new BulletThread();
                anim.ResetThread(p.getX(),p.getY(),GameConfig.RIFLE_RANGE-1, p.lastMove);
                anim.setPlayer(p);
                anim.setGame(game);
                anim.setObserver(this);
                anim.start();
                startAnim = false;
            }
            else if(!anim.over){
                int x = anim.getX();
                int y = anim.getY();

                if(p.lastMove == Direction.z){
                g.drawImage(ImagesLoader.lookUp(ImagesLoader.bullet), x + 40, y, this);
                }else if(p.lastMove == Direction.s){
                    g.drawImage(ImagesLoader.lookDown(ImagesLoader.bullet), x + 40, y, this);
                }else if(p.lastMove == Direction.q){
                    g.drawImage(ImagesLoader.lookLeft(ImagesLoader.bullet), x, y + 40, this);
                }else if(p.lastMove == Direction.d){
                    g.drawImage(ImagesLoader.lookRight(ImagesLoader.bullet), x, y + 40, this);
                }
            }
            else if(anim.over){
                p.notShooting();
                anim.interrupt();
                anim = null;
                startAnim = true;
            }
        }
    }

    /**
     * Display player at the screen.
     * @param g
     * @param p Player to display
     */
    public void displayPlayer(Graphics g, Player p){

        BufferedImage img = p.getImgRepr();

        int baseX = (int)(( 64 - sizeImg.getWidth())/2);
        int baseY = (int)(( 64 - sizeImg.getHeight())/2);

        int x = 64 * p.getX() + baseX;
        int y = 64 * p.getY() + baseY;


        // The required drawing location
        int drawLocationX = 300;
        int drawLocationY = 300;

        // Rotation information
        if(p.lastMove == Direction.z){
            g.drawImage(ImagesLoader.lookUp(img), x, y, this);
        }else if(p.lastMove == Direction.s){
            g.drawImage(ImagesLoader.lookDown(img), x, y, this);
        }else if(p.lastMove == Direction.q){
            g.drawImage(ImagesLoader.lookLeft(img), x, y, this);
        }else if(p.lastMove == Direction.d){
            g.drawImage(ImagesLoader.lookRight(img), x, y, this);
        }
        if(!p.getAsTurn()){
            g.setColor(new Color(255,0,0));
            g.drawRect(p.getX()*64, p.getY()*64, 64, 64);
        }
        drawLife(p,g);
    }

    /**
     * Draw player action's points.
     * @param p player
     * @param g 
     */
    public void drawActionPoint(Player p, Graphics g){
        int nbAction = p.getEnergy();

        int x = 64 * (p.getX());
        int y = 64 * (p.getY()+1);

        g.setColor(new Color(0,255,255));
        g.setFont(new Font("default", Font.BOLD, 14));
        g.drawString(Integer.toString(nbAction), x+10, y-10);
    }

    /**
     * Draw player's life.
     * @param p Player
     * @param g 
     */
    public void drawLife(Player p, Graphics g){
        g.setColor(Color.BLACK);
        int life = p.getLife();

        int size = 64 - 20;
        int x = 64 * p.getX();
        int y = 64 * p.getY();

        double percentLife = life / (double)10;
        int hpSize = (int) (size * percentLife);
        g.drawRect(x+10,y+2,size, 5);

        if(life >= 7){
            g.setColor(new Color(0,255,0));
        }else if(life >= 4){
            g.setColor(new Color(229,133,61));
        }else{
            g.setColor(new Color(255,0,0));
        }
        g.fillRect(x+10+1,y+2+1,hpSize-2, 5-2);
    }

    /**
     * Set entities of the view.
     * @param l 
     */
    public void setEntities(Tile[] l){
        this.entities = l;
    }

    /**
     * Signal to view the model has changed.
     * @param source 
     */
    @Override
    public void update(Object source) {
        this.repaint();
    }
}
