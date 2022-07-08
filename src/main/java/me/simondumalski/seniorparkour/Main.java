package me.simondumalski.seniorparkour;

import me.simondumalski.seniorparkour.commands.ParkourCommand;
import me.simondumalski.seniorparkour.data.parkour.ParkourDataConfig;
import me.simondumalski.seniorparkour.data.database.DatabaseManager;
import me.simondumalski.seniorparkour.holograms.HologramManager;
import me.simondumalski.seniorparkour.menus.GUIInteractListener;
import me.simondumalski.seniorparkour.holograms.HologramInteractListener;
import me.simondumalski.seniorparkour.listeners.PlayerQuitListener;
import me.simondumalski.seniorparkour.menus.InventoryManager;
import me.simondumalski.seniorparkour.messaging.MessageManager;
import me.simondumalski.seniorparkour.parkour.ParkourManager;
import me.simondumalski.seniorparkour.parkour.TrialManager;
import me.simondumalski.seniorparkour.parkour.tasks.IncrementTimerTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private ParkourManager parkourManager;
    private HologramManager hologramManager;
    private TrialManager trialManager;
    private InventoryManager inventoryManager;
    private ParkourDataConfig parkourDataConfig;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {

        //Initialize the config.yml
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();

        //Initialize the MessageManager
        MessageManager.setPlugin(this);

        //Initialize the plugin managers
        parkourManager = new ParkourManager(this);
        hologramManager = new HologramManager(this);
        trialManager = new TrialManager(this);
        inventoryManager = new InventoryManager(this);

        //Initialize and load the parkour-data.yml file
        parkourDataConfig = new ParkourDataConfig(this);
        parkourDataConfig.initialize();
        parkourDataConfig.loadData();

        //Initialize the DatabaseManager and connect to the database
        databaseManager = new DatabaseManager(this);
        databaseManager.connect();
        databaseManager.prepareDatabase();

        //Register the event listeners
        getServer().getPluginManager().registerEvents(new HologramInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIInteractListener(this), this);

        //Set the command executors
        getCommand("parkour").setExecutor(new ParkourCommand(this));

        //Schedule the tasks
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new IncrementTimerTask(this), 20L, 20L);

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
