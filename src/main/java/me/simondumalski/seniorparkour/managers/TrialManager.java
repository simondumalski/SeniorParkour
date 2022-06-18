package me.simondumalski.seniorparkour.managers;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.utils.Parkour;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class TrialManager {

    private final Main plugin = Main.getInstance();
    private final HashMap<UUID, Parkour> playersInParkour = new HashMap<>();
    private final HashMap<UUID, Location> playersInParkourLocation = new HashMap<>();
    private final HashMap<UUID, Integer> timeInParkour = new HashMap<>();

    /**
     * Returns the UUIDs of the players that are currently in a Parkour Trial
     * @return Set of UUIDs
     */
    public Set<UUID> getPlayersInParkour() {
        return playersInParkour.keySet();
    }

    /**
     * Starts a parkour trial for the specified player in the specified Parkour Course
     * @param player Player to start a trial for
     * @param parkour Parkour Course to start the trial in
     */
    public void startParkour(Player player, Parkour parkour) {
        playersInParkour.put(player.getUniqueId(), parkour);
        playersInParkourLocation.put(player.getUniqueId(), parkour.getStartLocation());
        timeInParkour.put(player.getUniqueId(), 0);
    }

    /**
     * Advances the specified player in their Parkour Course
     * @param player Player to advance
     */
    public void advanceParkour(Player player) {

        //Get the Parkour Course the player is in
        Parkour parkour = playersInParkour.get(player.getUniqueId());

        //Get the location in the parkour the player is currently at
        Location currentLocation = playersInParkourLocation.get(player.getUniqueId());

        //Get the checkpoint number
        int checkpointNumber = parkour.getCheckpointNumber(currentLocation);

        //Advance the player in the Parkour Course
        playersInParkourLocation.replace(player.getUniqueId(), parkour.getCheckpoint(checkpointNumber));

    }

    /**
     * Advances the specified player in their Parkour Course, use if only 1 checkpoint
     * @param player Player to advance
     */
    public void specialAdvanceParkour(Player player) {

        //Get the Parkour Course the player is in
        Parkour parkour = playersInParkour.get(player.getUniqueId());

        //Advance the player in the Parkour Course
        playersInParkourLocation.replace(player.getUniqueId(), parkour.getFirstCheckpoint());

    }

    /**
     * Ends the player's time in the Parkour Course
     * @param player Player to end
     * @return Time spent in the Parkour Course
     */
    public int endParkour(Player player) {

        //Get the time the player spent in the Parkour Course
        int timeInCourse = timeInParkour.get(player.getUniqueId());

        //Remove the player from all the lists
        playersInParkour.remove(player.getUniqueId());
        playersInParkourLocation.remove(player.getUniqueId());
        timeInParkour.remove(player.getUniqueId());

        //Return the time the player spent in the Parkour Course
        return timeInCourse;
    }

    /**
     * Forcefully removes the player from a Parkour Course. Used when a player quits the server
     * @param player Player to remove
     */
    public void forceEnd(Player player) {

        playersInParkour.remove(player.getUniqueId());
        playersInParkourLocation.remove(player.getUniqueId());
        timeInParkour.remove(player.getUniqueId());

    }

    /**
     * Returns true/false if the specified player is currently in a Parkour Course
     * @param player Player to check
     * @return True/false
     */
    public boolean isPlayerInParkour(Player player) {
        return playersInParkour.containsKey(player.getUniqueId());
    }

    /**
     * Returns the location the specified player is in the Parkour Course
     * @param player Player whose location to get
     * @return Location in the Parkour Course
     */
    public Location getParkourLocation(Player player) {
        return playersInParkourLocation.get(player.getUniqueId());
    }

    /**
     * Returns the Parkour Course the specified player is currently in
     * @param player Player whose Parkour Course to get
     * @return Parkour Course the player is currently in
     */
    public Parkour getParkour(Player player) {
        return playersInParkour.get(player.getUniqueId());
    }

    /**
     * Increments the players time in a Parkour Trial by 1 second
     * @param player Player whose time should be incremented
     */
    public void incrementTime(Player player) {
        timeInParkour.replace(player.getUniqueId(), timeInParkour.get(player.getUniqueId()) + 1);
    }

    /**
     * Returns the amount of time a player has currently spent in a Parkour Trial
     * @param player Player whose time to get
     * @return Amount of time spent in a trial
     */
    public int getTimeInParkour(Player player) {
        return timeInParkour.get(player.getUniqueId());
    }

}
