package modele;

import java.util.*;
import static java.lang.Math.*;

/**
 * The game actors.
 */
public class Player extends Tile {

    private String name;
    private final String classname;
    private int energy;
    private int life;
    private int copy_life;
    private final HashMap<Weapon,Integer> loadout;
    private boolean shield_up = false;
    private PlayerStrategy strategy;
    private final PlayerGrid view;
    public Direction lastMove = Direction.z;
    private boolean selected;
    private final int visionSize;
    private boolean isShooting = false;
    private boolean asTurn = false;
    private boolean isPlanting = false;
    private int baseEnergy;
    private boolean plantingBomb = false;

    /** Class constructor.
     * @param g
     * Grid in which the player is.
     * @param name
     * Player's name.
     * @param classname
     * Player's class.
    */
    public Player(RealGrid g, String name, String classname) {
        super(0,0);
        this.visionSize = GameConfig.PLAYER_FOV;
        this.life = GameConfig.PLAYER_BASE_HP;
        this.copy_life = GameConfig.PLAYER_BASE_HP;
        this.energy = GameConfig.PLAYER_BASE_AP;
        this.baseEnergy = GameConfig.PLAYER_BASE_AP;
        this.name = name;
        this.classname = classname;
        this.loadout = new HashMap<>();
        this.strategy = new RandomStrategy(this);
        this.view = new PlayerGrid(g,this);
        this.selected = false;
        this.walkable=false;
    }

    /** Do the action determined by the player strategy.*/
    public void act() {
        this.strategy.execute();
    }

    public void setStrategy(PlayerStrategy s) {
        this.strategy = s;
    }

    public void addWeapon(Weapon w, int ammo) {
        this.loadout.put(w,ammo);
    }

    public PlayerGrid getView() {
        return this.view;
    }

    public void setAsTurn(boolean asTurn) {
        this.asTurn = asTurn;
    }

    public int getBaseEnergy() {
        return baseEnergy;
    }

    public void setBaseEnergy(int baseEnergy) {
        this.baseEnergy = baseEnergy;
    }

    public boolean getAsTurn() {
        return asTurn;
    }

    public boolean isSelected(){
        return this.selected;
    }

    public void select(){
        this.selected = true;
    }

    public void unselect(){
        this.selected = false;
    }

    public int getLife() {
        return this.life;
    }

    public int getCopyLife() {
        return this.copy_life;
    }

    public void setCopyLife(int new_life) {
        this.copy_life=new_life;
    }

    public void enablePlant(){
        isPlanting = true;
    }

    public void disablePlant(){
        isPlanting = false;
    }

    public void setLife(int new_life) {
        this.life=new_life;
    }

    public boolean isPlanting(){
        return isPlanting;
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int new_energy) {
        this.energy=new_energy;
    }

    public boolean isShield_up() {
        return shield_up;
    }

    public void enableShield() {
        this.shield_up=true;
        this.energy-=GameConfig.SHIELD_COST;
    }

    public void disableShield() {
        this.shield_up = false;
    }

