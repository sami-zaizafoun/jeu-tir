package graphics;


import modele.*;
import java.util.logging.*;
import java.io.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        com.sun.javafx.application.PlatformImpl.startup(()->{});
        new GameConfig();

        File file2 = new File("Images/Spritesheet/spritesheet_characters.xml");
        ImagesLoader il = new ImagesLoader(file2);
        il.loadPlayerImages();

	PlayerFactory factory = PlayerFactory.getInstance();
        ArrayList<BufferedImage> images = ImagesLoader.loadImages();

        try {
            il.loadPlayerImages();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = new File("Levels/level.xml");

        try {
            Game game = new Game();
            game.loadGrid(file, 4);

            Player p = factory.buildBasic(game.getGrid());
            p.setX(0);
            p.setY(0);
            p.setName("Quentin");
            game.addPlayer(p);

            Player p2 = factory.buildBasic(game.getGrid());
            p2.setX(12);
            p2.setY(0);
            p2.setName("Sami");
            game.addPlayer(p2);

            Player p3 = factory.buildBasic(game.getGrid());
            p3.setX(0);
            p3.setY(12);
            p3.setName("Aymeric");
            game.addPlayer(p3);

            Player p4 = factory.buildBasic(game.getGrid());
            p4.setX(11);
            p4.setY(12);
            p4.setName("Martin");
            game.addPlayer(p4);

            p.setImgRepr(ImagesLoader.imagePlayers.get(3).get(0));
            p2.setImgRepr(ImagesLoader.imagePlayers.get(5).get(0));
            p3.setImgRepr(ImagesLoader.imagePlayers.get(1).get(0));
            p4.setImgRepr(ImagesLoader.imagePlayers.get(4).get(0));

            p2.lastMove = Direction.s;

            game.getGrid().nextPlayer();
            GUI gui1 = new GUI(game, p);
            GUI gui2 = new GUI(game, p2);
            GUI gui3 = new GUI(game, p3);
            GUI gui4 = new GUI(game, p4);
            ViewConsole console = new ViewConsole(null,game);
            console.update(console);

            SoundLoader bg = new SoundLoader(0);
            bg.loopTrack();

        } catch (IOException | ParserConfigurationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
