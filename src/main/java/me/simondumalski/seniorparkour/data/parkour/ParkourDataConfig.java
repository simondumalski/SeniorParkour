package me.simondumalski.seniorparkour.data.parkour;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.messaging.MessageManager;
import me.simondumalski.seniorparkour.messaging.Log;
import me.simondumalski.seniorparkour.parkour.Parkour;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class ParkourDataConfig {

    private final Main plugin;

    private final String FILE_NAME = "parkour-data.yml";
    private File dataFile;
    private YamlConfiguration dataConfig;

    /**
     * Constructor for the ParkourDataConfig
     * @param plugin Instance of the main plugin class
     */
    public ParkourDataConfig(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Saves the Parkour Courses to file
     */
    public void saveData() {

        try {

            MessageManager.log(Log.DATA_SAVING_PARKOURS, Level.INFO, null);

            int savedParkours = 0;

            //Save each parkour course to file
            for (Parkour parkour : plugin.getParkourManager().getParkourCourses()) {

                //Get the course information
                String courseName = parkour.getCourseName();
                Location startLocation = parkour.getStartLocation();
                Location endLocation = parkour.getEndLocation();
                List<Location> checkpoints = parkour.getCheckpoints();

                //Check if the end location is null, and don't save the parkour if it is
                if (endLocation == null) {
                    MessageManager.log(Log.DATA_NO_END_LOCATION, Level.WARNING, new String[]{courseName});
                    continue;
                }

                //Create the section in the data file
                dataConfig.createSection("data." + courseName);

                //Get the newly created section
                ConfigurationSection section = dataConfig.getConfigurationSection("data." + courseName);

                if (section == null) {
                    return;
                }

                //Save the start and end locations
                section.set("start-location", startLocation);
                section.set("end-location", endLocation);

                //Save each checkpoint location
                for (int i = 1; i < checkpoints.size() + 1; i++) {
                    Location location = checkpoints.get(i - 1);
                    section.set("checkpoints." + i, location);
                }

                //Save the start holograms
                List<ArmorStand> startHolograms = plugin.getHologramManager().getHologram(startLocation);

                for (int i = 0; i < startHolograms.size(); i++) {

                    //Get the hologram
                    ArmorStand hologram = startHolograms.get(i);

                    //Get the hologram text
                    String text = hologram.getCustomName();

                    //Save the hologram
                    section.set("start-holograms." + (i + 1), text);

                }

                //Save the end holograms
                List<ArmorStand> endHolograms = plugin.getHologramManager().getHologram(endLocation);

                for (int i = 0; i < endHolograms.size(); i++) {

                    //Get the hologram
                    ArmorStand hologram = endHolograms.get(i);

                    //Get the hologram text
                    String text = hologram.getCustomName();

                    //Save the hologram
                    section.set("end-holograms." + (i + 1), text);

                }

                //Save the checkpoint holograms
                for (int i = 1; i < checkpoints.size() + 1; i++) {

                    //Get the location of the checkpoint
                    Location checkpointLocation = checkpoints.get(i - 1);

                    List<ArmorStand> checkpointHolograms = plugin.getHologramManager().getHologram(checkpointLocation);

                    for (int k = 0; k < checkpointHolograms.size(); k++) {

                        //Get the hologram
                        ArmorStand hologram = checkpointHolograms.get(k);

                        //Get the hologram text
                        String text = hologram.getCustomName();

                        //Save the hologram
                        section.set("checkpoint-" + i + "-holograms." + (k + 1), text);

                    }

                }

                savedParkours++;

            }

            dataConfig.save(dataFile);
            plugin.getHologramManager().removeAllHolograms();
            MessageManager.log(Log.DATA_SAVED_PARKOURS, Level.INFO, new String[]{Integer.toString(savedParkours)});

        } catch (Exception ex) {
            ex.printStackTrace();
            MessageManager.log(Log.DATA_ERROR_SAVING_PARKOURS, Level.SEVERE, null);
        }

    }

    /**
     * Loads the saved Parkour Courses from file
     */
    public void loadData() {

        try {

            MessageManager.log(Log.DATA_LOADING_PARKOURS, Level.INFO, null);

            if (!dataConfig.isConfigurationSection("data")) {
                dataConfig.createSection("data");
                MessageManager.log(Log.DATA_NO_PARKOURS, Level.INFO, null);
                return;
            }

            int loadedParkours = 0;

            for (String key : dataConfig.getConfigurationSection("data").getKeys(false)) {

                ConfigurationSection section = dataConfig.getConfigurationSection("data." + key);

                //Load the start location
                Location startLocation = section.getLocation("start-location");

                //Load the end location
                Location endLocation = section.getLocation("end-location");

                //Load the checkpoints
                List<Location> checkpoints = new ArrayList<>();

                for (int i = 1; i < section.getConfigurationSection("checkpoints").getKeys(false).size() + 1; i++) {
                    checkpoints.add(section.getLocation("checkpoints." + i));
                }

                //Create the parkour course
                plugin.getParkourManager().loadParkour(key, startLocation, endLocation, checkpoints);

                //Load the start holograms
                List<String> startHologramLines = new ArrayList<>();

                for (String startHologramKey : section.getConfigurationSection("start-holograms").getKeys(false)) {
                    startHologramLines.add(section.getString("start-holograms." + startHologramKey));
                }

                plugin.getHologramManager().createHologram(startLocation.add(0, 0.6, 0), startHologramLines);

                //Load the end holograms
                List<String> endHologramLines = new ArrayList<>();

                for (String endHologramKey : section.getConfigurationSection("end-holograms").getKeys(false)) {
                    endHologramLines.add(section.getString("end-holograms." + endHologramKey));
                }

                plugin.getHologramManager().createHologram(endLocation.add(0, 0.6, 0), endHologramLines);

                //Load the checkpoint holograms
                for (int i = 1; i < checkpoints.size() + 1; i++) {

                    Location checkpointLocation = checkpoints.get(i - 1);
                    List<String> checkpointHologramLines = new ArrayList<>();

                    for (int k = 0; k < section.getConfigurationSection("checkpoint-" + i + "-holograms").getKeys(false).size(); k++) {
                        checkpointHologramLines.add(section.getString("checkpoint-" + i + "-holograms." + (k + 1)));
                    }

                    plugin.getHologramManager().createHologram(checkpointLocation.add(0, 0.6, 0), checkpointHologramLines);

                }

                loadedParkours++;

            }

            MessageManager.log(Log.DATA_LOADED_PARKOURS, Level.INFO, new String[]{Integer.toString(loadedParkours)});

        } catch (Exception ex) {
            ex.printStackTrace();
            MessageManager.log(Log.DATA_ERROR_LOADING_PARKOURS, Level.SEVERE, null);
        }

    }

    /**
     * Initializes the parkour-data.yml file for storing parkour data
     */
    public void initialize() {

        try {

            MessageManager.log(Log.DATA_INITIALIZING_FILE, Level.INFO, null);

            File file = new File(plugin.getDataFolder(), FILE_NAME);

            if (file.createNewFile()) {
                MessageManager.log(Log.DATA_CREATING_FILE, Level.INFO, null);
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            this.dataFile = file;
            this.dataConfig = config;

            MessageManager.log(Log.DATA_INITIALIZED_FILE, Level.INFO, null);

        } catch (Exception ex) {
            ex.printStackTrace();
            MessageManager.log(Log.DATA_ERROR_INITIALIZING_FILE, Level.SEVERE, null);
        }

    }

}
