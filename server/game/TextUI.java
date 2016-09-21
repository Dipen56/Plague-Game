package server.game;

import java.util.Scanner;

import server.game.player.Avatar;
import server.game.player.Player;
import server.game.player.Position;
import server.game.world.Area;

/**
 * This is a text-based UI client. It provides a tiny (hard-coded) world and a simple text
 * UI. It's playable. It will NOT be used in our final game. It is only here for testing.
 *
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class TextUI {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final int MOCK_UID = 123;

    public TextUI() {

        Game game = setupGame();

        // game running!
        runGame(game);

        // when game stops, some clean-up process.
        gameStop(game);

        SCANNER.close();
    }

    private Game setupGame() {

        Game game = new Game(TestConst.world, TestConst.areas);

        // mock player
        Player player = new Player(MOCK_UID, Avatar.Avatar_1, "Hector");
        game.joinPlayer(player);

        return game;
    }

    private void runGame(Game game) {

        Player player = game.getPlayerById(MOCK_UID);

        while (true) {
            // print board
            Position currentPosition = player.getPosition();
            Area currentArea = game.getAreas().get(currentPosition.areaId);
            int width = currentArea.getWidth();
            int replaceIndex = currentPosition.y * (width + 1) + currentPosition.x;
            char[] chars = currentArea.toString().toCharArray();
            chars[replaceIndex] = player.getDirection().getChar();
            String boardString = new String(chars);
            System.out.println(boardString);

            // step run the game
            StepRun(game, player);
        }

    }

    private void StepRun(Game game, Player player) {
        char input = parseChar();

        switch (input) {
        case 'w':
            // Move forward
            if (!game.playerMoveForward(MOCK_UID)) {
                System.out.println("Failed to move forward");
            }
            break;
        case 's':
            // Move backward
            if (!game.playerMoveBackward(MOCK_UID)) {
                System.out.println("Failed to move backward");
            }
            break;
        case 'q':
            // Turn left
            game.playerTurnLeft(MOCK_UID);
            break;
        case 'e':
            // Turn right
            game.playerTurnRight(MOCK_UID);
            break;
        case 'a':
            // Move left
            if (!game.playerMoveLeft(MOCK_UID)) {
                System.out.println("Failed to move left");
            }
            break;
        case 'd':
            // move right
            if (!game.playerMoveRight(MOCK_UID)) {
                System.out.println("Failed to move right");
            }
            break;
        case 'f':
            // the player wants to unlock a chest
            if (!game.playerUnlockLockable(MOCK_UID)) {
                System.out.println("Failed to unlock the Lockable");
            }
            break;
        case 'g':
            // the player wants to take items in a chest in front
            if (!game.playerTakeItemsFromContainer(MOCK_UID)) {
                System.out.println("No items was taken from chest");
            }
            break;
        case 'r':
            // the player wants to enter a room
            if (!game.playerTransit(MOCK_UID)) {
                System.out.println("Failed to enter/exit the room");
            }
            break;
        case 'i':
            // the player wants to see what's inside the inventory
            game.getPlayerInventory(MOCK_UID).stream()
                    .forEach(e -> System.out.println(e));
            break;
        case 'c':
            // the player wants to see time & health left
            System.out.println(
                    "Your health left: " + game.getPlayerHealth(MOCK_UID) + " seconds");
            System.out.println("World clock: " + game.getClock());
            break;
        case '1':
            // the player wants to use the first item in inventory
            if (!game.playerUseItem(MOCK_UID, 0)) {
                System.out.println(
                        "Player has no item in inventory, or it cannot be used.");
            }
            break;
        case '2':
            // the player wants to use the second item in inventory
            if (!game.playerUseItem(MOCK_UID, 1)) {
                System.out.println(
                        "Player has no such item in inventory, or it cannot be used.");
            }
            break;
        case '0':
            // the player wants to destroy the first item in inventory
            if (!game.playerDestroyItem(MOCK_UID, 0)) {
                System.out.println(
                        "Player has no time in inventory, or it cannot be destroyed.");
            }
            break;

        default:

        }

    }

    private void gameStop(Game game) {

    }

    private static char parseChar() {

        String line = SCANNER.nextLine();

        // if user asked for help, print out help message
        if (line.equals("help")) {
            helpMessage();
            line = SCANNER.nextLine();
        }

        while (line.length() > 1) {
            System.out.println("Please only type in one char");
            line = SCANNER.nextLine();
        }

        return line.charAt(0);
    }

    // @formatter:off
    /**
     * This method print out available input keys.
     */
    private static void helpMessage() {
        String s = "[Help]\n"
                + "Only type in lower case, and only one character.\n"
                + "Move forward: w\n"
                + "Move backward: s\n"
                + "Move left: a\n"
                + "Move right: d\n"
                + "Turn left: q\n"
                + "Trun right: e\n"
                + "Unlock Lockable: f\n"
                + "Take items in Container: g\n"
                + "Enter/Exit room: r\n"
                + "Open inventory: i\n"
                + "Clock & Time left: c\n"
                + "Use the 1st item in inventory: 1\n"
                + "Use the 2nd item in inventory: 2\n"
                + "Destroy the 1st item in inventory: 0\n";
        System.out.println(s);
    }
    // @formatter:on

    public static void main(String[] args) {
        new TextUI();
    }
}
