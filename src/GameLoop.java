import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
	public void handle(long now) {
		Main.update();
	}
}