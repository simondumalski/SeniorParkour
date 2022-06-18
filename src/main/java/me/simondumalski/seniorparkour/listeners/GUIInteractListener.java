package me.simondumalski.seniorparkour.listeners;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.managers.MessageManager;
import me.simondumalski.seniorparkour.utils.Message;
import me.simondumalski.seniorparkour.utils.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIInteractListener implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        //Get the player who clicked
        Player player = (Player) e.getWhoClicked();

        //Determine if the inventory being clicked in is a Parkour GUI
        for (String courseName : plugin.getParkourManager().getParkourNames()) {

            if (e.getView().getTitle().contains(courseName) && e.getCurrentItem() != null) {

                Parkour parkour = plugin.getParkourManager().getParkour(courseName);

                e.setCancelled(true);

                int clickedSlot = e.getRawSlot();

                //Start item slot
                if (clickedSlot == 0) {
                    player.closeInventory();
                    player.teleport(parkour.getStartLocation().add(0, 0.6, 0));
                    parkour.getStartLocation().subtract(0, 0.6, 0);
                    MessageManager.message(player, Message.SUCCESS_PARKOUR_TELEPORTED, new String[]{parkour.getCourseName()});
                    return;
                }

                //End item slot
                if (clickedSlot == (parkour.getCheckpointsAmount() + 1)) {
                    player.closeInventory();
                    player.teleport(parkour.getEndLocation().add(0, 0.6, 0));
                    parkour.getEndLocation().subtract(0, 0.6, 0);
                    MessageManager.message(player, Message.SUCCESS_PARKOUR_TELEPORTED, new String[]{parkour.getCourseName()});
                    return;
                }

                //Checkpoint item slots
                for (int i = 1; i < parkour.getCheckpointsAmount() + 1; i++) {

                    if (clickedSlot == i) {
                        Location location = parkour.getCheckpoint(i - 1);
                        player.closeInventory();
                        player.teleport(location.add(0, 0.6, 0));
                        location.subtract(0, 0.6, 0);
                        MessageManager.message(player, Message.SUCCESS_CHECKPOINT_TELEPORTED, new String[]{Integer.toString(i), parkour.getCourseName()});
                        return;
                    }

                }

            }

        }

    }

}
