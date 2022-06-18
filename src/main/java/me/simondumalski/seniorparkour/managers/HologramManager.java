package me.simondumalski.seniorparkour.managers;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.utils.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HologramManager {

    private final Main plugin = Main.getInstance();
    private final HashMap<Location, List<ArmorStand>> hologramsList = new HashMap<>();

    /**
     * Creates a Hologram at the specified location
     * @param location Location to create the hologram
     * @param text Lines of text for the hologram
     */
    public void createHologram(Location location, List<String> text) {

        //Create a list for storing the ArmourStands
        List<ArmorStand> holograms = new ArrayList<>();

        for (String line : text) {

            //Get the height offset from the config.yml
            int height = plugin.getConfig().getInt("holograms.height");

            //Create the ArmourStand entity
            ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(location.subtract(0, 0.3 - height, 0), EntityType.ARMOR_STAND);

            //Set the entity attributes
            hologram.setGravity(false);
            hologram.setInvisible(true);
            hologram.setInvulnerable(true);

            //Set the hologram name
            hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', line));
            hologram.setCustomNameVisible(true);

            holograms.add(hologram);

        }

        //Add the hologram to the HashMap
        hologramsList.put(location, holograms);

    }

    /**
     * Deletes a Hologram at the specified location
     * @param location Location of the hologram
     */
    public void deleteHologram(Location location) {

        //Remove all the holograms for the specified location
        for (ArmorStand hologram : hologramsList.get(location)) {
            hologram.remove();
        }

        //Remove the hologram from the hashmap
        hologramsList.remove(location);

    }

    /**
     * Returns true/false if the provided ArmorStand is a hologram
     * @param armorStand ArmorStand to check
     * @return True/false
     */
    public boolean isHologram(ArmorStand armorStand) {

        for (List<ArmorStand> list : hologramsList.values()) {
            for (ArmorStand hologram : list) {
                if (hologram.equals(armorStand)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true/false if the provided ArmorStand is a start hologram
     * @param armorStand ArmorStand to check
     * @return True/false
     */
    public boolean isStartHologram(ArmorStand armorStand) {

        for (Parkour parkour : plugin.getParkourManager().getParkourCourses()) {
            for (ArmorStand hologram : hologramsList.get(parkour.getStartLocation())) {
                if (hologram.equals(armorStand)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true/false if the provided ArmorStand is a checkpoint hologram
     * @param armorStand ArmorStand to check
     * @return True/false
     */
    public boolean isCheckpointHologram(ArmorStand armorStand) {

        for (Parkour parkour : plugin.getParkourManager().getParkourCourses()) {
            for (Location checkpointLocation : parkour.getCheckpoints()) {
                for (ArmorStand hologram : hologramsList.get(checkpointLocation)) {
                    if (hologram.equals(armorStand)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns true/false if the provided ArmorStand is an end hologram
     * @param armorStand ArmorStand to check
     * @return True/false
     */
    public boolean isEndHologram(ArmorStand armorStand) {

        for (Parkour parkour : plugin.getParkourManager().getParkourCourses()) {
            for (ArmorStand hologram : hologramsList.get(parkour.getEndLocation())) {
                if (hologram.equals(armorStand)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns the Parkour Course that owns the provided hologram
     * @param armorStand Hologram to check
     * @return Parkour Course owning the hologram, or null if none was found
     */
    public Parkour getHologramParkourOwner(ArmorStand armorStand) {

        for (Parkour parkour : plugin.getParkourManager().getParkourCourses()) {

            //Check the start locations
            for (ArmorStand hologram : hologramsList.get(parkour.getStartLocation())) {
                if (hologram.equals(armorStand)) {
                    return parkour;
                }
            }

            //Check the checkpoint locations
            for (Location checkpointLocation : parkour.getCheckpoints()) {
                for (ArmorStand hologram : hologramsList.get(checkpointLocation)) {
                    if (hologram.equals(armorStand)) {
                        return parkour;
                    }
                }
            }

            //Check the end locations
            for (ArmorStand hologram : hologramsList.get(parkour.getEndLocation())) {
                if (hologram.equals(armorStand)) {
                    return parkour;
                }
            }

        }

        //Return null of no matching owning parkour course was found
        return null;
    }

    public Location getHologramLocation(ArmorStand armorStand) {

        for (Location location : hologramsList.keySet()) {
            for (ArmorStand hologram : hologramsList.get(location)) {
                if (hologram.equals(armorStand)) {
                    return location;
                }
            }
        }

        //Return null if no location was found
        return null;
    }

    /**
     * Returns the hologram at the specified location
     * @param location Location of the hologram
     * @return List of ArmorStands
     */
    public List<ArmorStand> getHologram(Location location) {
        return hologramsList.get(location);
    }

    /**
     * Deletes all the holograms and removes them from the HashMap.
     * Used when saving the holograms to file
     */
    public void removeAllHolograms() {

        for (List<ArmorStand> armorStandList : hologramsList.values()) {
            for (ArmorStand armorStand : armorStandList) {
                armorStand.remove();
            }
        }

        hologramsList.clear();

    }

}
