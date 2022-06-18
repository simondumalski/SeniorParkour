package me.simondumalski.seniorparkour.tasks;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.managers.MessageManager;
import me.simondumalski.seniorparkour.utils.Message;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IncrementTimerTask implements Runnable {

    private final Main plugin = Main.getInstance();

    @Override
    public void run() {

        //Get the list of players who are currently in a Parkour Trial
        List<Player> playersInParkour = new ArrayList<>();

        for (UUID uuid : plugin.getTrialManager().getPlayersInParkour()) {

            //Get the player object
            Player player = plugin.getServer().getPlayer(uuid);

            //Check if the player is valid
            if (player == null) {
                continue;
            }

            //Add the player to the list of players in parkour
            playersInParkour.add(player);

        }

        int timeout = plugin.getConfig().getInt("parkour.timeout");

        if (timeout == 0) {
            timeout = 60;
        }

        //Increment the players time in the Parkour Trial by 1 second
        for (Player player : playersInParkour) {

            int currentTime = plugin.getTrialManager().getTimeInParkour(player);

            if (currentTime >= timeout) {
                plugin.getTrialManager().forceEnd(player);
                MessageManager.message(player, Message.ERROR_PARKOUR_TIMEOUT, null);
            } else {
                plugin.getTrialManager().incrementTime(player);
            }

        }

    }

}
