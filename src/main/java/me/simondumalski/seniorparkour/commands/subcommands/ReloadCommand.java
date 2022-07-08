package me.simondumalski.seniorparkour.commands.subcommands;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.messaging.MessageManager;
import me.simondumalski.seniorparkour.messaging.Message;
import me.simondumalski.seniorparkour.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadCommand extends SubCommand {

    /**
     * Constructor for SubCommands
     * @param plugin Instance of the main plugin class
     */
    public ReloadCommand(Main plugin) {
        super(plugin);
    }

    @Override
    public String getCommand() {
        return "reload";
    }

    @Override
    public String getUsage() {
        return "/parkour reload";
    }

    @Override
    public String getPermission() {
        return "parkour.admin";
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }

    @Override
    public void perform(Player player, String[] args) {

        plugin.reloadConfig();
        MessageManager.message(player, Message.SUCCESS_CONFIG_RELOADED, null);

    }

}
