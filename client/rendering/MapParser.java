package client.rendering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * this class represent the parser for the game world. this class will basically
 * take a txt file and create a string form of the world map which will be used
 * to render the game world.
 * 
 * @author Dipen
 *
 */
public class MapParser {
	private static final String WORLD_MAP_FILE = "/worldmap.txt";
	private String[][] map;
	private Scanner sc;

	public MapParser(int rows, int cols) {
		map = new String[rows][cols];
		parse();
	}

	public void parse() {
		File file = new File("/Users/Raff/Documents/University/2016/TRIMESTER 2/SWEN222(WORKSPACE)/PROJECT GAME/resource/worldmap.txt");
		String line;
		int row = 0;
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				String[] lineChar = line.split(",");
				for (int col = 0; col < lineChar.length; col++) {
					// these are the items what will be in the world atm i only
					// know of 3 but i am sure there will be more therefore i
					// have left this open just need to add more if statement
					// for new items.
					if (lineChar[col].equals("T")) {
						map[row][col] = "tree";
					} else if (lineChar[col].equals("C")) {
						map[row][col] = "chest";
					} else if (lineChar[col].equals("-")) {
						map[row][col] = "grass";
					}
				}
				row++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// for test if we want to add more items to the world
		// for (int i = 0; i < map.length; i++) {
		// for (int j = 0; j < map[0].length; j++) {
		// System.out.print(map[i][j] + ",");
		// }
		// System.out.println("");
		// }
	}

	public String[][] getMap() {
		return map;
	}

}
