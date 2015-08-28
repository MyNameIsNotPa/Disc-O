import javafx.scene.media.MediaPlayer;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import javafx.scene.paint.Color;
import java.util.HashMap;

public class Director {

	private static MediaPlayer player;
	private static Boolean playing = false;
	private static long delta;
	private static Timeline timeline;
	private static double offset;
	private static double bpm = 0;
	private static double bps;
	private static Song current;
	private static double grace = 0;
	private static Disc disc;

	private static double total;
	private static double pos;

    private static double totaloff;
    private static double num = 1;

	private static long startTime;

    private static boolean tripped = false;

    private static HashMap<Player, Integer> scores;

	private static String parseLine(String line) {
		String temp = "";
		for (int i = 0; i < line.length(); i++) {
		    char c = line.charAt(i);
		    if (c == '0' || c == '3') {
		    	temp += " ";
		    } else {
		    	temp += "*";
		    }
		}
		return temp;
	}

	public static void advance() {

	}

	public static void startSong(Song song) {
        scores = new HashMap<>();
        scores.clear();
		disc = new Disc(Main.getPlayers().get(0), 0, 0);
		current = song;
		offset = song.getOffset();
		bpm = song.getBPM();
		bps = 60 / bpm;
		player = song.getPlayer();
        player.setOnReady(() -> {
            player.play();
            Main.startTimer(() -> updateSong());
        });
		startTime = System.currentTimeMillis();
		player.setOnEndOfMedia(() -> {
			Main.stopTimer();
			Input.unbindAll();
		});
		Input.bindKeyDown(KeyCode.A, () -> checkInput("down", Main.getPlayers().get(0)));
		Input.bindKeyDown(KeyCode.D, () -> checkInput("down", Main.getPlayers().get(1)));
		Input.bindKeyDown(KeyCode.Q, () -> checkInput("turbo", Main.getPlayers().get(0)));
		Input.bindKeyDown(KeyCode.E, () -> checkInput("turbo", Main.getPlayers().get(1)));
		Input.bindKeyUp(KeyCode.A, () -> checkInput("up", Main.getPlayers().get(0)));
		Input.bindKeyUp(KeyCode.D, () -> checkInput("up", Main.getPlayers().get(1)));
        Input.bindKeyUp(KeyCode.SPACE, () -> checkInput("offset", null));
        scores.put(Main.getPlayers().get(0), 0);
        scores.put(Main.getPlayers().get(1), 0);
        tripped = false;
	}

	public static void hitDisc(String type) {
        if (type == "normal") {
            if (pos > 0.5) {
                disc.setTarget(Math.ceil(getCurrentBeats()) + 1);
            } else {
                disc.setTarget(Math.floor(getCurrentBeats()) + 1);
            }
            disc.release("");
        } else if (type == "slow") {
            disc.setTarget(Math.ceil(getCurrentBeats()) + 1);
            disc.release("");
        } else if (type == "offbeat") {
            disc.setTarget(Math.floor(getCurrentBeats()) + 3);
            disc.release("offbeat");
        } else if (type == "turbo") {
            if (pos > 0.5) {
                disc.setTarget(Math.ceil(getCurrentBeats()) + 0.5);
            } else {
                disc.setTarget(Math.floor(getCurrentBeats()) + 0.5);
            }
            disc.release("turbo");
        }
        if (disc.getReceiver().getSide().equals("Left")) {
            disc.setReceiver(Main.getPlayers().get(1));
        } else {
            disc.setReceiver(Main.getPlayers().get(0));
        }
	}

    public static Player other(Player p) {
        for (Player pl : Main.getPlayers()) {
            if (pl != p) {
                return pl;
            }
        }
        return p;
    }

    public static void onTripped() {
        Sounds.playAfter(1, "ding");
        Main.waitToDo(getBps(), () -> {
            hitDisc("slow");
            Main.getPlayers().get(0).setTripped(false);
            Main.getPlayers().get(1).setTripped(false);
        });
    }

	public static void checkInput(String type, Player p) {
        if (p.isTripped()) {
            return;
        }
		updateBeat();
        if (type == "offset") {
            System.out.println(pos * bps);
        }
		if (p != disc.getReceiver()) {
			return;
		}
		if (type == "down") {
			double error = pos;
            if (pos < 0.1 || pos > 0.9) {
                if (getCurrentBeats() - disc.getTarget() < 0.5) {
                    System.out.println("Yeah!");
                }
                Sounds.setPan("click", disc.getReceiver().getSide() == "Left" ? 1 : 0);
                Sounds.play("click");
                disc.hold(disc.getTarget());
            } else if (pos > 0.40 && pos < 0.50) {
                if (getCurrentBeats() - disc.getTarget() < 0.5) {
                    System.out.println("Yeah!");
                }
                Sounds.play("turbo");
            } else {
                Sounds.play("trip");
                p.setTripped(true);
                if (disc.getReceiver() == p) {
                    scores.put(other(p), scores.get(other(p)) + 1);
                    System.out.println("P1 " + scores.get(Main.getPlayers().get(0)));
                    System.out.println("P2 " + scores.get(Main.getPlayers().get(1)));
                    tripped = true;
                }
            }
		} else {
			double error = pos;
            if (pos < 0.1 || pos > 0.9) {
                Sounds.setPan("tick", disc.getReceiver().getSide() == "Left" ? 1 : 0);
				Sounds.play("tick");
                if (disc.getBeatsSinceHeld() > 2.5) {
                    hitDisc("turbo");
                } else {
                    hitDisc("normal");
                }
			} else if (pos > 0.40 && pos < 0.50) {
                Sounds.play("off");
                hitDisc("offbeat");
            } else {
                Sounds.play("trip");
                p.setTripped(true);
                if (disc.getReceiver() == p) {
                    scores.put(other(p), scores.get(other(p)) + 1);
                    System.out.println("P1 " + scores.get(Main.getPlayers().get(0)));
                    System.out.println("P2 " + scores.get(Main.getPlayers().get(1)));
                    tripped = true;
                }
            }
		}
	}

	public static void updateBeat() {
		double passed = getCurrentTime();
		total = passed / bps;
		pos = total - Math.floor(total) - offset;
		if (pos < 0.1 || pos > 0.95) {
            if (tripped) {
                tripped = false;
                onTripped();
            }
			Main.getCircle().setFill(Color.rgb(255, 0, 0));
		} else {
			Main.getCircle().setFill(Color.rgb(0, 0, 0));
		}
	}

	public static void updateSong() {
		disc.update();
		updateBeat();
	}

    public static double getBps() {
        return bps;
    }

	public static double getCurrentBeats() {
		return getCurrentTime() / bps;
	}

    public static double getBestPosition() {
        return ((System.currentTimeMillis() - startTime) / 1000.0) / bps;
    }

	public static double getCurrentTime() {
		return ((System.currentTimeMillis() - startTime) / 1000.0);
	}
}