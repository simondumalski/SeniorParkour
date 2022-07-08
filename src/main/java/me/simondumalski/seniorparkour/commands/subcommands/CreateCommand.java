package me.simondumalski.seniorparkour.commands.subcommands;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.messaging.MessageManager;
import me.simondumalski.seniorparkour.messaging.Message;
import me.simondumalski.seniorparkour.commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateCommand extends SubCommand {

    /**
     * Constructor for SubCommands
     * @param plugin Instance of the main plugin class
     */
    public CreateCommand(Main plugin) {
        super(plugin);
    }

    @Override
    public String getCommand() {
        return "create";
    }

    @Override
    public String getUsage() {
        return "/parkour create <name>";
    }

    @Override
    public String getPermission() {
        return "parkour.admin.create";
    }

    @Override
    public List<String> tabComplete(String[] args) {
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

        //Check if that name has already been taken
        if (plugin.getParkourManager().parkourExists(courseName)) {
            MessageManager.message(player, Message.ERROR_PARKOUR_NAME_TAKEN, new String[]{courseName});
            return;
        }

        //Create the parkour course
        plugin.getParkourManager().createParkour(courseName, player.getLocation());
        MessageManager.message(player, Message.SUCCESS_PARKOUR_CREATED, new String[]{courseName});

    }

}
