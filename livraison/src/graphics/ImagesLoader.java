package graphics;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.imageio.ImageIO;
import javax.xml.parsers.*;
import modele.*;
import org.xml.sax.SAXException;


public class ImagesLoader {

    public static ArrayList<BufferedImage> imageList;
    public static HashMap<Integer, ArrayList<BufferedImage>> imagePlayers;
    private final File file;
    public static BufferedImage fog;
    public static BufferedImage shield;
    public static BufferedImage bomb;
    public static BufferedImage mine;
    public static BufferedImage bullet;
    public static BufferedImage bonus;

    /**
     * Construct ImagesLoader with a file to read.
     * @param file File to load.
     */
    public ImagesLoader(File file){
        this.file = file;
    }

    /**
     * Load images that will represent the players.
     * The base image is cut with an XML.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException 
     */
    public void loadPlayerImages() throws ParserConfigurationException, IOException, SAXException{
        imagePlayers = new HashMap<>();
        BufferedImage spritesheet = null;
        try{
            spritesheet = ImageIO.read(new File("Images/Spritesheet/spritesheet_characters.png"));
        }catch(IOException e){
            System.out.println("Loader"+e);
        }

        PlayerImageParser playerHandler = new PlayerImageParser();
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file,playerHandler);
        } catch (SAXException ex) {
            Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
        }

        for(int i = 0; i < 9; i++){
            imagePlayers.put(i, new ArrayList<>());
        }

        int acc = 0;
        int index = 0;
        for(String name: playerHandler.playerNames){
            ArrayList<Integer> list = playerHandler.playerImages.get(name);
            int x = list.get(0);
            int y = list.get(1);
            int width = list.get(2);
            int height = list.get(3);
            BufferedImage temp = spritesheet.getSubimage(x,y,width, height);
            imagePlayers.get(index).add(temp);
            acc++;
            if(acc > 5){
                acc = 0;
                index++;
            }
        }
    }

    /**
     * Return the image corresponding to the player when he is looking UP. The image is completely recalculated.
     * @param img Basic image player that will be rotated.
     * @return The image rotated in the right sens.
     */
    public static BufferedImage lookUp(BufferedImage img) {
        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(height, width, img.getType());

        for(int y = 0; y < height ; y++){
            for(int x = 0; x < width; x++){
                rotated.setRGB(y, (width-1)-x, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    /**
     * Return the image corresponding to the player when he is looking DOWN. The image is completely recalculated.
     * @param img Basic image player that will be rotated.
     * @return The image rotated in the right sens.
     */
    public static BufferedImage lookDown(BufferedImage img) {

        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(height, width, img.getType());

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height ; y++){
                rotated.setRGB(y, x, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    /**
     * Return the image corresponding to the player when he is looking RIGHT. The image is completely recalculated.
     * @param img Basic image player that will be rotated.
     * @return The image rotated in the right sens.
     */
    public static BufferedImage lookRight(BufferedImage img) {

        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(width, height, img.getType());

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height ; y++){
                rotated.setRGB(x, y, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    /**
     * Return the image corresponding to the player when he is looking LEFT. The image is completely recalculated.
     * @param img Basic image player that will be rotated.
     * @return The image rotated in the right sens.
     */
    public static BufferedImage lookLeft(BufferedImage img) {

        int height = img.getHeight();
        int width = img.getWidth();

        BufferedImage rotated = new BufferedImage(width, height, img.getType());

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height ; y++){
                rotated.setRGB((width-1)-x, y, img.getRGB(x,y));
            }
        }
        return rotated;
    }

    /**
     * Method to call only once at the beggining of the program. After that, the images will be accessible from everywhere in the code.
     * @return ArrayList containing images.
     */
    public static ArrayList<BufferedImage> loadImages(){
        imageList = new ArrayList<>();

        BufferedImage tilesheet = null;

        try{
            tilesheet = ImageIO.read(new File("Images/Tilesheet/tilesheet_complete.png"));
        }catch(IOException e){
            System.out.println("Loader"+e);
        }

        try{
            fog = ImageIO.read(new File("Images/fog.png"));
            shield = ImageIO.read(new File("Images/shield.png"));
            bomb = ImageIO.read(new File("Images/bomb2.png"));
            mine = ImageIO.read(new File("Images/mine2.png"));
            bullet = ImageIO.read(new File("Images/bullet.png"));
            bonus = ImageIO.read(new File("Images/bonus.png"));
        }catch(IOException e){
            System.out.println("Loader"+e);
        }
        int width = tilesheet.getWidth();
        int height = tilesheet.getHeight();
        int size = 64;
        int nbImagesWidth = width / size;
        int nbImagesHeight = height / size;

        for(int y = 0 ; y < nbImagesHeight ; y++){
            for(int x = 0 ; x < nbImagesWidth ; x++){
                BufferedImage temp = tilesheet.getSubimage(x*size , y*size, size, size);
                imageList.add(temp);
            }
        }
        return imageList;
    }
}
