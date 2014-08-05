package game.snl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game {
	static Map<Integer, Integer> snakes = new HashMap<Integer, Integer>();
	static Map<Integer, Integer> ladders = new HashMap<Integer, Integer>();
	static boolean hasGameEnded = false;
	static final int MAX_POSITION = 100;
	static Player[] players;

	public static void main(String[] args) {
		// int numOfPlayers = Integer.parseInt(args[0]);
		int numOfPlayers = 6;

		try {
			setUpGame(numOfPlayers);
		} catch (FileNotFoundException e) {
			System.out.println("error exiting..");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("error exiting..");
			e.printStackTrace();
			System.exit(0);
		}
		play();
	}

	private static void setUpGame(int numOfPlayers) throws IOException {
		createBoard();
		players = new Player[numOfPlayers];
		for (int i = 0; i < numOfPlayers; i++)
			players[i] = new Player("Player" + (i + 1));
	}

	private static void play() {
		while (!hasGameEnded) {
			for (Player p : players) {
				int diceValue = rollDice();
				int numOfSix = 0;
				if (diceValue == 6) {
					while (numOfSix < 3 && diceValue == 6) {
						numOfSix++;
						System.out.println(p.name
								+ " rolls a 6 and gets an extra turn");
						updatePosition(p, diceValue);
						diceValue = rollDice();
					}
					if (numOfSix == 3) {
						moveToOne(p);
						System.out
								.println(p.name
										+ " rolls 3 6s consecutively and moved to start position");
					} else
						updatePosition(p, diceValue);
				} else
					updatePosition(p, diceValue);
				
				System.out.println(p.name + " rolls " + diceValue
						+ " and moves to " + p.getPosition());

				if (p.position == MAX_POSITION) {
					hasGameEnded = true;
					System.out.println("GAME ENDED\nWinner:" + p.name);
					break;
				}
			}
			System.out.println();
		}
	}

	private static void moveToOne(Player p) {
		p.moveTo1();
	}

	private static void updatePosition(Player p, int diceValue) {
		if (!p.hasEntered) {
			if (diceValue == 1 || diceValue == 6) {
				p.hasEntered = true;
				System.out.println(p.name + " enters the board");
			}

		} else {
			if (snakes.containsKey(p.getPosition() + diceValue)) {
				p.changePosition(snakes.get(p.getPosition() + diceValue));
				System.out.println(p.name + " gets bitten by a snake at "
						+ (p.getPosition() + diceValue));
			} else if (ladders.containsKey(p.getPosition() + diceValue)) {
				p.changePosition(ladders.get(p.getPosition() + diceValue));
				System.out.println(p.name + " takes the ladder at "
						+ (p.getPosition() + diceValue));
			} else if ((p.getPosition() + diceValue) <= MAX_POSITION)
				p.changePosition(p.getPosition() + diceValue);
		}

	}

	private static int rollDice() {
		Random rand = new Random();
		return rand.nextInt((6 - 1) + 1) + 1;
	}

	private static void createBoard() throws IOException {
		loadSnakes();
		loadLadders();
	}

	private static void loadSnakes() throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader("snakes.txt"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			int start = Integer.parseInt(line.split(" ")[0]);
			int end = Integer.parseInt(line.split(" ")[1]);
			snakes.put(start, end);

		}
	}

	private static void loadLadders() throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader("ladders.txt"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			int start = Integer.parseInt(line.split(" ")[0]);
			int end = Integer.parseInt(line.split(" ")[1]);
			ladders.put(start, end);

		}
	}

}
