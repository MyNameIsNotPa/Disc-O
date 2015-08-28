import javafx.scene.Scene;
import java.util.HashSet;
import java.util.HashMap;
import javafx.scene.input.KeyCode;

public class Input {

	private static HashMap<KeyCode, Runnable> keyDownEvents
	 = new HashMap<KeyCode, Runnable>();
	private static HashMap<KeyCode, Runnable> keyUpEvents
	 = new HashMap<KeyCode, Runnable>();
	private static HashSet<KeyCode> pressed
	 = new HashSet<KeyCode>();

	public static void bindInputs() {
		Main.getScene().setOnKeyPressed(e -> {
			if (pressed.contains(e.getCode())) {
				return;
			}
			pressed.add(e.getCode());
			for (KeyCode code : keyDownEvents.keySet()) {
				if (e.getCode() == code) {
					keyDownEvents.get(code).run();
				}
			}
		});
		Main.getScene().setOnKeyReleased(e -> {
			pressed.remove(e.getCode());
			for (KeyCode code : keyUpEvents.keySet()) {
				if (e.getCode() == code) {
					keyUpEvents.get(code).run();
				}
			}
		});
	}

	public static void unbindAll() {
		keyDownEvents.clear();
		keyUpEvents.clear();
	}

	public static void bindKeyDown(KeyCode code, Runnable event) {
		keyDownEvents.put(code, event);
	}

	public static void bindKeyUp(KeyCode code, Runnable event) {
		keyUpEvents.put(code, event);
	}
}