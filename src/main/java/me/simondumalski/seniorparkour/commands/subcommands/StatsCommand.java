package me.simondumalski.seniorparkour.commands.subcommands;

import me.simondumalski.seniorparkour.managers.MessageManager;
import me.simondumalski.seniorparkour.utils.Message;
import me.simondumalski.seniorparkour.utils.Parkour;
import me.simondumalski.seniorparkour.utils.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatsCommand extends SubCommand {

    @Override
    public String getCommand() {
        return "stats";
    }

    @Override
    public String getUsage() {
        return "/parkour stats <player>";
    }

    @Override
    public String getPermission() {
        return "parkour.stats";
    }

    @Override
    public List<String> tabComplete(String[] args) {

        if (args.length == 2) {

            List<String> playerNames = new ArrayList<>();

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                playerNames.add(player.getName());
            }

            return StringUtil.copyPartialMatches(args[1], playerNames, new ArrayList<>());
        }

        return null;
    }

    @Override
    public void perform(Player player, String[] args) {

        //Check if a player was specified
        if (args.length < 2) {
            MessageManager.message(player, Message.ERROR_INVALID_PLAYER, null);
            return;
        }

        //Check if the target player is online
        Player target = plugin.getServer().getPlayer(args[1]);

        if (target == null) {
            MessageManager.message(player, Message.ERROR_INVALID_PLAYER, null);
            return;
        }

        HashMap<Parkour, Integer> data = plugin.getDatabaseManager().getPlayerStats(target);

        if (data.isEmpty()) {
            MessageManager.message(player, Message.ERROR_NO_PLAYER_DATA_TO_SHOW, null);
            return;
        }

        plugin.getInventoryManager().openStatsInventory(player, target, data);

    }

}
