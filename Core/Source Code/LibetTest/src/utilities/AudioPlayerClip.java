package utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayerClip implements LineListener {
	private Clip clip;
	private long beepTime = 0;
	private SimpleDateFormat df_localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	public AudioPlayerClip(String audio_path) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		File file = new File(audio_path);
		if (file.exists()) {
			AudioInputStream sound = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(sound);
			clip.addLineListener(this);
		} else {
			throw new RuntimeException("Audio file not found");
		}
	}

	public void start() {
		clip.setFramePosition(0);
		clip.start();
	}

	public void stop() {
		clip.stop();
	}
	
	public void shutdown() {
		clip.close();
	}
	
	public long getBeepTime() {
		return beepTime;
	}

	@Override
	public void update(LineEvent event) {
		if(event.getType() == Type.START) {
			System.out.println("Beep start time: " + df_localTime.format(new Date()));
			beepTime = System.nanoTime();
		}		
	}
}
