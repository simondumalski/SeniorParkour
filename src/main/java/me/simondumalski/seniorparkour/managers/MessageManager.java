package me.simondumalski.seniorparkour.managers;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.utils.Log;
import me.simondumalski.seniorparkour.utils.Message;
import me.simondumalski.seniorparkour.utils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.logging.Level;

public class MessageManager {

    private static final Main plugin = Main.getInstance();

    /**
     * Sends a message from the config.yml to the provided CommandSender
     * @param sender Person to send the message to
     * @param configValue Config value of the message to send
     * @param args Arguments, send null if none
     */
    public static void message(CommandSender sender, Message configValue, String[] args) {

        //Get the message from the config.yml
        String message = plugin.getConfig().getString(configValue.getConfigValue());

        //Check that the message is set
        if (message == null) {
            sender.sendMessage(configValue.getConfigValue());
            //TODO: log to console that it messed up
            return;
        }

        //Replace the placeholders

        //%prefix%
        if (message.contains("%prefix%")) {

            //Get the prefix from the config.yml
            String prefix = plugin.getConfig().getString(Message.PLUGIN_PREFIX.getConfigValue());

            //Check that the prefix is set
            if (prefix == null) {
                prefix = "&8[&bSenior&fParkour&8]";
            }

            //Replace the placeholder
            message = message.replace("%prefix%", prefix);
        }

        //%args#%
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String argsIndex = "%args" + i + "%";
                if (message.contains(argsIndex)) {
                    message = message.replace(argsIndex, args[i]);
                }
            }
        }

        //Send the message
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

    }

    /**
     * Logs a message to console
     * @param logMessage Message to log
     * @param level Severity level of the message
     * @param args Arguments, send null if none
     */
    public static void log(Log logMessage, Level level, String[] args) {

        //Get the message as a string
        String message = logMessage.getMessage();

        //%args#%
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String argsIndex = "%args" + i + "%";
                if (message.contains(argsIndex)) {
                    message = message.replace(argsIndex, args[i]);
                }
            }
        }

        //Log the message to console
        plugin.getLogger().log(level, ChatColor.translateAlternateColorCodes('&', message));

    }

    /**
     * Sends the help message to the provided CommandSender
     * @param sender Person to send the message to
     * @param subCommands List of SubCommands
     */
    public static void help(CommandSender sender, List<SubCommand> subCommands) {

        //Get the help menu values from the config.yml
        String header = plugin.getConfig().getString("messages.help.header");
        String footer = plugin.getConfig().getString("messages.help.footer");
        String command = plugin.getConfig().getString("messages.help.command");
        String separator = plugin.getConfig().getString("messages.help.separator");
        String description = plugin.getConfig().getString("messages.help.description");

        //Check that the values are valid
        if (header == null) {
            header = "&8---------[ &bSenior&fParkour &8]---------";
        }

        if (footer == null) {
            footer = "&8--------------------------------";
        }

        if (command == null) {
            command = "&e";
        }

        if (separator == null) {
            separator = " &8- ";
        }

        if (description == null) {
            description = "&f";
        }

        //Send the header to the CommandSender
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', header));

        //For each SubCommand in the list
        for (SubCommand subCommand : subCommands) {
            if (sender.hasPermission(subCommand.getPermission())) {
                String message = command + subCommand.getUsage() + separator + description + subCommand.getDescription();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        }

        //Send the footer to the CommandSender
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', footer));

    }

}
