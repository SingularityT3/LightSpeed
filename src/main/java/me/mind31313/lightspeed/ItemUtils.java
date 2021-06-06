package me.mind31313.lightspeed;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("deprecation")
public class ItemUtils {
    public static ItemStack getItem(Material material) {
        return getItem(material, 1);
    }

    public static ItemStack getItem(Material material, int amount) {
        return getItem(material, amount, null);
    }

    public static ItemStack getItem(Material material, int amount, String displayName) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        if (displayName != null) {
            meta.setDisplayName(ChatColor.RESET + displayName);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getPotion(PotionEffect effect, Color color, Material material, String displayName) {
        ItemStack item = new ItemStack(material, 1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.addCustomEffect(effect, true);
        meta.setColor(color);
        meta.setDisplayName(ChatColor.RESET + displayName);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getLeatherArmor(Material material, Color color) {
        ItemStack item = new ItemStack(material, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setUnbreakable(true);
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack[] getPyroArmor() {
        ItemStack[] armor = new ItemStack[4];

        armor[0] = getLeatherArmor(Material.LEATHER_HELMET, Color.ORANGE);
        armor[1] = getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.ORANGE);
        armor[2] = getLeatherArmor(Material.LEATHER_LEGGINGS, Color.ORANGE);
        armor[3] = getLeatherArmor(Material.LEATHER_BOOTS, Color.ORANGE);

        for (ItemStack item : armor) {
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_FIRE, 1, true);
            item.setItemMeta(meta);
        }

        return armor;
    }

    public static ItemStack[] getBomberArmor() {
        ItemStack[] armor = new ItemStack[4];

        armor[0] = getLeatherArmor(Material.LEATHER_HELMET, Color.PURPLE);
        armor[1] = getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.PURPLE);
        armor[2] = getLeatherArmor(Material.LEATHER_LEGGINGS, Color.PURPLE);
        armor[3] = getLeatherArmor(Material.LEATHER_BOOTS, Color.PURPLE);

        for (ItemStack item : armor) {
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 5, true);
            item.setItemMeta(meta);
        }

        return armor;
    }

    public static ItemStack getIceBall() {
        ItemStack iceball = new ItemStack(Material.SNOWBALL, 1);
        ItemMeta meta = iceball.getItemMeta();
        meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.RESET + "Iceball");
        iceball.setItemMeta(meta);
        return iceball;
    }

    public static ItemStack getStick() {
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Knockback stick");
        meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        stick.setItemMeta(meta);
        return stick;
    }

    public static ItemStack getBow() {
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta meta = bow.getItemMeta();
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
        bow.setItemMeta(meta);
        return bow;
    }

    public static ItemStack getGrapplingHook() {
        ItemStack hook = new ItemStack(Material.BOW, 1);
        ItemMeta meta = hook.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.RESET + "Grappling hook");
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        hook.setItemMeta(meta);
        return hook;
    }

    public static ItemStack getLightningStaff(boolean charged) {
        ItemStack staff = new ItemStack(Material.STICK, 1);
        ItemMeta meta = staff.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Lightning staff");

        if (charged) {
            meta.setLore(Arrays.asList("Right click to summon", "lightning!"));
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.setLore(Collections.singletonList("Charging up..."));
        }

        staff.setItemMeta(meta);
        return staff;
    }
}
