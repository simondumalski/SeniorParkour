package me.simondumalski.seniorparkour.commands.subcommands;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.messaging.MessageManager;
import me.simondumalski.seniorparkour.messaging.Message;
import me.simondumalski.seniorparkour.parkour.Parkour;
import me.simondumalski.seniorparkour.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CheckpointCommand extends SubCommand {

    /**
     * Constructor for SubCommands
     * @param plugin Instance of the main plugin class
     */
    public CheckpointCommand(Main plugin) {
        super(plugin);
    }

    @Override
    public String getCommand() {
        return "checkpoint";
    }

    @Override
    public String getUsage() {
        return "/parkour checkpoint <name>";
    }

    @Override
    public String getPermission() {
        return "parkour.admin.checkpoint";
    }

    @Override
    public List<String> tabComplete(String[] args) {

        //  /parkour checkpoint <name>
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

        //Get the player's location
        Location checkpointLocation = player.getLocation();

        //Create the checkpoint
        plugin.getParkourManager().addCheckpoint(parkour, checkpointLocation);
        MessageManager.message(player, Message.SUCCESS_CHECKPOINT_CREATED, new String[]{Integer.toString(parkour.getCheckpointsAmount()), courseName});

    }

}
