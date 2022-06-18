package me.simondumalski.seniorparkour.commands.subcommands;

import me.simondumalski.seniorparkour.managers.MessageManager;
import me.simondumalski.seniorparkour.utils.Message;
import me.simondumalski.seniorparkour.utils.Parkour;
import me.simondumalski.seniorparkour.utils.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends SubCommand {

    @Override
    public String getCommand() {
        return "info";
    }

    @Override
    public String getUsage() {
        return "/parkour info <name>";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public List<String> tabComplete(String[] args) {

        //  /parkour delete <name>
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], plugin.getParkourManager().getParkourNames(), new ArrayList<>());
        }

        return null;
    }

    @Override
    public void perform(Player player, String[] args) {

        //Check if the player specified a parkour course name
        if (args.length < 2) {
            MessageManager.message(player, Message.ERROR_INVALID_USAGE, new String[]{getUsage()});
            return;
        }

        //Get the parkour course name
        String courseName = args[1];

        //Get the parkour course
        Parkour parkour = plugin.getParkourManager().getParkour(courseName);

        //Check if the parkour course exists
        if (parkour == null) {
            MessageManager.message(player, Message.ERROR_INVALID_PARKOUR, new String[]{courseName});
            return;
        }

        //Open the inventory for the player
        plugin.getInventoryManager().openInfoInventory(player, parkour);

    }

}
