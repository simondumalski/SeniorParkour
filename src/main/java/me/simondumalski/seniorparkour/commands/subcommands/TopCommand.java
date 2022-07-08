package me.simondumalski.seniorparkour.commands.subcommands;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.messaging.MessageManager;
import me.simondumalski.seniorparkour.messaging.Message;
import me.simondumalski.seniorparkour.parkour.Parkour;
import me.simondumalski.seniorparkour.commands.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TopCommand extends SubCommand {

    /**
     * Constructor for SubCommands
     * @param plugin Instance of the main plugin class
     */
    public TopCommand(Main plugin) {
        super(plugin);
    }

    @Override
    public String getCommand() {
        return "top";
    }

    @Override
    public String getUsage() {
        return "/parkour top <name>";
    }

    @Override
    public String getPermission() {
        return "parkour.top";
    }

    @Override
    public List<String> tabComplete(String[] args) {

        //  /parkour top <name>
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], plugin.getParkourManager().getParkourNames(), new ArrayList<>());
        }

        return null;
    }

    @Override
    public void perform(Player player, String[] args) {

        //Get the parkour course name
        String courseName = args[1];

        //Get the parkour course
        Parkour parkour = plugin.getParkourManager().getParkour(courseName);

        //Check if the parkour course exists
        if (parkour == null) {
            MessageManager.message(player, Message.ERROR_INVALID_PARKOUR, new String[]{courseName});
            return;
        }

        //Get the statistics for the parkour
        HashMap<UUID, Integer> data = plugin.getDatabaseManager().getParkourStats(parkour);

        //Check if the HashMap is empty
        if (data.isEmpty()) {
            MessageManager.message(player, Message.ERROR_NO_PARKOUR_DATA_TO_SHOW, null);
            return;
        }

        //Open the inventory
        plugin.getInventoryManager().openTopInventory(player, parkour, data);

    }

}
