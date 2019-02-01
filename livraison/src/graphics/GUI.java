package graphics;

import modele.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Class used for create a new player's window.
 * @author quentindeme
 */
public class GUI extends JFrame{
    private static final long serialVersionUID = 7526471155622776147L;
    private View view;
    private Game game;
    private Integer[] coordPlayer = new Integer[2];
    public  Player playerToPlay = null;
    private boolean isShooting = false;
    private boolean isMoving = false;
    private boolean isPlanting = false;
    private Player player;
    private SoundLoader sound;

    public GUI(Player p){
        this(new Game(),p);
    }

    /**
     * JFrame containing player's view.
     * @param game Model the view will be listening.
     * @param p player to whom the view belongs.
     */
    public GUI(Game game, Player p){
        this.player = p;
        this.game = game;
        playerToPlay = game.getGrid().getPlayerToPlay();
        //Create a new View (that is a JPanel)
        this.view = new View(null,game, player, this);
        view.setEntities(game.getGrid().getGrid());
        //Set the view as contentPane of the Frame
        setContentPane(view);
        setTitle("Shooter Game ("+player.getName()+")");
        //In this code the GUI does not adapt to level size.
        //But it could performing like this:
//        int size = game.getGrid().getWidth() * 64; (64x64 is size of the images)    
        setSize(832,832); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Place window center of the screen
        setResizable(false);
        setLocationRelativeTo(null);

        //In order to read keys input
        setFocusable(true);
        requestFocus();

        //The title of the window in the name of the player and indicates who is playing
        setTitle("Shooter Game ("+player.getName()+"). Tour de: "+playerToPlay.getName());

        //Création du menu clique
        final JPopupMenu popup = new JPopupMenu();

        //Move action
        JMenuItem depItem = new JMenuItem("Déplacement"  + GameConfig.MOVE_COST + " AP", new ImageIcon("Images/moveIcon.png"));
        depItem.getAccessibleContext().setAccessibleDescription("Déplacer le personnage");
        depItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.getEnergy() >= GameConfig.MOVE_COST){
                        player.select();
                        isMoving = true;
                        game.stateChange();
                    }
                }
        });
        popup.add(depItem);

        //Shield action
        JMenuItem shieldItem = new JMenuItem("Activer bouclier"  + GameConfig.SHIELD_COST + " AP", new ImageIcon("Images/shieldIcon.png"));
        shieldItem.getAccessibleContext().setAccessibleDescription("Activer le bouclier");
        shieldItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Player p = player;
                    if(!p.isShield_up() && p.getEnergy() >= GameConfig.SHIELD_COST){
                        p.enableShield();
                        if(p.getEnergy() == 0){
                            changeTurn();
                        }else{
                            game.stateChange();
                        }
                    }
            }
        });
        popup.add(shieldItem);

        //Shoot action
        JMenuItem shootItem = new JMenuItem("Tirer"  + GameConfig.FIRE_COST + " AP", new ImageIcon("Images/target.png"));
        shootItem.getAccessibleContext().setAccessibleDescription("Tirer dans une direction");
        shootItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.getEnergy() >= GameConfig.FIRE_COST){
                    isShooting = true;
                }
            }
        });
        popup.add(shootItem);

        //Plant mine action
        JMenuItem plantMine = new JMenuItem("Poser une mine"  + GameConfig.PLANT_COST + " AP", new ImageIcon("Images/mine.png"));
        plantMine.getAccessibleContext().setAccessibleDescription("Poser une mine");
        plantMine.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.getEnergy() >= GameConfig.PLANT_COST){
                    player.enablePlant();
                    game.stateChange();
                }
            }
        });
        popup.add(plantMine);

        //Plant bomb action
        JMenuItem plantBomb = new JMenuItem("Poser une bombe"  + GameConfig.PLANT_COST + " AP", new ImageIcon("Images/bomb.png"));
        plantBomb.getAccessibleContext().setAccessibleDescription("Poser une bombe");
        plantBomb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(player.getEnergy() >= GameConfig.PLANT_COST){
                    player.enablePlant();
                    player.enablePlantingBomb();
                    game.stateChange();
                }
            }
        });
        popup.add(plantBomb);

        //Pass turn
        JMenuItem passTurn = new JMenuItem("Passer", new ImageIcon("Images/pass.png"));
        passTurn.getAccessibleContext().setAccessibleDescription("Passer son tour");
        passTurn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                changeTurn();
            }
        });
        popup.add(passTurn);

        addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            /**
             * If the player wants to move with keyboard (for multiples movements).
             * @param e Keyvent.
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if(player.getName() == playerToPlay.getName()){
                    if(player.getEnergy() >= GameConfig.MOVE_COST && player.getName().equals(playerToPlay.getName())){
                        if(e.getKeyCode() == KeyEvent.VK_Z){
                            if(player.possibleMoves().contains(Direction.z)){
                              player.move(Direction.z);
                              game.stateChange();
                            }
                        }
                        if(e.getKeyCode() == KeyEvent.VK_Q){
                            if(player.possibleMoves().contains(Direction.q)){
                              player.move(Direction.q);
                              game.stateChange();
                            }

                        }
                        if(e.getKeyCode() == KeyEvent.VK_S){
                                if(player.possibleMoves().contains(Direction.s)){
                                  player.move(Direction.s);
                                  game.stateChange();
                                }
                        }
                        if(e.getKeyCode() == KeyEvent.VK_D){
                            if(player.possibleMoves().contains(Direction.d)){
                              player.move(Direction.d);
                              game.stateChange();
                            }
                        }
                    }
                    if(player.getEnergy() == 0){
                        changeTurn();
                    }
                }
            }
        });

        getContentPane().addMouseListener(new MouseListener(){
            /**
             * Display actions menu.
             * @param e 
             */
            public void showPopup(MouseEvent e){
                popup.show(e.getComponent(), e.getX(), e.getY());
            }

            /**
             * Not used.
             * @param e 
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;
            }

            /**
             * If we press the window, the player to play is actualized.
             * @param e 
             */
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / 64;
                int y = e.getY() / 64;
                playerToPlay = game.getGrid().getPlayerToPlay();
            }

            /**
             * On mouse released all actions are treated in consequences.
             * @param e MouseEvent.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                int popX = e.getX();
                int popY = e.getY();
                int x = e.getX() / 64;
                int y = e.getY() / 64;

                //Display actions menu and distinct what is possible or not.
                if(game.getGrid().getTileAt(x, y) instanceof Player ){ //Test si il est le joueur qui doit jouer
                    Player p = (Player) game.getGrid().getTileAt(x, y);
                    if(p.getAsTurn()){
                        if(!p.isSelected() && p.getName().equals(player.getName()) && !game.getGrid().gameIsOver()){
                            showPopup(e);
                            if(p.getEnergy() >= GameConfig.MOVE_COST){
                                depItem.setText("Déplacement: "  + GameConfig.MOVE_COST + " AP");
                            }else{
                                depItem.setText("D̶é̶p̶l̶a̶c̶e̶m̶e̶n̶t̶: " + GameConfig.MOVE_COST + " AP");
                            }
                            if(p.getEnergy() >= GameConfig.SHIELD_COST && !p.isShield_up()){
                                shieldItem.setText("Activer bouclier: " + GameConfig.SHIELD_COST + " AP");
                            }else{
                                shieldItem.setText("A̶c̶t̶i̶v̶e̶r̶ ̶b̶o̶u̶c̶l̶i̶e̶r̶: " + GameConfig.SHIELD_COST + " AP");
                            }
                            if(p.getEnergy() >= GameConfig.FIRE_COST){
                                shootItem.setText("Tirer: " + GameConfig.FIRE_COST + " AP");
                            }else{
                                shootItem.setText("T̶i̶r̶e̶r̶: "  + GameConfig.FIRE_COST + " AP");
                            }
                            if(p.getEnergy() >= GameConfig.PLANT_COST){
                                plantBomb.setText("Poser une bombe: " + GameConfig.PLANT_COST + " AP");
                                plantMine.setText("Poser une mine: " + GameConfig.PLANT_COST + " AP");
                            }else{
                                plantBomb.setText("P̶o̶s̶e̶r̶ ̶u̶n̶e̶ ̶b̶o̶m̶b̶e̶: "  + GameConfig.PLANT_COST + " AP");
                                plantMine.setText("P̶o̶s̶e̶r̶ ̶u̶n̶e̶ ̶m̶i̶n̶e̶: " + GameConfig.PLANT_COST + " AP");
                            }
                        }else{
                            p.unselect();
                        }
                    }
                    game.stateChange();
                }
                
                //Just actualize player
                Player p = playerToPlay;
                //First of all, we act if the player as energy.
                if(p.getEnergy() > 0){
                    //If the past choice was a move
                    if(isMoving){
                        int depX = x - p.getX();
                        int depY = y - p.getY();

                        //We check click coordinate and get the direction of the click
                        Direction d = null;
                        if(depX == 0 && depY == -1){
                            d = Direction.z;
                        }else if(depX == 0 && depY == 1){
                            d = Direction.s;
                        }else if(depX == -1 && depY == 0){
                            d = Direction.q;
                        }else if(depX == 1 && depY == 0){
                            d = Direction.d;
                        }

                        //If the direction is in player's possible moves, that's ok
                        if(p.possibleMoves().contains(d)){
                            //If the player is moving on a mine, we play explosion sound
                            if (game.getGrid().getTileAt(p.getX()+d.x(),p.getY()+d.y()) instanceof Mine) {
                                sound = new SoundLoader(1);
                            }
                            //We compute the movement
                            p.move(d);
                            //After the movement, the player is not moving anymore for now
                            isMoving = false;
                            p.unselect();
                            if(p.getEnergy()==0){
                                changeTurn();
                            }
                            game.stateChange();
                        }
                        isMoving = false;
                        p.unselect();
                        game.stateChange();
                    }else if(isShooting){
                        //Everything follow the same mechanic than before.
                        Direction d = null;
                        int depX = x - p.getX();
                        int depY = y - p.getY();
                        d = block2dir(x,y);

                        if(game.getGrid().getTileAt(depX, depY) instanceof Player){

                        }else{
                            p.fire(d);
                            sound = new SoundLoader(3);
                        }

                        isShooting = false;
                        if(p.getEnergy()==0){
                            changeTurn();
                        }
                        game.stateChange();

                    }else if(p.isPlanting() && x == p.getX() && y == p.getY()){
                        p.disablePlant();
                    }else if(p.isPlanting() && game.getGrid().getTileAt(x,y) instanceof FreeTile){
                        int distance = (int) Math.sqrt(Math.pow(playerToPlay.getX() - x, 2) + Math.pow(playerToPlay.getY() - y, 2));
                        if(distance == 1 ){
                            if(playerToPlay.isPlantingBomb()){
                                playerToPlay.plant(new Bomb(playerToPlay), game.getGrid().getTileAt(x,y));
                            }else{
                                playerToPlay.plant(new Mine(playerToPlay), game.getGrid().getTileAt(x,y));
                            }
                        }
                        p.disablePlant();
                        p.disablePlantingBomb();
                        game.stateChange();
                    }
                    damageSound();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        setVisible(true);
        int difX = 832 + (832 - this.getContentPane().getWidth());
        int difY = 832 + (832 - this.getContentPane().getHeight());
        this.setSize(difX, difY);
    }

    /**
     * Getter for the view.
     * @return The view inside the Frame
     */
    public View getView(){
        return this.view;
    }

    /**
     * Getter for the game.
     * @return The model that is listened.
     */
    public Game getGame(){
        return this.game;
    }

    /**
     * Change the turn and reset informations for the GUI.
     */
    public void changeTurn(){
        playerToPlay.setAsTurn(false);
        playerToPlay = game.getGrid().nextPlayer();
        playerToPlay.setAsTurn(true);
        setTitle("Shooter Game ("+player.getName()+"). Tour de: "+playerToPlay.getName());

        if(game.getGrid().hearExplosion()){
            sound = new SoundLoader(1);
            damageSound();
            game.getGrid().endExplosion();
        }
        game.stateChange();
    }

    /**
     * Play damage sounds.
     */
    public void damageSound(){
        if(game.getGrid().checkDamage()){
            sound = new SoundLoader(2);
            game.getGrid().checkDamage();
        }
    }

    /**
     * Convert block into direction between x y coordinates and the player.
     * @param x x coordinate.
     * @param y y coordinate.
     * @return The direction of the player looking to x y coordinate.
     */
    public Direction block2dir(int x, int y){
        int varX = playerToPlay.getX() - x;
        int varY = playerToPlay.getY() - y;

        if(varX == 0){
            if(varY<0){
                return Direction.s;
            }else if(varY > 0){
                return Direction.z;
            }
        }else{
            if(varX<0){
                return Direction.d;
            }else{
                return Direction.q;
            }
        }
        return null;
    }
}
