package game.snl;

public class Player {
	String name;
	int position;
	boolean hasEntered;

	public Player(String name) {
		this.name = name;
		this.position = 0;
		this.hasEntered = false;
	}

	public void changePosition(int newPosition) {
		this.position = newPosition;
	}

	public int getPosition() {
		return this.position;
	}

	public void enter() {
		this.hasEntered = true;
	}
	
	public void moveTo1() {
		this.hasEntered = false;
		this.position = 0;
	}

}