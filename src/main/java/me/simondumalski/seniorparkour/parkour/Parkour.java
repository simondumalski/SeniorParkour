package me.simondumalski.seniorparkour.parkour;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Parkour {

    private final String courseName;
    private final Location startLocation;
    private Location endLocation;
    private final List<Location> checkpoints;

    /**
     * Constructor for creating a new Parkour Course
     * @param courseName Name of the course
     * @param startLocation Starting location of the course
     */
    public Parkour(String courseName, Location startLocation) {
        this.courseName = courseName;
        this.startLocation = startLocation;
        checkpoints = new ArrayList<>();
    }

    /**
     * Constructor for loading a Parkour Course from file
     * @param courseName Name of the course
     * @param startLocation Starting location of the course
     * @param endLocation Ending location of the course
     * @param checkpoints Checkpoints in the course
     */
    public Parkour(String courseName, Location startLocation, Location endLocation, List<Location> checkpoints) {
        this.courseName = courseName;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.checkpoints = checkpoints;
    }

    /**
     * Returns the name of the Parkour Course
     * @return Parkour Course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Returns the start location of the Parkour Course
     * @return Start Location
     */
    public Location getStartLocation() {
        return startLocation;
    }

    /**
     * Returns the end location of the Parkour Course
     * @return End Location
     */
    public Location getEndLocation() {
        return endLocation;
    }

    /**
     * Sets the end location for the Parkour Course
     * @param endLocation End location
     */
    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    /**
     * Returns the list of checkpoints for the Parkour Course
     * @return List of Checkpoint Locations
     */
    public List<Location> getCheckpoints() {
        return checkpoints;
    }

    /**
     * Returns the location of the specified checkpoint for the Parkour Course
     * @param checkpointNumber Checkpoint number
     * @return Location of the checkpoint
     */
    public Location getCheckpoint(int checkpointNumber) {
        return checkpoints.get(checkpointNumber);
    }

    /**
     * Returns the location of the first checkpoint in the Parkour Course
     * @return Location of the first checkpoint
     */
    public Location getFirstCheckpoint() {
        return checkpoints.get(0);
    }

    /**
     * Returns the location of the last checkpoint in the Parkour Course
     * @return Location of the last checkpoint
     */
    public Location getLastCheckpoint() {
        return checkpoints.get(getCheckpointsAmount() - 1);
    }

    /**
     * Returns the next checkpoint in the list of checkpoints
     * @param previousCheckpointLocation Current checkpoint
     * @return Next checkpoint
     */
    public Location getNextCheckpoint(Location previousCheckpointLocation) {
        return checkpoints.get(checkpoints.indexOf(previousCheckpointLocation) + 1);
    }

    public Location getPreviousCheckpoint(Location checkpointLocation) {
        return checkpoints.get(getCheckpointNumber(checkpointLocation) - 2);
    }

    /**
     * Returns the previous checkpoints in the Parkour Course based on the provided checkpoint
     * @param checkpointLocation Current checkpoint the player is on
     * @return List of previous parkour checkpoints
     */
    public List<Location> getPreviousCheckpoints(Location checkpointLocation) {

        List<Location> previousCheckpoints = new ArrayList<>();

        for (Location location : checkpoints) {
            if (location.equals(checkpointLocation)) {
                return previousCheckpoints;
            } else {
                previousCheckpoints.add(location);
            }

        }

        return previousCheckpoints;
    }

    /**
     * Gets the checkpoint number for the specified checkpoint
     * @param checkpointLocation Checkpoint
     * @return Number of the checkpoint
     */
    public int getCheckpointNumber(Location checkpointLocation) {

        for (int i = 0; i < getCheckpointsAmount(); i++) {

            Location checkpoint = checkpoints.get(i);

            if (checkpoint.equals(checkpointLocation)) {
                return i + 1;
            }

        }

        return 0;
    }

    /**
     * Adds a checkpoint to the Parkour Course
     * @param checkpointLocation Location of the checkpoint
     */
    public void addCheckpoint(Location checkpointLocation) {
        checkpoints.add(checkpointLocation);
    }

    /**
     * Removes a checkpoint from the Parkour Course
     * @param checkpointNumber Number of the checkpoint
     */
    public void removeCheckpoint(int checkpointNumber) {
        checkpoints.remove(checkpointNumber);
    }

    /**
     * Returns the amount of checkpoints the Parkour Course has
     * @return Amount of checkpoints
     */
    public int getCheckpointsAmount() {
        return checkpoints.size();
    }

}
