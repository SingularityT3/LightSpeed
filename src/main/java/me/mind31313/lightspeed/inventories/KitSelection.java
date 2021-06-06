package me.mind31313.lightspeed.inventories;

import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import me.mind31313.lightspeed.events.ScoreBoardEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class KitSelection implements InventoryHolder {

    private final Inventory inventory;

    public KitSelection() {
        inventory = Bukkit.createInventory(this, 36, "Kits");
    }

    public void init(Player player) {
        inventory.clear();

        for (Kit kit : Kit.values()) {
            ItemStack item = kit.getItem().clone();
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            lore = lore == null ? new ArrayList<>() : lore;
            String result;
            PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
            if (playerData.getKitsPurchased().contains(kit)) {
                if (playerData.getKit() == kit) {
                    result = ChatColor.AQUA + "Applied";
                } else {
                    result = ChatColor.GREEN + "Purchased";
                }
            } else {
                result = ChatColor.YELLOW + "Price: " + ChatColor.BOLD + kit.getPrice();
            }
            lore.add(result);
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        ItemStack speedpoints = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = speedpoints.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "SpeedPoints: " + ChatColor.BOLD + (int) Math.floor(PlayerData.getPlayer(player.getUniqueId().toString()).getSpeedPoints()));
        speedpoints.setItemMeta(meta);
        inventory.setItem(29, speedpoints);

        ItemStack stats = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) stats.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skullMeta.setDisplayName(ChatColor.GRAY + "Your stats");
        stats.setItemMeta(skullMeta);
        inventory.setItem(31, stats);

        ItemStack hint = new ItemStack(Material.BOOK, 1);
        ItemMeta hintMeta = hint.getItemMeta();
        hintMeta.setDisplayName(ChatColor.GREEN + "Left click to purchase");
        hintMeta.setLore(Collections.singletonList(ChatColor.BLUE + "Right click to preview"));
        hint.setItemMeta(hintMeta);
        inventory.setItem(33, hint);
    }

    public static void applyKit(Player player, Kit kit) {
        PlayerData.getPlayer(player.getUniqueId().toString()).setKit(kit);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        if (!Main.plugin.getConfig().getStringList("worlds").contains(player.getWorld().getName())) return;

        Inventory inventory = player.getInventory();
        inventory.clear();

        for (ItemStack armor : kit.getArmorContents()) {
            EntityEquipment equipment = player.getEquipment();
            equipment.setItem(armor.getType().getEquipmentSlot(), armor);
        }

        for (ItemStack item : kit.getContents()) {
            inventory.addItem(item);
        }

        ScoreBoardEvents.update(player);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
