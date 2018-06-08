import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;


import  sun.audio.*;   

@SuppressWarnings("restriction")
public class OtherMusic {
	static MediaPlayer mediaPlayer;
	


	public OtherMusic()
	{
		final JFXPanel fxPanel = new JFXPanel();
		playAudio(getClass().getResource("Songs/BenMaze.mp3"));
	}
	public static void playAudio(URL url)
	{
		Media hit = new Media(url.toString());
		mediaPlayer = new MediaPlayer(hit);
		//mediaPlayer.play();
	}
	public static void play(){
		mediaPlayer.play();
	}

}