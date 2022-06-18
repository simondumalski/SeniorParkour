package me.simondumalski.seniorparkour.listeners;

import me.simondumalski.seniorparkour.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        //Get the player who left
        Player player = e.getPlayer();

        //Check if the player was in a Parkour Trial
        //If they are, remove them from the trial
        if (plugin.getTrialManager().isPlayerInParkour(player)) {
            plugin.getTrialManager().forceEnd(player);
        }

    }

}
