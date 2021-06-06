package me.mind31313.lightspeed;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;

import static me.mind31313.lightspeed.ItemUtils.*;

@SuppressWarnings("deprecation")
public enum Kit {
    DEFAULT(new ItemStack[]{
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew")
    }, new ItemStack[]{
            getLeatherArmor(Material.LEATHER_HELMET, Color.MAROON),
            getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.MAROON),
            getLeatherArmor(Material.LEATHER_LEGGINGS, Color.MAROON),
            getLeatherArmor(Material.LEATHER_BOOTS, Color.MAROON)
    }, "Default", new String[]{"Basic items"}, Material.STICK, 0),

    TANK(new ItemStack[]{
            ItemUtils.getItem(Material.IRON_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew")
    }, new ItemStack[]{
            ItemUtils.getItem(Material.IRON_HELMET),
            ItemUtils.getItem(Material.IRON_CHESTPLATE),
            ItemUtils.getItem(Material.IRON_LEGGINGS),
            ItemUtils.getItem(Material.IRON_BOOTS)
    }, "Tank", new String[]{"Strong armor but low speed"}, Material.IRON_BLOCK, 450, 8),

    MISSILE(new ItemStack[]{
            ItemUtils.getItem(Material.IRON_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew")
    }, new ItemStack[]{}, "Missile", new String[]{"Permanent high speed but no armor"}, Material.ARROW, 500, 20, new HashMap<PotionEffectType, Integer>() {
        {
            put(PotionEffectType.SPEED, 16);
        }
    }),

    ARCHER(new ItemStack[] {
            ItemUtils.getBow(),
            ItemUtils.getStick(),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            ItemUtils.getItem(Material.ARROW)
    }, new ItemStack[]{
            ItemUtils.getItem(Material.CHAINMAIL_HELMET),
            ItemUtils.getItem(Material.CHAINMAIL_CHESTPLATE),
            ItemUtils.getItem(Material.CHAINMAIL_LEGGINGS),
            ItemUtils.getItem(Material.CHAINMAIL_BOOTS)
    }, "Archer", new String[]{"Attack your opponents from a range!"}, Material.BOW, 500),

    JUMPER(new ItemStack[]{
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew")
    }, new ItemStack[]{
            ItemUtils.getLeatherArmor(Material.LEATHER_HELMET, Color.LIME),
            ItemUtils.getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.LIME),
            ItemUtils.getItem(Material.CHAINMAIL_LEGGINGS),
            ItemUtils.getItem(Material.CHAINMAIL_BOOTS)
    }, "Jumper", new String[]{"Pounce on your opponents from the sky!"}, Material.SLIME_BLOCK, 500, 15, new HashMap<PotionEffectType, Integer>() {
        {
            put(PotionEffectType.JUMP, 2);
        }
    }),

    SNOWMAN(new ItemStack[]{
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            ItemUtils.getItem(Material.SNOWBALL, 16),
            ItemUtils.getIceBall()
    }, new ItemStack[]{
            getLeatherArmor(Material.LEATHER_HELMET, Color.WHITE),
            getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.WHITE),
            getLeatherArmor(Material.LEATHER_LEGGINGS, Color.WHITE),
            getLeatherArmor(Material.LEATHER_BOOTS, Color.WHITE)
    }, "Snowman", new String[]{"Throw snowballs and ice balls!"}, Material.SNOW_BLOCK, 600),

    ENDERMAN(new ItemStack[]{
            ItemUtils.getItem(Material.IRON_AXE),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            ItemUtils.getItem(Material.ENDER_PEARL)
    }, new ItemStack[]{
            getLeatherArmor(Material.LEATHER_HELMET, Color.FUCHSIA),
            getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.BLACK),
            getLeatherArmor(Material.LEATHER_LEGGINGS, Color.FUCHSIA),
            getLeatherArmor(Material.LEATHER_BOOTS, Color.BLACK)
    }, "Enderman", new String[]{"vwoop vwoop"}, Material.ENDER_PEARL, 600),

    ALCHEMIST(new ItemStack[]{
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            getPotion(new PotionEffect(PotionEffectType.POISON, 300, 0), Color.GREEN, Material.SPLASH_POTION, "Poison"),
            getPotion(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0), Color.BLACK, Material.SPLASH_POTION, "Strength"),
            getPotion(new PotionEffect(PotionEffectType.INVISIBILITY, 300, 0), Color.BLACK, Material.SPLASH_POTION, "Invisibility")
    }, new ItemStack[]{
            getLeatherArmor(Material.LEATHER_HELMET, Color.YELLOW),
            getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.YELLOW),
            getLeatherArmor(Material.LEATHER_LEGGINGS, Color.YELLOW),
            getLeatherArmor(Material.LEATHER_BOOTS, Color.YELLOW)
    }, "Alchemist", new String[]{"Disorient and poison your", "opponents with potions!"}, Material.BREWING_STAND, 600),

    BATMAN(new ItemStack[]{
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            ItemUtils.getGrapplingHook(),
            ItemUtils.getItem(Material.ARROW, 1, "Hook")
    }, new ItemStack[]{
            getLeatherArmor(Material.LEATHER_HELMET, Color.BLACK),
            getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.BLACK),
            getLeatherArmor(Material.LEATHER_LEGGINGS, Color.BLACK),
            getLeatherArmor(Material.LEATHER_BOOTS, Color.BLACK)
    }, "Batman", new String[]{"Sneak up on your opponents", "with a grappling hook"}, Material.BAT_SPAWN_EGG, 1000),

    PYROMANCER(new ItemStack[] {
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            ItemUtils.getItem(Material.FIRE_CHARGE, 1, "Fireball")
    }, getPyroArmor(), "Pyromancer", new String[]{"Shoot fireballs and burn opponents", "who come too close to you!"}, Material.FIRE_CHARGE, 1000, 15, new HashMap<PotionEffectType, Integer>() {
        {
            put(PotionEffectType.FIRE_RESISTANCE, 0);
        }
    }),

    BOMBER(new ItemStack[]{
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            ItemUtils.getItem(Material.TNT, 5)
    }, getBomberArmor(), "Bomber", new String[]{"BOOM!"}, Material.TNT, 1000),

    ZEUS(new ItemStack[]{
            ItemUtils.getItem(Material.STONE_SWORD),
            ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"),
            getLightningStaff(true)
    }, new ItemStack[]{
            getLeatherArmor(Material.LEATHER_HELMET, Color.YELLOW),
            getLeatherArmor(Material.LEATHER_CHESTPLATE, Color.WHITE),
            getLeatherArmor(Material.LEATHER_LEGGINGS, Color.YELLOW),
            getLeatherArmor(Material.LEATHER_BOOTS, Color.WHITE)
    }, "Zeus", new String[]{"Summon lightning on your command!"}, Material.BEACON, 1500);

    private final ItemStack[] contents;
    private final ItemStack[] armorContents;
    private final ItemStack item;
    private final int price;
    private final int maxSpeed;
    private final HashMap<PotionEffectType, Integer> effects;
    private static final HashMap<Material, Kit> mappings = new HashMap<>();
    private static final HashMap<PotionEffectType, Integer> potionCooldowns = new HashMap<PotionEffectType, Integer>(){
        {
            put(PotionEffectType.POISON, 400);
            put(PotionEffectType.INCREASE_DAMAGE, 400);
            put(PotionEffectType.INVISIBILITY, 700);
        }
    };
    
    Kit(ItemStack[] contents, ItemStack[] armorContents, String displayName, String[] lore, Material material, int price) {
        this(contents, armorContents, displayName, lore, material, price, 15);
    }

    Kit(ItemStack[] contents, ItemStack[] armorContents, String displayName, String[] lore, Material material, int price, int maxSpeed) {
        this(contents, armorContents, displayName, lore, material, price, maxSpeed, new HashMap<>());
    }

    Kit(ItemStack[] contents, ItemStack[] armorContents, String displayName, String[] lore, Material material, int price, int maxSpeed, HashMap<PotionEffectType, Integer> effects) {
        this.contents = contents;
        this.armorContents = armorContents;
        this.price = price;
        this.maxSpeed = maxSpeed;
        this.effects = effects;
        item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + displayName);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getDisplayName() {
        return getItem().getItemMeta().getDisplayName();
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public HashMap<PotionEffectType, Integer> getEffects() {
        return effects;
    }

    public static void init() {
        for (Kit kit : Kit.values()) {
            mappings.put(kit.getItem().getType(), kit);
        }
    }

    public static Kit getFromItem(ItemStack item) {
        return mappings.get(item.getType());
    }

    public static int getPotionCooldown(PotionEffectType potion) {
        return potionCooldowns.get(potion);
    }

    public int getPrice() {
        return price;
    }
}
