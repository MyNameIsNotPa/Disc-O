import javafx.scene.shape.Circle;
import javafx.scene.effect.MotionBlur;
import java.util.HashMap;

public class Disc {

	private Player receiver;
	private double targetTime;
	private double letTime;
	private double xPos;
	private Circle circle;
	private MotionBlur blur;
    private double heldBeat;
    private boolean holding;
    private String move;
    private boolean playedSounds = false;
    private HashMap<String, Runnable> actions;

	public Disc(Player receiver, double targetTime, double xPos) {
		this.receiver = receiver;
		this.targetTime = targetTime;
		this.xPos = xPos;
		letTime = 0;
		circle = Main.getCircle();
		blur = new MotionBlur();
		circle.setEffect(blur);
        move = "";
        actions = new HashMap<>();
        actions.put("offbeat", () -> {
            Main.waitToDo(Director.getBps() * 0.5, () -> {
                Sounds.play("hat");
            });
            Main.waitToDo(Director.getBps() * 1.5, () -> {
                Sounds.play("hat");
            });
            Main.waitToDo(Director.getBps() * 2.5, () -> {
                Sounds.play("hat");
            });
        });
	}

	public void setTarget(double targetTime) {
		this.letTime = Director.getCurrentBeats();
		this.targetTime = targetTime;
	}

	public void setReceiver(Player receiver) {
		this.receiver = receiver;
	}

	public Player getReceiver() {
		return receiver;
	}

	public double getTarget() {
		return targetTime;
	}

    public double getBeatsSinceHeld() {
        return Director.getCurrentBeats() - heldBeat;
    }

    public void hold(double heldBeat) {
        this.heldBeat = heldBeat;
        holding = true;
        Player holder = getReceiver();
    }

    public void release(String move) {
        Sounds.stop("charge");
        holding = false;
        setMove(move);
        if (actions.containsKey(move)) {
            actions.get(move).run();
        }
    }

    public void setMove(String move) {
        this.move = move;
    }

	private double clamp(double val) {
		return Math.min(Math.max(val, 0), 1);
	}

	public void update() {
		xPos = clamp(2 * (targetTime - Director.getCurrentBeats()) / ((targetTime - letTime) * 2));
		if (receiver.getSide() == "Right") {
			xPos = 1 - xPos;
		}
		circle.setCenterX(20 + (xPos * 600));
		circle.setCenterY(200 - 100 * Math.sin(3.14159 / 4 + (xPos * (3.14159 / 2))));
		blur.setRadius(70 * (0.5 - Math.abs(0.5 - xPos)));
	}
}