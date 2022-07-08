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

public class TeleportCommand extends SubCommand {

    /**
     * Constructor for SubCommands
     * @param plugin Instance of the main plugin class
     */
    public TeleportCommand(Main plugin) {
        super(plugin);
    }

    @Override
    public String getCommand() {
        return "teleport";
    }

    @Override
    public String getUsage() {
        return "/parkour teleport <name> [checkpoint]";
    }

    @Override
    public String getPermission() {
        return "parkour.admin.teleport";
    }

    @Override
    public List<String> tabComplete(String[] args) {

        //  /parkour teleport <name>
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], plugin.getParkourManager().getParkourNames(), new ArrayList<>());
        }

        //  /parkour teleport <name> [checkpoint]
        if (args.length == 3) {

            //Get the parkour course name
            String courseName = args[1];

            //Get the parkour course
            Parkour parkour = plugin.getParkourManager().getParkour(courseName);

            //Check if the parkour course exists
            if (parkour == null) {
                return null;
            }

            List<String> checkpoints = new ArrayList<>();

            for (int i = 1; i <= parkour.getCheckpointsAmount(); i++) {
                checkpoints.add(Integer.toString(i));
            }

            return StringUtil.copyPartialMatches(args[2], checkpoints, new ArrayList<>());
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

        //Check if the player specified a checkpoint
        //If they didn't specify one, teleport them to the parkour start location
        if (args.length < 3) {
            player.teleport(parkour.getStartLocation().add(0, 0.6, 0));
            parkour.getStartLocation().subtract(0, 0.6, 0);
            MessageManager.message(player, Message.SUCCESS_PARKOUR_TELEPORTED, new String[]{parkour.getCourseName()});
            return;
        }

        //Get the checkpoint
        int checkpointNumber = 0;

        try {
            checkpointNumber = Integer.parseInt(args[2]);
        } catch (NumberFormatException ex) {
            MessageManager.message(player, Message.ERROR_INVALID_CHECKPOINT, null);
            return;
        }

        //Check if the checkpoint is valid
        if (checkpointNumber > parkour.getCheckpointsAmount()) {
            MessageManager.message(player, Message.ERROR_INVALID_CHECKPOINT, null);
            return;
        }

        //Get the checkpoint location
        Location checkpointLocation = parkour.getCheckpoint(checkpointNumber - 1);

        //Teleport the player to the checkpoint
        player.teleport(checkpointLocation.add(0, 0.6, 0));
        checkpointLocation.subtract(0, 0.6, 0);
        MessageManager.message(player, Message.SUCCESS_CHECKPOINT_TELEPORTED, new String[]{Integer.toString(checkpointNumber), parkour.getCourseName()});

    }

}
