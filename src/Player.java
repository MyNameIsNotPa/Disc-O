public class Player {

	private boolean isTripped;
	private String side;

	public Player(String side) {
		isTripped = false;
		this.side = side;
	}

	public void setTripped(boolean isTripped) {
		this.isTripped = isTripped;
	}

	public boolean isTripped() {
		return isTripped;
	}

	public String getSide() {
		return side;
	}
}