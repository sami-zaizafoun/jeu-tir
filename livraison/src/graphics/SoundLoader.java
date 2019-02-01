package graphics;

import java.io.File;
import java.net.*;
import java.util.Arrays;
import java.util.logging.*;
import javafx.scene.media.*;

/**
* Class that loads all the sound files included in the game.
*/

public class SoundLoader {
    private Media loadSound;
    private MediaPlayer playSound;

    /**
     * Constructor method that takes an action as a parameter to play a certain sound
     * @param action int value that represents an action performed (Shooting, getting hurt, etc..)
     */
    public SoundLoader(int action) {
        String mainPath = new File("").getAbsolutePath();
        File audioFiles = new File("Sons/");
        File[] fileList = audioFiles.listFiles();
        Arrays.sort(fileList);
        for (File f: fileList){
            String sound = mainPath + "/Sons/"+fileList[action].getName();
            try {
                final URL songPath = new File(sound).toURI().toURL();
                loadSound = new Media(songPath.toString());
            } catch (MalformedURLException ex) {
                Logger.getLogger(SoundLoader.class.getName()).log(Level.SEVERE, null, ex);
            }

            playSound = new MediaPlayer(loadSound);
            playSound.setVolume(0.3);
            playSound.play();
        }
    }

    /**
     * Method to play a track in a loop
     */
    public void loopTrack(){
        playSound.setOnEndOfMedia(() -> {
            playSound.seek(playSound.getStartTime());
        });
    }
}
