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
		File file = new File("C:/Users/Angelo/Documents/University/SWEN221/Workspace/Plague Project/resource/worldmap.txt");
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

	}

	public String[][] getMap() {
		return map;
	}

}
