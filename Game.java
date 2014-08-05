package game.snl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Game {

	public int[] board;
	public boolean hasGameEnded = false;
	public int MAX_POSITION = 100;
	public Player[] players;

	public Game(int numOfPlayers) throws IOException {
		createBoard("snakes.txt", "ladders.txt");
		players = new Player[numOfPlayers];
		for (int i = 0; i < numOfPlayers; i++)
			players[i] = new Player("Player" + (i + 1));
	}

	public static void main(String[] args) throws IOException {
		// int numOfPlayers = Integer.parseInt(args[0]);
		int numOfPlayers = 6;
		Game game = null;
		game = new Game(numOfPlayers);
		
		while (!game.hasGameEnded) {
			for (Player p : game.players) {
				int diceValue = Game.rollDice();
				int numOfSix = 0;
				if (diceValue == 6) {
					while (numOfSix < 3 && diceValue == 6) {
						numOfSix++;
						System.out.println(game.updatePosition(p, diceValue));
						diceValue = Game.rollDice();
					}
					if (numOfSix == 3) {
						p.moveTo1();
					} else
						System.out.println(game.updatePosition(p, diceValue));
				} else
					System.out.println(game.updatePosition(p, diceValue));
				if (p.position == game.MAX_POSITION) {
					game.hasGameEnded = true;
					System.out.println("GAME ENDED\nWinner:" + p.name);
					break;
				}
			}
			System.out.println();
		}
	}

	private String updatePosition(Player p, int diceValue) {
		if (!p.hasEntered) {
			if (diceValue == 1 || diceValue == 6) {
				p.hasEntered = true;
				return p.name + " enters the board";
			}
		} else {
			if (p.getPosition() + diceValue < MAX_POSITION)
				if (board[p.getPosition() + diceValue] < 0)
					return p.name + " gets bitten by a snake at "
							+ (p.getPosition() + diceValue);
				else if (board[p.getPosition() + diceValue] > 0)
					return p.name + " takes the ladder at "
							+ (p.getPosition() + diceValue);
			if ((p.getPosition() + diceValue) <= MAX_POSITION) {
				p.changePosition(p.getPosition() + diceValue
						+ board[p.getPosition() + diceValue]);
				return p.name + " rolls " + diceValue + " and moves to "
						+ p.getPosition();
			}
		}
		return p.name + " did not move";

	}

	public static int rollDice() {
		Random rand = new Random();
		return rand.nextInt((6 - 1) + 1) + 1;
	}

	public void createBoard(String snakesFile, String laddersFile)
			throws IOException {
		board = new int[MAX_POSITION + 1];
		loadSnakes(snakesFile);
		loadLadders(laddersFile);
	}

	private void loadSnakes(String filename) throws NumberFormatException,
			IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = null;
		while ((line = reader.readLine()) != null) {
			int start = Integer.parseInt(line.split(" ")[0]);
			int end = Integer.parseInt(line.split(" ")[1]);
			// snakes.put(start, end);
			board[start] = end - start;

		}
		reader.close();
	}

	private void loadLadders(String filename) throws NumberFormatException,
			IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = null;
		while ((line = reader.readLine()) != null) {
			int start = Integer.parseInt(line.split(" ")[0]);
			int end = Integer.parseInt(line.split(" ")[1]);
			// ladders.put(start, end);
			board[start] = end - start;
		}
		reader.close();
	}

}
