package me.simondumalski.seniorparkour.utils;

import me.simondumalski.seniorparkour.Main;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public abstract class SubCommand {

    /**
     * Instance of the Main plugin class.
     * Used for accessing the plugin managers.
     */
    public final Main plugin = Main.getInstance();

    /**
     * Returns the command as a string
     * @return Command
     */
    public abstract String getCommand();

    /**
     * Returns the description of the command
     * @return Command description
     */
    public String getDescription() {
        return Objects.requireNonNullElse(plugin.getConfig().getString("messages.commands." + getCommand()), "DESCRIPTION NOT SET");
    }

    /**
     * Returns the usage of the command
     * @return Command usage
     */
    public abstract String getUsage();

    /**
     * Returns the permission node required to use the command
     * @return Permission node
     */
    public abstract String getPermission();

    /**
     * Returns the tab complete arguments
     * @return Tab complete arguments
     */
    public abstract List<String> tabComplete(String[] args);

    /**
     * Performs the command logic
     */
    public abstract void perform(Player player, String[] args);

}