    /** Moves the player one tile towards the parameter direction
      * and reacts to the content of the destination tile.
      * @param d
      * Move direction.
      */
    public void move(Direction d) {
        if (this.possibleMoves().contains(d)) {
            this.view.setTileAt(new FreeTile(x,y));
            this.x += d.x();
            this.y += d.y();

            try {
                ((Bonus)this.view.getTileAt(x,y)).boost(this);
            } catch (ClassCastException not_a_bonus) {}
            try {
                ((Weapon)this.view.getModel().getTileAt(x,y)).explode(this.view.getModel(), this);
            } catch(ClassCastException not_a_weapon) { }


            this.view.setTileAt(this);
            this.lastMove = d;
            this.energy-=GameConfig.MOVE_COST;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Lists the available directions of a potential move.
      * @return the list of possible directions.
      */
    public ArrayList<Direction> possibleMoves() {
        ArrayList<Direction> res = new ArrayList<>();
        if((this.y > 0) && view.getTileAt(this.x,this.y-1).isWalkable()) {
            res.add(Direction.z);
        }

        if((this.y < (view.getModel().getGrid().length/view.getModel().getWidth())-1) && view.getTileAt(this.x,this.y+1).isWalkable()){
            res.add(Direction.s);
        }

        if((this.x > 0) && view.getTileAt(this.x-1,this.y).isWalkable()){
            res.add(Direction.q);
        }

        if((this.x < view.getModel().getWidth() -1) && view.getTileAt(this.x+1,this.y).isWalkable()){
            res.add(Direction.d);
        }
        return res;
    }

    public ArrayList<ArrayList<Integer>> visibleTiles(){
        ArrayList<ArrayList<Integer>> t = new ArrayList<>();

        int c=1;
        while(c<this.visionSize){
            if(this.x+c>view.getModel().getWidth()){
                break;
            }else{
                if(!(view.getModel().getTileAt(this.x+c, this.y) instanceof FreeTile)){
                    break;
                }
                else{
                    ArrayList<Integer> pos = new ArrayList<>(2);
                    pos.add(0,this.x+c);
                    pos.add(1,this.y);
                    t.add(pos);
                }
            }
            c++;
        }

        c=1;
        while(c<this.visionSize){
            if(this.x-c<0){
                break;
            }else{
                if(!(view.getModel().getTileAt(this.x-c, this.y) instanceof FreeTile)){
                    break;
                }
                else{
                    ArrayList<Integer> pos = new ArrayList<>(2);
                    pos.add(0,this.x-c);
                    pos.add(1,this.y);
                    t.add(pos);
                }
            }
            c++;
        }

        c=1;
        while(c<this.visionSize){
            if(this.y+c>view.getModel().getGrid().length/view.getModel().getWidth()){
                break;
            }else{
                if(!(view.getModel().getTileAt(this.x, this.y+c) instanceof FreeTile)){
                    break;
                }
                else{
                    ArrayList<Integer> pos = new ArrayList<>(2);
                    pos.add(0,this.x);
                    pos.add(1,this.y+c);
                    t.add(pos);
                }
            }
            c++;
        }

        c=1;
        while(c<this.visionSize){
            if(this.y-c<0){
                break;
            }else{
                if(!(view.getModel().getTileAt(this.x, this.y-c) instanceof FreeTile)){
                    break;
                }
                else{
                    ArrayList<Integer> pos = new ArrayList<>(2);
                    pos.add(0,this.x);
                    pos.add(1,this.y-c);
                    t.add(pos);
                }
            }
            c++;
        }

        ArrayList<Integer> pos = new ArrayList<>(2);
        pos.add(0,this.x);
        pos.add(1,this.y);
        t.add(pos);
        return t;
    }

    public ArrayList<ArrayList<Integer>> visiblesTiles(){
        ArrayList<ArrayList<Integer>> visibles = new ArrayList<>();
        int jX = this.x ;
        int jY = this.y ;
        int mapSizeX = view.getModel().getWidth();
        int mapSizeY = view.getModel().getHeight();

        for(int vX = 0; vX<mapSizeX; vX++){
            for(int vY = 0; vY<mapSizeY; vY++){
                if(!(jX==vX && jY==vX)){
                    testView(jX,jY,vX,vY,visibles);
                }
            }
        }

        for(int vY = 0; vY<mapSizeY; vY++){
            for(int vX = 0; vX<mapSizeX; vX++){

                Tile t = view.getModel().getTileAt(vX, vY);
                boolean isWall = t instanceof Wall;

                ArrayList<Integer> a = new ArrayList<>(); a.add(0,vX); a.add(1,vY);
                boolean isContained = visibles.contains(a);

                boolean narrowContained;
                if( isWall && !(isContained) ){
                    narrowContained = false;
                    ArrayList<Integer> narrow = new ArrayList<>(); narrow.add(0,0); narrow.add(1,0);
                    for(int narrowvX=-1; narrowvX<=1; narrowvX+=1){
                        for(int narrowvY=-(1); narrowvY<=1; narrowvY+=1){
                            try{
                                narrow.set(0,vX+narrowvX); narrow.set(1,vY+narrowvY);
                                narrowContained = narrowContained || (visibles.contains(narrow) && !(view.getModel().getTileAt(vX+narrowvX, vY+narrowvY) instanceof Wall));
                            }catch(Exception e){ }
                        }
                    }
                    if(narrowContained){ visibles.add(a); }
                }

            }
        }
        return visibles;
    }

    public void testView(int jX, int jY, int vX, int vY, ArrayList<ArrayList<Integer>> visibles){
        int mapSizeX = view.getModel().getWidth(); //creation of g[][], liste of all the values ran
        int mapSizeY = view.getModel().getHeight();
        int[][] g = new int[mapSizeX][mapSizeY];

        for(int[] i : g){ //run g
            for(int j : i){
                j = 0; //resets g to 0
            }
        }

        int distanceX = abs(vX-jX); //calculates the absolute distance between the player and point (X)
        int distanceY = abs(vY-jY); //calculates the absolute distance between the player and point (Y)
        double hypothenuse = sqrt(pow(distanceX,2)+pow(distanceY,2)); //calculates the lenght of the hypotenuse
        int intHypothenuse = ((int)(hypothenuse))*20; //rounds the length of the hypotenuse to an int

        double movingX = distanceX*20; //calculates the distance per mouvement of test in X
        double movingY = distanceY*20; //calculates the distance per mouvement of test in Y
        if(intHypothenuse!=0){
            movingX/=intHypothenuse; //divides movingX by the length of the hypotenuse if the distance is greater than 0
            movingY/=intHypothenuse; //divides movingY by the length of the hypotenuse if the distance is greater than 0
        }

        float XX = jX*20; //creates XX value in jX * 20
        float YY = jY*20; //creates YY value in jY * 20
        boolean revX;
        boolean revY;
        revX = vX-jX<0;
        revY = vY-jY<0;

        for(int i=0; i<intHypothenuse; i++){

            if(revX){
                XX-=movingX;
            }else{
                XX+=movingX;
            } //moves XX of movingX (towards vX*20 therefore)
            if(revY){
                YY-=movingY;
            }else{
                YY+=movingY;
            }

            int xtest = (int) floor( ((int)(XX)+10) /20 );
            int ytest = (int) floor( ((int)(YY)+10) /20 );

            boolean question =  view.getModel().getTileAt(xtest,ytest).isWalkable() || view.getModel().getTileAt(xtest,ytest) instanceof Player;
            if(question){
                g[xtest][ytest] += 1;
            }
            else{
                return;
            }
        }

        ArrayList<Integer> tmp = new ArrayList<>(2);
        tmp.add(0,vX);
        tmp.add(1,vY);
        visibles.add(tmp);
    }

    /** Applies damage to the player
      * and handles its death.
      * @param damage
      * Damage received.
      */
    public void takeDamage(int damage) {
        if (!(this.shield_up)) {
            this.life -= damage;
            if (this.life<=0) {
                this.view.setTileAt(new FreeTile(this.x,this.y));
                this.view.getModel().getActivePlayers().remove(this);
                this.view.getModel().nextPlayer();
            }
        } else {
            this.disableShield();
        }

    }

    /** Checks if an action is available, given its cost
      * and the weapon's stock, if applicable.
      * @param cost
      * Action cost.
      * @param weapon
      * Index of the weapon in the factory's static inventory,
      * or -1 if no weapon is involved in the action.
      * @return the test result.
    */
    public boolean actionIsAvailable(int cost, int weapon) {
        boolean enough_ap = (this.energy>=cost);
        return (weapon>=0 && weapon<PlayerFactory.inventory.size()) ? (enough_ap && this.loadout.get(PlayerFactory.inventory.get(weapon))>0) : enough_ap;
    }

    /** Plants an explosive on the parameter tile.
      * @param explosive
      * The kind of explosive to plant.
      * @param t
      * The planting site.
    */
    public void plant(Mine explosive, Tile t) {
        if (t!=null) {
            explosive.setPosition(t.getX(),t.getY());
            this.view.setTileAt(explosive);
            try {
                this.view.addBomb((Bomb)explosive);
                this.loadout.put(PlayerFactory.inventory.get(2), this.loadout.get(PlayerFactory.inventory.get(2))-1);
            } catch(ClassCastException not_a_bomb) {
                this.loadout.put(PlayerFactory.inventory.get(1), this.loadout.get(PlayerFactory.inventory.get(1))-1);
            }
            this.energy-=GameConfig.PLANT_COST;
        }
    }

    /** Shoot with a firearm towards the parameter direction.
      * @param d
      * Fire direction.
      */
    public void fire(Direction d) {
        this.isShooting = true;
        this.lastMove = d;
        for (Weapon w : this.loadout.keySet()) {
            if (w instanceof Rifle) {
                Rifle fire_rifle = new Rifle(this);//new Rifle just for firing
                fire_rifle.fire(this.view.getModel(),d);
                this.loadout.put(PlayerFactory.inventory.get(0), this.loadout.get(PlayerFactory.inventory.get(0))-1);// ammunation
                this.energy-=GameConfig.FIRE_COST;
            }
        }
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void notShooting(){
        this.isShooting = false;
    }

    @Override
    public int hashCode() {
        int code=13;
        code+=33*code+this.name.length();
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (o==this) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        Player p = (Player)o;
        return this.name.equals(p.name);
    }

    public String getName() {
        return this.name;
    }

    public String printLoadout() {
        return "Rifle : " + this.loadout.get(PlayerFactory.inventory.get(0)) + " balles      Mines : " + this.loadout.get(PlayerFactory.inventory.get(1)) + "    Bombes : " + this.loadout.get(PlayerFactory.inventory.get(1));
    }

    public String printStats() {
        return "Classe : " + this.classname + "\nPosition : " + this.x + " " + this.y + "\nEnergie : " + this.energy + "\nPoints de vie : " + this.life + "\nEquipement : "+ this.printLoadout();
    }

    public String printControls() {
        String controls = "\nn : tour auto     p : fin de tour     e : quitter";
        if (actionIsAvailable(GameConfig.MOVE_COST,-1)) {
            controls+="\nz,q,s,d : déplacer joueur\n";
        }
        if (actionIsAvailable(GameConfig.PLANT_COST,1)) {
            controls+="m : poser mine      ";
        }
        if (actionIsAvailable(GameConfig.PLANT_COST,2)) {
            controls+="b : poser bombe     ";
        }
        if (actionIsAvailable(GameConfig.FIRE_COST,0)) {
            controls+="t : tirer     ";
        }
        if (actionIsAvailable(GameConfig.SHIELD_COST,-1) && !this.shield_up) {
            controls+="a : activer bouclier";
        }
        return controls;
    }

    public boolean isPlantingBomb() {
        return plantingBomb;
    }

    public void enablePlantingBomb(){
        this.plantingBomb = true;
    }

    public void disablePlantingBomb(){
        this.plantingBomb = false;
    }

    @Override
    public String toString() {
        return this.shield_up ? "€" : "@";
    }
}
