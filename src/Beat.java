public class Beat {

	private int time;
	private boolean isTurbo;
	private boolean isOffbeat;
	private boolean hit;
	private int player;

	public Beat(int time, boolean isTurbo, boolean isOffbeat, int player) {
		this.time = time;
		this.isTurbo = isTurbo;
		this.isOffbeat = isOffbeat;
		this.hit = false;
		this.player = player;
	}

	public Beat() {
		this(0, false, false, 1);
	}

	public void setTurbo(boolean isTurbo) {
		this.isTurbo = isTurbo;
	}

	public void setOffbeat(boolean isOffbeat) {
		this.isOffbeat = isOffbeat;
	}

	public boolean isOffbeat() {
		return isOffbeat;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean getHit() {
		return hit;
	}

	public int getTime() {
		return time;
	}

	public int getPlayer() {
		return player;
	}
}