import javafx.scene.media.AudioClip;
import java.nio.file.Paths;
import java.util.HashMap;

public class Sounds {

	private static HashMap<String, AudioClip> sounds
	 = new HashMap<String, AudioClip>();

	public static void addSound(String name) {
		sounds.put(name, new AudioClip(Paths.get("sounds/" + name + ".wav").toUri().toString()));
	}

	public static void setPan(String name, double pan) {
		sounds.get(name).setPan(pan);
	}

	public static void play(String name) {
		sounds.get(name).play();
	}

    public static void stop(String name) {
        sounds.get(name).stop();
    }
}