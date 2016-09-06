package game;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import game.player.Player;

/**
 * This class represents the game.
 * 
 * @author Hector (Fang Zhao 300364061)
 *
 */
public class Game {

    private List<Player> players;

    // the world clock starts from 00:00
    private LocalTime clock = LocalTime.of(0, 0);

    /*
     * @forTeam@
     * 
     * Ideally, other packages who want to visit game states should only interact with
     * this class. This class wraps all game-world-related and logic-related classes
     * inside, and provides getters, setters and reasonable functions for external
     * packages to interact with game. If you guys need any access point for some stuff,
     * like player, item, map, or time, and it is not provided within this class yet, let
     * me know.
     */

    /**
     * Constructor, take in an XML file that describes the game world, and construct it
     * with details in the file.
     * 
     * @param file
     */
    public Game(File file) {

        players = new ArrayList<>();

        // this timer will constantly decrease every player's life by 1 minute.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // decrease every player's life
                for (Player p : players) {
                    p.increaseHealth(-1);
                }

                // time advance by 1
                clock.plusMinutes(1);
            }
        }, 1000, 1000);
    }

    /**
     * This method returns the clock of the world. The world time is constantly advancing.
     * 
     * @return
     */
    public LocalTime getClock() {
        return clock;
    }

    /**
     * The clock tick is essentially a clock trigger, which allows the board to update the
     * current state. The frequency with which this is called determines the rate at which
     * the game state is updated.
     * 
     */
    public synchronized void update() {
        // TODO Auto-generated method stub

    }

}
