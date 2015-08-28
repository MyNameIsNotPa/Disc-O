import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Main extends Application {

	private static Scene scene;
	private static Pane root;
	private static Circle c;
	private static ArrayList<Player> players;
	private static GameLoop frame;
	private static Runnable event;

	public void start(Stage stage) throws Exception {
		players = new ArrayList<Player>();
		players.add(new Player("Left"));
		players.add(new Player("Right"));
		frame = new GameLoop();
		root = new Pane();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		c = new Circle(20, 20, 10);
		root.getChildren().add(c);
		Input.bindInputs();
		Sounds.addSound("up");
		Sounds.addSound("down");
		Sounds.addSound("off");
		Sounds.addSound("ding");
		Sounds.addSound("score");
		Sounds.addSound("hat");
		Sounds.addSound("click");
		Sounds.addSound("tick");
        Sounds.addSound("charge");
        Sounds.addSound("turbo");
        Sounds.addSound("trip");
        Sounds.addSound("3");
        Sounds.addSound("2");
        Sounds.addSound("1");
        Director.startSong(new Song("RALLY"));
	}

    public static void waitToDo(double seconds, Runnable thingToDo) {
        PauseTransition t1 = new PauseTransition(
                Duration.seconds(seconds));
        t1.setOnFinished(e -> {
            thingToDo.run();
        });
        t1.play();
    }

	public static ArrayList<Player> getPlayers() {
		return players;
	}

	public static void update() {
		event.run();
	}

	public static Scene getScene() {
		return scene;
	}

	public static Circle getCircle() {
		return c;
	}

	public static void startTimer(Runnable eventx) {
		event = eventx;
		frame.start();
	}

	public static void stopTimer() {
		frame.stop();
	}

	public static GameLoop getFrame() {
		return frame;
	}

	public static void main(String[] args) {
		launch(args);
	}
}