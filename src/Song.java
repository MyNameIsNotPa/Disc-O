import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;

public class Song {

	private Media audio;
	private MediaPlayer player;
	private double bpm;
	private double qtime;
	private double offset;
	private File chart;

	private static Double getBPM(File file) throws Exception {
		Scanner s = new Scanner(file);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.length() > 5 && line.substring(0, 3).equals("BPM")) {
				Double bpm = Double.parseDouble(line.substring(line.indexOf("=") + 1, line.length()));
				return bpm;
			}
		}
		return 0.0;
	}

	private static Double getOffset(File file) throws Exception {
		Scanner s = new Scanner(file);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.length() > 5 && line.substring(0, 6).equals("OFFSET")) {
				Double bpm = Double.parseDouble(line.substring(line.indexOf("=") + 1, line.length()));
				return bpm;
			}
		}
		return 0.0;
	}

	public Song(String name) throws Exception {
		audio = new Media(Paths.get("music/" + name + ".mp3").toUri().toString());
		chart = new File("music/" + name + ".song");
		bpm = getBPM(chart);
		offset = getOffset(chart);
		player = new MediaPlayer(audio);
	}

	public double getBPM() {
		return bpm;
	}

	public Media getAudio() {
		return audio;
	}

	public MediaPlayer getPlayer() {
		return player;
	}

	public double getOffset() {
		return offset;
	}
}