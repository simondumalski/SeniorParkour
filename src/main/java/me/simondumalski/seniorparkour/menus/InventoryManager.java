package me.simondumalski.seniorparkour.menus;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.parkour.Parkour;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class InventoryManager {

    private final Main plugin;

    /**
     * Constructor for the InventoryManager
     * @param plugin Instance of the main plugin class
     */
    public InventoryManager(Main plugin) {
        this.plugin = plugin;
    }

    public void openInfoInventory(Player player, Parkour parkour) {

        int slots = parkour.getCheckpointsAmount() + 2;

        while ((slots % 9) != 0) {
            slots++;
        }

        //Get the name for the Info GUI
        String inventoryName = plugin.getConfig().getString("gui.info.gui-name");

        //Check if the specified name is valid
        if (inventoryName == null) {
            inventoryName = "&f%parkour% &bInfo";
        }

        //Replace the placeholders
        if (inventoryName.contains("%parkour%")) {
            inventoryName = inventoryName.replace("%parkour%", parkour.getCourseName());
        }

        Inventory inventory = plugin.getServer().createInventory(null, slots, ChatColor.translateAlternateColorCodes('&', inventoryName));

        //Get the material for the starting point item
        Material startMaterial;

        try {
            startMaterial = Material.valueOf(plugin.getConfig().getString("gui.info.start-point.item"));
        } catch (Exception ex) {
            System.out.println("Specified start item material is invalid.");
            return;
        }

        //Get the starting point item and get the item meta
        ItemStack startItem = new ItemStack(startMaterial, 1);
        ItemMeta startItemMeta = startItem.getItemMeta();

        if (startItemMeta == null) {
            return;
        }

        //Get the item name from the config.yml
        String startItemName = plugin.getConfig().getString("gui.info.start-point.name");

        //Check that the specified name is valid
        if (startItemName == null) {
            startItemName = "&a%parkour% &eStart";
        }

        //Replace the item name placeholders
        if (startItemName.contains("%parkour%")) {
            startItemName = startItemName.replace("%parkour%", parkour.getCourseName());
        }

        //Set the item name
        startItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', startItemName));

        //Get the item lore from the config.yml
        List<String> startItemLore = plugin.getConfig().getStringList("gui.info.start-point.lore");
        List<String> startItemLoreChanged = new ArrayList<>();

        for (String loreLine : startItemLore) {

            if (loreLine.contains("%parkour%")) {
                loreLine = loreLine.replace("%parkour%", parkour.getCourseName());
            }

            if (loreLine.contains("%location%")) {

                int x = parkour.getStartLocation().getBlockX();
                int y = parkour.getStartLocation().getBlockY();
                int z = parkour.getStartLocation().getBlockZ();

                String locationText = "X: " + x + " Y: " + y + " Z: " + z;

                loreLine = loreLine.replace("%location%", locationText);

            }

            startItemLoreChanged.add(ChatColor.translateAlternateColorCodes('&', loreLine));

        }

        //Set the item lore
        startItemMeta.setLore(startItemLoreChanged);
        startItem.setItemMeta(startItemMeta);
        inventory.setItem(0, startItem);

        if (parkour.getEndLocation() != null) {

            //Get the material for the ending point item
            Material endMaterial;

            try {
                endMaterial = Material.valueOf(plugin.getConfig().getString("gui.info.end-point.item"));
            } catch (Exception ex) {
                System.out.println("Specified end item material is invalid.");
                return;
            }

            //Get the ending point item and get the item meta
            ItemStack endItem = new ItemStack(endMaterial, 1);
            ItemMeta endItemMeta = endItem.getItemMeta();

            if (endItemMeta == null) {
                return;
            }

            //Get the item name from the config.yml
            String endItemName = plugin.getConfig().getString("gui.info.end-point.name");

            //Check that the specified name is valid
            if (endItemName == null) {
                endItemName = "&a%parkour% &eEnd";
            }

            //Replace the item name placeholders
            if (endItemName.contains("%parkour%")) {
                endItemName = endItemName.replace("%parkour%", parkour.getCourseName());
            }

            //Set the item name
            endItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', endItemName));

            //Get the item lore from the config.yml
            List<String> endItemLore = plugin.getConfig().getStringList("gui.info.end-point.lore");
            List<String> endItemLoreChanged = new ArrayList<>();

            for (String loreLine : endItemLore) {

                if (loreLine.contains("%parkour%")) {
                    loreLine = loreLine.replace("%parkour%", parkour.getCourseName());
                }

                if (loreLine.contains("%location%")) {

                    int x = parkour.getEndLocation().getBlockX();
                    int y = parkour.getEndLocation().getBlockY();
                    int z = parkour.getEndLocation().getBlockZ();

                    String locationText = "X: " + x + " Y: " + y + " Z: " + z;

                    loreLine = loreLine.replace("%location%", locationText);

                }

                endItemLoreChanged.add(ChatColor.translateAlternateColorCodes('&', loreLine));

            }

            //Set the item lore
            endItemMeta.setLore(endItemLoreChanged);
            endItem.setItemMeta(endItemMeta);
            inventory.setItem(parkour.getCheckpointsAmount() + 1, endItem);

        }

        //Create the checkpoint items
        for (int i = 1; i < parkour.getCheckpointsAmount() + 1; i++) {

            Location checkpoint = parkour.getCheckpoint(i - 1);

            Material material;

            try {
                material = Material.valueOf(plugin.getConfig().getString("gui.info.checkpoint.item"));
            } catch (Exception ex) {
                System.out.println("Specified checkpoint item material is invalid.");
                return;
            }

            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();

            if (meta == null) {
                return;
            }

            //Get the item name from the config.yml
            String itemName = plugin.getConfig().getString("gui.info.checkpoint.name");

            //Check that the specified name is valid
            if (itemName == null) {
                itemName = "&a%parkour% &eCheckpoint #%checkpoint%";
            }

            //Replace the item name placeholders
            if (itemName.contains("%parkour%")) {
                itemName = itemName.replace("%parkour%", parkour.getCourseName());
            }

            if (itemName.contains("%checkpoint%")) {
                itemName = itemName.replace("%checkpoint%", Integer.toString(i));
            }

            //Set the item name
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));

            //Get the item lore from the config.yml
            List<String> itemLore = plugin.getConfig().getStringList("gui.info.checkpoint.lore");
            List<String> itemLoreChanged = new ArrayList<>();

            for (String loreLine : itemLore) {

                if (loreLine.contains("%parkour%")) {
                    loreLine = loreLine.replace("%parkour%", parkour.getCourseName());
                }

                if (loreLine.contains("%location%")) {

                    int x = checkpoint.getBlockX();
                    int y = checkpoint.getBlockY();
                    int z = checkpoint.getBlockZ();

                    String locationText = "X: " + x + " Y: " + y + " Z: " + z;

                    loreLine = loreLine.replace("%location%", locationText);

                }

                if (loreLine.contains("%checkpoint%")) {
                    loreLine = loreLine.replace("%checkpoint%", Integer.toString(i));
                }

                itemLoreChanged.add(ChatColor.translateAlternateColorCodes('&', loreLine));

            }

            //Set the item lore
            meta.setLore(itemLoreChanged);
            item.setItemMeta(meta);
            inventory.setItem(i, item);

        }

        player.openInventory(inventory);

    }

    /**
     * Opens the inventory containing the Parkour Statistics for the player
     * @param player Player to open the inventory for
     * @param target Player whose parkour stats will be shown
     * @param data Player's parkour stats
     */
    public void openStatsInventory(Player player, Player target, HashMap<Parkour, Integer> data) {

        int size = data.size();

        while (size % 9 != 0) {
            size++;
        }

        //Get the name for the Stats GUI
        String inventoryName = plugin.getConfig().getString("gui.stats.gui-name");

        //Check if the specified name is valid
        if (inventoryName == null) {
            inventoryName = "%player%''s Parkour Stats";
        }

        //Replace the placeholders
        if (inventoryName.contains("%player%")) {
            inventoryName = inventoryName.replace("%player%", target.getName());
        }

        Inventory inventory = plugin.getServer().createInventory(null, size, ChatColor.translateAlternateColorCodes('&', inventoryName));

        int i = 0;

        for (Parkour parkour : data.keySet()) {

            int completionTime = data.get(parkour);
            String timeConverted = plugin.timeToString(completionTime);

            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            if (meta == null) {
                return;
            }

            //Get the item name from the config.yml
            String itemName = plugin.getConfig().getString("gui.stats.item.name");

            //Check if the specified name is valid
            if (itemName == null) {
                itemName = "&e%player%";
            }

            //Replace the placeholders
            if (itemName.contains("%player%")) {
                itemName = itemName.replace("%player%", target.getName());
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            meta.setOwningPlayer(target);

            //Get the item lore from the config.yml
            List<String> lore = plugin.getConfig().getStringList("gui.stats.item.lore");

            //Check if the specified lore is valid
            if (lore.isEmpty()) {
                lore.add("&7Course: &f%parkour%");
                lore.add("&7Time: &f%time%");
            }

            //Replace the placeholders
            List<String> updatedLore = new ArrayList<>();

            for (String line : lore) {

                if (line.contains("%parkour%")) {
                    line = line.replace("%parkour%", parkour.getCourseName());
                }

                if (line.contains("%time%")) {
                    line = line.replace("%time%", timeConverted);
                }

                updatedLore.add(ChatColor.translateAlternateColorCodes('&', line));

            }

            //Set the item lore and meta
            meta.setLore(updatedLore);
            item.setItemMeta(meta);

            //Add the item to the inventory
            inventory.setItem(i, item);

            i++;
        }

        player.openInventory(inventory);

    }

    /**
     * Opens the inventory showing the top player stats for the specified parkour
     * @param player Player to open the inventory to
     * @param parkour Parkour whose data is being shown
     * @param data HashMap of the parkour data
     */
    public void openTopInventory(Player player, Parkour parkour, HashMap<UUID, Integer> data) {

        int size = data.size();

        while (size % 9 != 0 ) {
            size++;
        }

        if (size > 54) {
            size = 54;
        }

        //Get the name of the GUI from the config
        String inventoryName = plugin.getConfig().getString("gui.top.gui-name");

        //Check if the specified name is valid
        if (inventoryName == null) {
            inventoryName = "&f%parkour% &bLeaderboard";
        }

        //Replace the placeholders for the GUI name
        if (inventoryName.contains("%parkour%")) {
            inventoryName = inventoryName.replace("%parkour%", parkour.getCourseName());
        }

        Inventory inventory = plugin.getServer().createInventory(null, size, ChatColor.translateAlternateColorCodes('&', inventoryName));

        //Sort the parkour leaderboard
        HashMap<UUID, Integer> sortedData = sortData(data);

        int i = 0;

        for (UUID uuid : sortedData.keySet()) {

            //Get the player the UUID belongs to
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);

            //Get the time the course was completed in
            int timeCompleted = data.get(uuid);

            //Convert the time completed to a string
            String formattedTimeCompleted = plugin.timeToString(timeCompleted);

            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            if (meta == null) {
                return;
            }

            //Get the item name from the config.yml
            String itemName = plugin.getConfig().getString("gui.top.item.name");

            //Check if the specified item name is valid
            if (itemName == null) {
                itemName = "&e%player%";
            }

            //Replace the placeholders
            if (itemName.contains("%player%")) {
                itemName = itemName.replace("%player%", offlinePlayer.getName());
            }

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            meta.setOwningPlayer(offlinePlayer);

            //Get the item lore from the config.yml
            List<String> lore = plugin.getConfig().getStringList("gui.top.item.lore");

            //Check if the specified item lore is valid
            if (lore.isEmpty()) {
                lore.add("&6Time: &f%time%");
                lore.add("&cPosition: &e%position%");
            }

            //Replace the placeholders
            List<String> updatedLore = new ArrayList<>();

            for (String line : lore) {

                if (line.contains("%time%")) {
                    line = line.replace("%time%", formattedTimeCompleted);
                }

                if (line.contains("%position%")) {
                    line = line.replace("%position%", Integer.toString(i + 1));
                }

                updatedLore.add(ChatColor.translateAlternateColorCodes('&', line));

            }

            //Set the item lore and meta
            meta.setLore(updatedLore);
            item.setItemMeta(meta);

            //Add the item to the inventory
            inventory.setItem(i, item);

            i++;

            if (i > 53) {
                break;
            }

        }

        player.openInventory(inventory);

    }

    /**
     * Sorts the data in the provided HashMap by value
     * @param unsortedData HashMap to sort
     * @return Sorted HashMap
     */
    public HashMap<UUID, Integer> sortData(HashMap<UUID, Integer> unsortedData) {

        LinkedHashMap<UUID, Integer> sortedData = new LinkedHashMap<>();

        ArrayList<Integer> list = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : unsortedData.entrySet()) {
            list.add(entry.getValue());
        }

        list.sort(Integer::compareTo);

        for (Integer timeInt : list) {
            for (Map.Entry<UUID, Integer> entry : unsortedData.entrySet()) {
                if (entry.getValue().equals(timeInt)) {
                    sortedData.put(entry.getKey(), timeInt);
                }
            }
        }

        System.out.println(sortedData);
        return sortedData;
    }

}
