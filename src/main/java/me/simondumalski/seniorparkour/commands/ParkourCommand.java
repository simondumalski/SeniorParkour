package me.simondumalski.seniorparkour.commands;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.commands.subcommands.*;
import me.simondumalski.seniorparkour.messaging.MessageManager;
import me.simondumalski.seniorparkour.messaging.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ParkourCommand implements TabExecutor {

    private final Main plugin;
    private List<SubCommand> subCommands = new ArrayList<>();

    /**
     * Constructor for the ParkourCommand
     * @param plugin Instance of the main plugin class
     */
    public ParkourCommand(Main plugin) {

        this.plugin = plugin;

        subCommands.add(new CheckpointCommand(this.plugin));
        subCommands.add(new CreateCommand(this.plugin));
        subCommands.add(new DeleteCommand(this.plugin));
        subCommands.add(new EndCommand(this.plugin));
        subCommands.add(new InfoCommand(this.plugin));
        subCommands.add(new ReloadCommand(this.plugin));
        subCommands.add(new StatsCommand(this.plugin));
        subCommands.add(new TeleportCommand(this.plugin));
        subCommands.add(new TopCommand(this.plugin));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            //Check if the player is sending any arguments or the help command
            if (args == null || args.length < 1 || args[0].equalsIgnoreCase("help")) {
                MessageManager.help(player, subCommands);
                return true;
            }

            //Find a matching SubCommand
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getCommand().equalsIgnoreCase(args[0])) {
                    if (subCommand.getPermission() == null) {
                        subCommand.perform(player, args);
                    } else if (player.hasPermission(subCommand.getPermission())) {
                        subCommand.perform(player, args);
                    } else {
                        MessageManager.message(player, Message.ERROR_INSUFFICIENT_PERMISSIONS, null);
                    }
                    return true;
                }
            }

            //Otherwise, send the unknown command message
            MessageManager.message(player, Message.ERROR_UNKNOWN_COMMAND, null);

        } else {
            MessageManager.message(sender, Message.ERROR_PLAYERS_ONLY, null);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        //  /parkour <subcommand>
        if (args.length == 1) {

            List<String> arguments = new ArrayList<>();

            for (SubCommand subCommand : subCommands) {
                arguments.add(subCommand.getCommand());
            }

            return StringUtil.copyPartialMatches(args[0], arguments, new ArrayList<>());
        }

        //  /parkour <subcommand> <subcommand parameters>
        if (args.length > 1) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getCommand().equalsIgnoreCase(args[0])) {
                    return subCommand.tabComplete(args);
                }
            }
        }

        return null;
    }

}
