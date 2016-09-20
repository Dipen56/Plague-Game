package anotherServer;

import java.util.Map;
import java.util.Scanner;

import server.game.player.Position;

/**
 * This class is a utility class containing static methods to parse communications between
 * server and client.
 * 
 * 
 * this class represent the parser for the game world. this class will basically take a
 * txt file and create a string form of the world map which will be used to render the
 * game world.
 * 
 * @author Dipen, Rafaela (Just put your Id here)
 *
 */
public class ParserUtilities {

    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Private constructor. No instantiation is allowed.
     */
    private ParserUtilities() {
    }

    /**
     * This method reads in a String and parse it into a char[][], which is used on client
     * side to render the world. The char[][] is recorded in <i><b>areas</b></i> given as
     * parameter. The input String is expected to have the following format:
     * 
     * <p>
     * Say we have a 3 by 3 room (room's areaId is 1):<br>
     * <p>
     * EEE<br>
     * EEC<br>
     * ETE<br>
     * 
     * 
     * <p>
     * E stands for empty space, C stands for chest, T stands for door.
     * 
     * <p>
     * The string should be <i>"1,3,3\nEEE\nEEC\nETE"</i>
     * 
     * @param areas
     * @param string
     */
    public static void parseMap(Map<Integer, char[][]> areas, String string) {

        Scanner scanner = new Scanner(string);
        String line = scanner.nextLine();

        // first get the width and height
        String[] widthHeight = line.split(",");
        int areaId = -1;
        int width = -1;
        int height = -1;
        try {
            areaId = Integer.valueOf(widthHeight[0]);
            width = Integer.valueOf(widthHeight[1]);
            height = Integer.valueOf(widthHeight[2]);

            if (areaId < 0 || width <= 0 || height <= 0) {
                System.out.println(
                        "Error occurred when parsing map. Negative areaId, Width or height. String input is: "
                                + string);
                scanner.close();
                return; // do not crash the game.
            }
        } catch (NumberFormatException e1) {
            System.out.println(
                    "Error occurred when parsing map. AreaId, width or height is not integer. String input is: "
                            + string);
            scanner.close();
            return; // do not crash the game.
        }

        // then set the map char by char
        char[][] map = new char[height][width];

        int row = 0;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            if (line.length() != width) {
                System.out.println(
                        "Error occurred when parsing map. Width mismatch at row: " + row
                                + ". String input is: " + string);
                scanner.close();
                return; // do not crash the game.
            }

            for (int i = 0; i < line.length(); i++) {
                try {
                    map[row][i] = line.charAt(i);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(
                            "Error occurred when parsing map. Index out of boundary at row: "
                                    + row + ", column: " + i + ". String input is: "
                                    + string);
                    scanner.close();
                    return; // do not crash the game.
                }
            }
            row++;
        }

        if (row != height) {
            System.out.println("Error occurred when parsing map. Height mismatch: " + row
                    + ". String input is: " + string);
            scanner.close();
            return; // do not crash the game.
        }

        // job done, let's put it in map.
        areas.put(areaId, map);

        scanner.close();
    }

    /**
     * This method reads in a String and parse it into a Position, which is used on client
     * side to locate all connected players and render them. Result is recorded in
     * <i><b>positions</b></i> given as parameter. The input String is expected to have
     * the following format:
     * 
     * <p>
     * Say Player(uId: 123) is in area(areaId: 456), and his coordinates is (78, 90):
     * 
     * <p>
     * The string should be <i>"123,456,78,90"</i>
     * 
     * @param areas
     * @param string
     */
    public static void parsePosition(Map<Integer, Position> positions, String string) {

        Scanner scanner = new Scanner(string);
        String line = scanner.nextLine();
        String[] nums = line.split(",");

        int uId = -1;
        int areaId = -1;
        int x = -1;
        int y = -1;

        try {
            uId = Integer.valueOf(nums[0]);
            areaId = Integer.valueOf(nums[1]);
            x = Integer.valueOf(nums[2]);
            y = Integer.valueOf(nums[3]);

            if (uId < 0 || areaId < 0 || x <= 0 || y <= 0) {
                System.out.println(
                        "Error occurred when parsing postion. Negative uId, areaId, x or y. String input is: "
                                + string);
                scanner.close();
                return; // do not crash the game.
            }
        } catch (NumberFormatException e1) {
            System.out.println(
                    "Error occurred when parsing postion. uId, areaId, x or y is not integer. String input is: "
                            + string);
            scanner.close();
            return; // do not crash the game.
        }

        // job done, let's put it in map.
        positions.put(uId, new Position(x, y, areaId));
        scanner.close();
    }

    /**
     * This helper method parse user's input as integer, and limits the maximum and
     * minimum boundary of it.
     * 
     * @param min
     *            --- the minimum boundary of input as an integer
     * @param max
     *            --- the maximum boundary of input as an integer
     * @return --- the parsed integer
     */
    public static int parseInt(int min, int max) {
        while (true) {
            String line = SCANNER.nextLine();

            try {
                // parse the input
                int i = Integer.valueOf(line);
                if (i >= min && i <= max) {
                    // a good input
                    return i;
                } else {
                    // a out of boundary input, let the user retry.
                    System.out.println(
                            "Please choose between " + min + " and " + max + ":");
                    continue;
                }
            } catch (NumberFormatException e) {
                // the input is not an integer
                System.out.println("Please enter an integer:");
                continue;
            }
        }
    }

    /**
     * This method read in a user input.
     * 
     * @return
     */
    public static String parseString() {
        return SCANNER.nextLine();
    }

}
