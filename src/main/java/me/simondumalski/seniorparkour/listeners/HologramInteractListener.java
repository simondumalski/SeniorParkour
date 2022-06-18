package me.simondumalski.seniorparkour.listeners;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.managers.MessageManager;
import me.simondumalski.seniorparkour.managers.TrialManager;
import me.simondumalski.seniorparkour.utils.Message;
import me.simondumalski.seniorparkour.utils.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class HologramInteractListener implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {

        //Get the player who is interacting
        Player player = e.getPlayer();

        //Get the entity being right-clicked
        Entity entity = e.getRightClicked();

        e.setCancelled(perform(player, entity));

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        //Get the attacking entity
        Entity attacker = e.getDamager();

        //Check if the attacking entity is a player
        if (attacker.getType() != EntityType.PLAYER) {
            return;
        }

        //Cast the attacking entity to a player
        Player player = (Player) attacker;

        //Get the defending entity
        Entity defender = e.getEntity();

        e.setCancelled(perform(player, defender));

    }

    private boolean perform(Player player, Entity entity) {

        //Check if the entity is an armor stand
        if (entity.getType() != EntityType.ARMOR_STAND) {
            return false;
        }

        //Cast the entity to an ArmorStand
        ArmorStand armorStand = (ArmorStand) entity;

        //Check if the ArmorStand is a hologram
        if (!plugin.getHologramManager().isHologram(armorStand)) {
            return false;
        }

        //Get the plugin's TrialManager
        TrialManager trialManager = plugin.getTrialManager();

        //Check if the hologram is a start hologram
        if (plugin.getHologramManager().isStartHologram(armorStand)) {

            //Get the Parkour Course the start hologram is for
            Parkour parkour = plugin.getHologramManager().getHologramParkourOwner(armorStand);

            //Check if the Parkour Course is valid
            if (parkour == null) {
                return true;
            }

            //Check if the Parkour Course has an end point
            if (parkour.getEndLocation() == null) {
                MessageManager.message(player, Message.ERROR_NO_END_POINT, null);
                return true;
            }

            //Check if the player is already in a Parkour Course
            if (trialManager.isPlayerInParkour(player)) {

                //Check if the Parkour Course they are trying to join is the one they are already in
                if (trialManager.getParkour(player).equals(parkour)) {
                    MessageManager.message(player, Message.ERROR_ALREADY_IN_PARKOUR, null);
                } else {
                    MessageManager.message(player, Message.ERROR_MUST_LEAVE_PARKOUR, null);
                }

            } else {

                //Start the parkour course
                trialManager.startParkour(player, parkour);
                MessageManager.message(player, Message.SUCCESS_STARTED_PARKOUR, new String[]{parkour.getCourseName()});

            }

            return true;
        }

        //Check if the player is in a parkour course
        if (!trialManager.isPlayerInParkour(player)) {
            MessageManager.message(player, Message.ERROR_NOT_IN_PARKOUR, null);
            return true;
        }

        //Check if the hologram is a checkpoint hologram
        if (plugin.getHologramManager().isCheckpointHologram(armorStand)) {

            //Get the Parkour Course the checkpoint hologram is for
            Parkour parkour = plugin.getHologramManager().getHologramParkourOwner(armorStand);

            //Check if the Parkour Course is valid
            if (parkour == null) {
                return true;
            }

            //Check if the Parkour Course being interacted with is the one they are in
            if (!trialManager.getParkour(player).equals(parkour)) {
                MessageManager.message(player, Message.ERROR_MUST_LEAVE_PARKOUR, null);
                return true;
            }

            //Get the player's current location in the Parkour Course
            Location currentLocation = trialManager.getParkourLocation(player);

            //Get the checkpoint that the hologram is owned by
            Location checkpoint = plugin.getHologramManager().getHologramLocation(armorStand);

            //Check that the hologram location is valid
            if (checkpoint == null) {
                return true;
            }

            //Get the number of the checkpoint
            int checkpointNumber = parkour.getCheckpointNumber(checkpoint);

            //Check if the player already passed the last checkpoint
            if (parkour.getCheckpointNumber(currentLocation) >= parkour.getCheckpointsAmount()) {
                MessageManager.message(player, Message.ERROR_ALREADY_PASSED_CHECKPOINT, null);
                return true;
            }

            //Check if the player is clicking the same checkpoint
            if (currentLocation.equals(checkpoint)) {
                MessageManager.message(player, Message.ERROR_ALREADY_PASSED_CHECKPOINT, null);
                return true;
            }

            //Check if the player is clicking a previous checkpoint
            for (Location location : parkour.getPreviousCheckpoints(checkpoint)) {
                if (location.equals(checkpoint)) {
                    MessageManager.message(player, Message.ERROR_ALREADY_PASSED_CHECKPOINT, null);
                    return true;
                }
            }

            //Check if the player is clicking the only checkpoint in the course
            if (parkour.getCheckpointsAmount() <= 1) {
                trialManager.specialAdvanceParkour(player);
                MessageManager.message(player, Message.SUCCESS_PASSED_CHECKPOINT, new String[]{Integer.toString(checkpointNumber), parkour.getCourseName()});
                return true;
            }

            //Check if the player is clicking the first checkpoint
            if (checkpoint.equals(parkour.getFirstCheckpoint())) {
                trialManager.advanceParkour(player);
                MessageManager.message(player, Message.SUCCESS_PASSED_CHECKPOINT, new String[]{Integer.toString(checkpointNumber), parkour.getCourseName()});
                return true;
            }

            //Check if the player has passed the previous checkpoint
            if (!currentLocation.equals(parkour.getPreviousCheckpoint(checkpoint))) {
                MessageManager.message(player, Message.ERROR_MUST_PASS_PREVIOUS_CHECKPOINT, null);
                return true;
            }

            //Advance the player in the parkour
            trialManager.advanceParkour(player);
            MessageManager.message(player, Message.SUCCESS_PASSED_CHECKPOINT, new String[]{Integer.toString(checkpointNumber), parkour.getCourseName()});

            return true;
        }

        //Check if the hologram is an end hologram
        if (plugin.getHologramManager().isEndHologram(armorStand)) {

            //Get the Parkour Course the checkpoint hologram is for
            Parkour parkour = plugin.getHologramManager().getHologramParkourOwner(armorStand);

            //Check if the Parkour Course is valid
            if (parkour == null) {
                return true;
            }

            //Check if the Parkour Course being interacted with is the one they are in
            if (!trialManager.getParkour(player).equals(parkour)) {
                MessageManager.message(player, Message.ERROR_MUST_LEAVE_PARKOUR, null);
                return true;
            }

            //Get the player's current location in the Parkour Course
            Location currentLocation = trialManager.getParkourLocation(player);

            //Get the checkpoint that the hologram is owned by
            Location hologramLocation = plugin.getHologramManager().getHologramLocation(armorStand);

            //Check that the hologram location is valid
            if (hologramLocation == null) {
                return true;
            }

            //Check if there are any checkpoints in the course
            if (parkour.getCheckpointsAmount() > 0) {
                //Check if the player has passed the last checkpoint
                if (!currentLocation.equals(parkour.getLastCheckpoint())) {
                    MessageManager.message(player, Message.ERROR_MUST_PASS_PREVIOUS_CHECKPOINT, null);
                    return true;
                }
            }

            //End the parkour for the player
            int timeInParkour = trialManager.endParkour(player);
            MessageManager.message(player, Message.SUCCESS_FINISHED_PARKOUR, new String[]{parkour.getCourseName(), plugin.timeToString(timeInParkour)});

            //Insert the stats into the database
            plugin.getDatabaseManager().insertStats(player, parkour, timeInParkour);

            return true;
        }

        return true;
    }



}
