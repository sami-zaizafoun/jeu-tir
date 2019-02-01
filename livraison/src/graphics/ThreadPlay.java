package graphics;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import modele.*;

/**
 * Thread waiting for user's input to play in the console, even if the GUI is running.
 */
public class ThreadPlay extends Thread{

    private final Game game;
    public static int counterInstance = 0;

    /**
     * Thread constructor
     * @param game Model to interact with.
     */
    public ThreadPlay(Game game){
        this.game = game;
    }

    /**
     * Run method waiting.
     */
    @Override
    public void run(){
        Player p = game.getGrid().getPlayerToPlay();
        String action = "";
        BufferedReader r = null;

        while(!action.contains("p")){
            displayInstructions(p);
            r = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("On demande une entrée");
            try {
                action = r.readLine();
            } catch (IOException ex) {

            }

            System.out.println("Et on traite l'info: "+action);
            try {
                treatInfo(action, p, r);
            } catch (IOException ex) {
                Logger.getLogger(ThreadPlay.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Fin de tour");
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
        }
        game.getGrid().nextPlayer();
        try {
            r.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadPlay.class.getName()).log(Level.SEVERE, null, ex);
        }
        game.stateChange();
    }

    public void displayInstructions(Player p){
        System.out.println("Directions possibles: ");
        System.out.println(p.possibleMoves());

        System.out.println("Vous pouvez consulter les commandes en tapant: man");
        System.out.println("Saisissez votre actions : ");

    }

    public void treatInfo(String input, Player p, BufferedReader r) throws IOException{
        switch (input) {
            case "man":
                displayMan(r, p);
                break;              
            case "E"://quit            
            case "e":            
                break;              
            case "P"://end turn even if AP>0           
            case "p":           
                break;              
            case "N"://AI action           
            case "n":           
                p.act();               
                break;              
            case "A"://Shield           
            case "a":           
                p.enableShield();               
                break;              
            case "M"://explosive plant          
            case "m":           
            case "B":          
            case "b":           
                ArrayList<FreeTile> sites = game.getGrid().getNeighbouringFreeTiles(p,1);               
                String site_list = "";               
                for (FreeTile f : sites) {               
                    site_list+=f.printCoords()+" ";                
                }                              
                System.out.println(site_list+"\nChoisissez un emplacement :");               
                if (input.equals("M") || input.equals("m")) {               
                    p.plant(new Mine(p), sites.get(Integer.parseInt(r.readLine())));
                }
                if (input.equals("B") || input.equals("b")) {
                  p.plant(new Bomb(p), sites.get(Integer.parseInt(r.readLine())));
                }
                break;             
            case "T"://firing          
            case "t":            
                System.out.println("\nChoisissez une direction (z,q,s,d):");                
                switch (r.readLine()) {                  
                    case "Z" :
                    case "z" :
                        p.fire(Direction.z);
                        break;                  
                    case "Q" :
                    case "q" :
                      p.fire(Direction.q);
                      break;
                    case "S" :
                    case "s" :
                        p.fire(Direction.s);
                        break;
                    case "D" :
                    case "d" :
                        p.fire(Direction.d);
                        break; 
                    default :
                        System.out.println("Non");
                        break;                  
                }                  
                break;              
            case "Z"://movements            
            case "z":            
                p.move(Direction.z);                
                break;              
            case "Q":           
            case "q":           
                p.move(Direction.q);
                break;              
            case "D":              
            case "d":              
                p.move(Direction.d);
                break;              
            case "S":             
            case "s":               
                p.move(Direction.s);
                break;              
            default:                
                System.out.println("Entrez une commande valide.");
                break;              
        }
        game.stateChange();
    }

    public void displayMan(BufferedReader r, Player p) throws IOException{
        String action = "";              
        System.out.println("# : mur");        
        System.out.println("; : mine");
        System.out.println("3 : bombe");        
        System.out.println(". : bonus");
        System.out.println("@ : joueur (€ si bouclier actif)");        
        System.out.println("\n" + p.printStats());
        System.out.println(p.printControls());
        System.out.println("Appuyez sur q puis entrer pour quitter.");
        action = r.readLine();        
        while(!action.equals("q")){
            System.out.println("Appuyez sur q puis entrer pour quitter.");            
            action = r.readLine();            
        }        
        System.out.println(game.getGrid().getPlayerToPlay().getView());
    }
}

