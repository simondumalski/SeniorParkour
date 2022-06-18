package me.simondumalski.seniorparkour.commands.subcommands;

import me.simondumalski.seniorparkour.managers.MessageManager;
import me.simondumalski.seniorparkour.utils.Message;
import me.simondumalski.seniorparkour.utils.Parkour;
import me.simondumalski.seniorparkour.utils.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DeleteCommand extends SubCommand {

    @Override
    public String getCommand() {
        return "delete";
    }

    @Override
    public String getUsage() {
        return "/parkour delete <name> [checkpoint]";
    }

    @Override
    public String getPermission() {
        return "parkour.admin.delete";
    }

    @Override
    public List<String> tabComplete(String[] args) {

        //  /parkour delete <name>
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], plugin.getParkourManager().getParkourNames(), new ArrayList<>());
        }

        //  /parkour delete <name> [checkpoint]
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
        //If they didn't specify one, delete the whole parkour course
        if (args.length < 3) {
            plugin.getDatabaseManager().removeData(parkour);
            plugin.getParkourManager().deleteParkour(parkour);
            MessageManager.message(player, Message.SUCCESS_PARKOUR_DELETED, new String[]{courseName});
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

        //Delete the checkpoint
        plugin.getParkourManager().removeCheckpoint(parkour, checkpointLocation, checkpointNumber - 1);
        MessageManager.message(player, Message.SUCCESS_CHECKPOINT_DELETED, new String[]{Integer.toString(checkpointNumber), parkour.getCourseName()});

    }

}
