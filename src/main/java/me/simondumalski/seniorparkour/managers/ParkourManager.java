package me.simondumalski.seniorparkour.managers;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.utils.Parkour;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParkourManager {

    private final Main plugin = Main.getInstance();
    private final List<Parkour> parkourCourses = new ArrayList<>();

    /**
     * Returns the list of Parkour Courses
     * @return List of Parkour Courses
     */
    public List<Parkour> getParkourCourses() {
        return parkourCourses;
    }

    /**
     * Creates a Parkour Course with the provided name and location
     * @param courseName Parkour Course name
     * @param startLocation Parkour Course start location
     */
    public void createParkour(String courseName, Location startLocation) {

        //Create the parkour course
        parkourCourses.add(new Parkour(courseName, startLocation));

        //Get the text for the hologram
        List<String> unchangedText = plugin.getConfig().getStringList("holograms.start");

        if (unchangedText.isEmpty()) {
            unchangedText.add("&a&l%parkour% &e&lStart");
            unchangedText.add("&6Click to start!");
            //TODO: log to console that it was not set
        }

        //Replace the placeholders in the hologram text
        List<String> text = new ArrayList<>();

        for (String line : unchangedText) {

            //%parkour% - Parkour course name
            if (line.contains("%parkour%")) {
                line = line.replace("%parkour%", courseName);
            }

            //Add the modified line to the list of hologram lines
            text.add(line);

        }

        //Create the Hologram
        plugin.getHologramManager().createHologram(startLocation, text);

    }

    /**
     * Deletes a Parkour Course
     * @param parkour Parkour Course to delete
     */
    public void deleteParkour(Parkour parkour) {

        //Delete the Holograms
        plugin.getHologramManager().deleteHologram(parkour.getStartLocation());

        if (parkour.getEndLocation() != null) {
            plugin.getHologramManager().deleteHologram(parkour.getEndLocation());
        }

        for (Location location : parkour.getCheckpoints()) {
            plugin.getHologramManager().deleteHologram(location);
        }

        //Delete the parkour course
        parkourCourses.remove(parkour);

    }

    /**
     * Loads a parkour course, creating the object based on the provided parameters
     * @param courseName Name of the course
     * @param startLocation Starting location of the course
     * @param endLocation Ending location of the course
     * @param checkpoints Locations of the checkpoints in the course
     */
    public void loadParkour(String courseName, Location startLocation, Location endLocation, List<Location> checkpoints) {
        parkourCourses.add(new Parkour(courseName, startLocation, endLocation, checkpoints));
    }

    /**
     * Adds a checkpoint to the specified Parkour Course
     * @param parkour Parkour course to add a checkpoint to
     * @param checkpointLocation Location of the checkpoint
     */
    public void addCheckpoint(Parkour parkour, Location checkpointLocation) {

        //Add the checkpoint to the Parkour Course
        parkour.addCheckpoint(checkpointLocation);

        //Get the checkpoint hologram text
        List<String> unchangedText = plugin.getConfig().getStringList("holograms.checkpoint");

        if (unchangedText.isEmpty()) {
            unchangedText.add("&a&l%parkour% &e&lCheckpoint #%checkpoint%");
            unchangedText.add("&6Click to pass!");
            //TODO: log to console that it was not set
        }

        //Get a list ready for storing the changed text
        List<String> text = new ArrayList<>();

        for (String line : unchangedText) {

            //%parkour% - Parkour course name
            if (line.contains("%parkour%")) {
                line = line.replace("%parkour%", parkour.getCourseName());
            }

            //%checkpoint% - Checkpoint number
            if (line.contains("%checkpoint%")) {
                line = line.replace("%checkpoint%", Integer.toString(parkour.getCheckpointsAmount()));
            }

            //Add the line to the changed text list
            text.add(line);

        }

        //Create the hologram
        plugin.getHologramManager().createHologram(checkpointLocation, text);

    }

    /**
     * Removes a checkpoint from the specified Parkour Course
     * @param parkour Parkour Course to remove a checkpoint from
     * @param checkpointLocation Location of the checkpoint
     * @param checkpointIndex Index of the checkpoint in the List
     */
    public void removeCheckpoint(Parkour parkour, Location checkpointLocation, int checkpointIndex) {

        //Remove the checkpoint from the parkour course
        parkour.removeCheckpoint(checkpointIndex);

        //Delete the checkpoint's hologram
        plugin.getHologramManager().deleteHologram(checkpointLocation);

        //Get the checkpoint hologram text
        List<String> unchangedText = plugin.getConfig().getStringList("holograms.checkpoint");

        if (unchangedText.isEmpty()) {
            unchangedText.add("&a&l%parkour% &e&lCheckpoint #%checkpoint%");
            unchangedText.add("&6Click to pass!");
            //TODO: log to console that it was not set
        }

        //Delete the old checkpoint holograms
        for (Location checkpoint : parkour.getCheckpoints()) {
            plugin.getHologramManager().deleteHologram(checkpoint);
        }

        //Create the updated checkpoint holograms
        for (int i = 0; i < parkour.getCheckpointsAmount(); i++) {

            //Get the checkpoint location
            Location checkpoint = parkour.getCheckpoint(i);

            //Get a list ready for storing the changed hologram text
            List<String> text = new ArrayList<>();

            //Replace the placeholders
            for (String line : unchangedText) {

                //%parkour% - Parkour course name
                if (line.contains("%parkour%")) {
                    line = line.replace("%parkour%", parkour.getCourseName());
                }

                //%checkpoint% - Checkpoint number
                if (line.contains("%checkpoint%")) {
                    line = line.replace("%checkpoint%", Integer.toString(i + 1));
                }

                //Add the line to the changed text list
                text.add(line);

            }

            //Create the hologram
            plugin.getHologramManager().createHologram(checkpoint.add(0, 0.6, 0), text);

        }

    }

    public void setEndLocation(Parkour parkour, Location endLocation) {

        //If an end location is already set, remove the hologram
        if (parkour.getEndLocation() != null) {
            plugin.getHologramManager().deleteHologram(parkour.getEndLocation());
        }

        //Set the end location for the parkour course
        parkour.setEndLocation(endLocation);

        //Get the text for the end point hologram
        List<String> unchangedText = plugin.getConfig().getStringList("holograms.end");

        if (unchangedText.isEmpty()) {
            unchangedText.add("&a&l%parkour% &e&lEnd");
            unchangedText.add("&6Click to end!");
            //TODO: log to console that it was not set
        }

        //Replace the placeholders in the hologram text
        List<String> text = new ArrayList<>();

        for (String line : unchangedText) {

            //%parkour% - Parkour course name
            if (line.contains("%parkour%")) {
                line = line.replace("%parkour%", parkour.getCourseName());
            }

            //Add the modified line to the list of hologram lines
            text.add(line);

        }

        //Create the hologram
        plugin.getHologramManager().createHologram(endLocation, text);

    }

    /**
     * Returns the Parkour Course matching the provided course name
     * @param courseName Name of the Parkour Course
     * @return Parkour, null if none was found
     */
    public Parkour getParkour(String courseName) {

        //Find the parkour course matching the provided course name
        for (Parkour parkour : parkourCourses) {
            if (parkour.getCourseName().equalsIgnoreCase(courseName)) {
                return parkour;
            }
        }

        //Otherwise, return null
        return null;
    }

    /**
     * Returns true/false if the specified Parkour Course exists
     * @param courseName Parkour Course name
     * @return True/false
     */
    public boolean parkourExists(String courseName) {

        for (Parkour parkour : parkourCourses) {
            if (parkour.getCourseName().equalsIgnoreCase(courseName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the list of Parkour Course names
     * @return Parkour Course names
     */
    public List<String> getParkourNames() {

        List<String> courseNames = new ArrayList<>();

        for (Parkour parkour : parkourCourses) {
            courseNames.add(parkour.getCourseName());
        }

        return courseNames;
    }

}
