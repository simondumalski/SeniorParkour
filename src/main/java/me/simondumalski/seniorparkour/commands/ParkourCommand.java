package me.simondumalski.seniorparkour.commands;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.commands.subcommands.*;
import me.simondumalski.seniorparkour.managers.MessageManager;
import me.simondumalski.seniorparkour.utils.Message;
import me.simondumalski.seniorparkour.utils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ParkourCommand implements TabExecutor {

    private final Main plugin = Main.getInstance();
    private List<SubCommand> subCommands = new ArrayList<>();

    public ParkourCommand() {

        subCommands.add(new CheckpointCommand());
        subCommands.add(new CreateCommand());
        subCommands.add(new DeleteCommand());
        subCommands.add(new EndCommand());
        subCommands.add(new InfoCommand());
        subCommands.add(new ReloadCommand());
        subCommands.add(new StatsCommand());
        subCommands.add(new TeleportCommand());
        subCommands.add(new TopCommand());

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
