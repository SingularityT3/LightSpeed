package me.mind31313.lightspeed.tasks;

import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class ReplaceItem extends BukkitRunnable {
    private final Player player;
    private final ItemStack original;
    private final ItemStack replaceItem;
    private final Kit kit;
    private final int deaths;

    public ReplaceItem(Player player, ItemStack original, ItemStack replaceItem, Kit kit, int deaths) {
        this.player = player;
        this.original = original;
        this.replaceItem = replaceItem;
        this.kit = kit;
        this.deaths = deaths;
    }

    @Override
    public void run() {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(player.getWorld().getName())) return;
        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
        if (playerData.getKit() != kit || playerData.getDeaths() != deaths) return;

        PlayerInventory inventory = player.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.equals(original)) {
                item.setItemMeta(replaceItem.getItemMeta());
                item.setAmount(replaceItem.getAmount());
                item.setType(replaceItem.getType());
            }
        }
    }
}
