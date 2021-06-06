package me.mind31313.lightspeed.tasks;

import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RefreshItem extends BukkitRunnable {
    private final Player player;
    private final Kit kit;
    private final ItemStack item;
    private final int deaths;

    public RefreshItem(Player player, Kit kit, ItemStack item, int deaths) {
        this.player = player;
        this.kit = kit;
        this.item = item;
        this.deaths = deaths;
    }

    @Override
    public void run() {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(player.getWorld().getName())) return;
        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
        if (playerData.getKit() == kit && playerData.getDeaths() == deaths) {
            for (ItemStack item1 : playerData.getKit().getContents()) {
                if (item1 == null) continue;

                if (item1.isSimilar(item)) {
                    for (ItemStack item2 : player.getInventory().getContents()) {
                        if (item2 == null) continue;

                        if (item2.isSimilar(item)) {
                            if (item2.getAmount() < item1.getAmount()) {
                                player.getInventory().addItem(item);
                            }
                            return;
                        }
                    }
                    player.getInventory().addItem(item);
                }
            }
        }
    }
}
