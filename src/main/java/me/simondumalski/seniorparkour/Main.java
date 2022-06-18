package me.simondumalski.seniorparkour;

import me.simondumalski.seniorparkour.commands.ParkourCommand;
import me.simondumalski.seniorparkour.configs.ParkourDataConfig;
import me.simondumalski.seniorparkour.listeners.GUIInteractListener;
import me.simondumalski.seniorparkour.listeners.HologramInteractListener;
import me.simondumalski.seniorparkour.listeners.PlayerQuitListener;
import me.simondumalski.seniorparkour.managers.*;
import me.simondumalski.seniorparkour.tasks.IncrementTimerTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private ParkourManager parkourManager;
    private HologramManager hologramManager;
    private TrialManager trialManager;
    private InventoryManager inventoryManager;
    private ParkourDataConfig parkourDataConfig;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {

        //Initialize the main plugin class instance variable
        instance = this;

        //Initialize the plugin managers
        parkourManager = new ParkourManager();
        hologramManager = new HologramManager();
        trialManager = new TrialManager();
        inventoryManager = new InventoryManager();

        //Initialize the config.yml
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();

        //Initialize and load the parkour-data.yml file
        parkourDataConfig = new ParkourDataConfig();
        parkourDataConfig.initialize();
        parkourDataConfig.loadData();

        //Initialize the DatabaseManager and connect to the database
        databaseManager = new DatabaseManager();
        databaseManager.connect();
        databaseManager.prepareDatabase();

        //Register the event listeners
        getServer().getPluginManager().registerEvents(new HologramInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new GUIInteractListener(), this);

        //Set the command executors
        getCommand("parkour").setExecutor(new ParkourCommand());

        //Schedule the tasks
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new IncrementTimerTask(), 20L, 20L);

    }

    @Override
    public void onDisable() {

        //Save the parkours to file
        parkourDataConfig.saveData();

        //Save the player data to the MySQL database
        databaseManager.disconnect();

    }

    /**
     * Takes in an integer for the amount of seconds, and converts it to a string formatted into days, hours, minutes, seconds
     * @param seconds Seconds to convert
     * @return String of converted time
     */
    public String timeToString(int seconds) {

        if (seconds < 60) {
            return seconds + "s";
        }

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        if (minutes < 60) {
            return ((minutes < 10) ? "0" : "") + minutes + "m "
                    + ((remainingSeconds < 10) ? "0" : "") + remainingSeconds + "s";
        }
        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;
        if (hours < 24) {
            return ((hours < 10) ? "0" : "") + hours + "h "
                    + ((remainingMinutes < 10) ? "0" : "") + remainingMinutes + "m "
                    + ((remainingSeconds < 10) ? "0" : "") + remainingSeconds + "s";
        }
        int days = hours / 24;
        int remainingHours = hours % 24;
        return ((days < 10) ? "0" : "") + days + "d "
                + ((remainingHours < 10) ? "0" : "") + remainingHours + "h "
                + ((remainingMinutes < 10) ? "0" : "") + remainingMinutes + "m "
                + ((remainingSeconds < 10) ? "0" : "") + remainingSeconds + "s";

    }

    /**
     * Returns the instance of the Main plugin class
     * @return Main plugin class
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Returns the instance of the plugin's ParkourManager
     * @return ParkourManager instance
     */
    public ParkourManager getParkourManager() {
        return parkourManager;
    }

    /**
     * Returns the instance of the plugin's HologramManager
     * @return HologramManager instance
     */
    public HologramManager getHologramManager() {
        return hologramManager;
    }

    /**
     * Returns the instance of the plugin's TrialManager
     * @return TrialManager instance
     */
    public TrialManager getTrialManager() {
        return trialManager;
    }

    /**
     * Returns the instance of the plugin's InventoryManager
     * @return InventoryManager instance
     */
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    /**
     * Returns the instance of the plugin's DatabaseManager
     * @return DatabaseManager instance
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

}
