package me.mind31313.lightspeed.events;

import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import me.mind31313.lightspeed.inventories.KitSelection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;

@SuppressWarnings("deprecation")
public class InventoryEvents implements Listener {
    @EventHandler
    public void onInventoryAction(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        if (event.getInventory().getHolder() instanceof KitSelection) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            if (clickedInventory.getHolder() instanceof KitSelection) {
                if (Kit.getFromItem(event.getCurrentItem()) != null) {
                    if (event.isRightClick()) {
                        previewKit(clickedInventory, Kit.getFromItem(event.getCurrentItem()));
                    } else if (event.isLeftClick()) {
                        Player player = ((Player) event.getWhoClicked());
                        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
                        Kit kit = Kit.getFromItem(event.getCurrentItem());

                        if (playerData.getKitsPurchased().contains(kit)) {
                            KitSelection.applyKit(player, kit);
                            player.closeInventory();
                            player.sendMessage(ChatColor.GREEN + "Kit applied!");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 2, 2);
                        } else if (playerData.getSpeedPoints() < kit.getPrice()) {
                            player.sendMessage(ChatColor.RED + "You do have enough speed points!");
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.AMBIENT, 2, 1);
                        } else {
                            playerData.getKitsPurchased().add(kit);
                            playerData.setSpeedPoints(playerData.getSpeedPoints() - kit.getPrice());
                            ((KitSelection) clickedInventory.getHolder()).init(player);
                            player.sendMessage(ChatColor.GREEN + "You purchased the " + kit.getDisplayName() + " kit!");
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT, 2, 2);
                            KitSelection.applyKit(player, kit);
                            player.closeInventory();
                        }
                    }
                } else if (event.getCurrentItem().getType() == Material.BARRIER) {
                    ((KitSelection) clickedInventory.getHolder()).init((Player) event.getWhoClicked());
                } else if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    showStats(event.getInventory(), (Player) event.getWhoClicked());
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName()) && !event.getPlayer().hasPermission("lightspeed.item.throw"))
            event.setCancelled(true);
    }

    private void previewKit(Inventory inventory, Kit kit) {
        inventory.clear();

        for (ItemStack armorItem : kit.getArmorContents()) {
            inventory.addItem(armorItem);
        }
        for (ItemStack item : kit.getContents()) {
            inventory.addItem(item);
        }

        ItemStack closePreview = createItem(Material.BARRIER, 1, "Close Preview");
        inventory.setItem(inventory.getSize() - 1, closePreview);
    }

    private void showStats(Inventory inventory, Player player) {
        inventory.clear();

        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());

        ItemStack kills = createItem(Material.DIAMOND_SWORD, 1, ChatColor.GREEN + "Kills: " + playerData.getKills());
        inventory.addItem(kills);

        ItemStack deaths = createItem(Material.SKELETON_SKULL, 1, ChatColor.RED + "Deaths: " + playerData.getDeaths());
        inventory.addItem(deaths);

        DecimalFormat df = new DecimalFormat("0.00");
        ItemStack kd = createItem(Material.COMPASS, 1, ChatColor.YELLOW + "K/D: " + df.format((float) playerData.getKills() / (float) playerData.getDeaths()));
        inventory.addItem(kd);

        ItemStack maxStreak = createItem(Material.IRON_AXE, 1, ChatColor.LIGHT_PURPLE + "Highest streak: " + playerData.getHighestStreak());
        inventory.addItem(maxStreak);

        ItemStack closePreview = createItem(Material.BARRIER, 1, "Close Stats");
        inventory.setItem(inventory.getSize() - 1, closePreview);
    }

    private ItemStack createItem(Material material, int amount, String displayName) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + displayName);
        item.setItemMeta(meta);
        return item;
    }
}
