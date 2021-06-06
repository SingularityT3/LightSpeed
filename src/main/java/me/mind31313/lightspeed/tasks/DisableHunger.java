package me.mind31313.lightspeed.tasks;

import me.mind31313.lightspeed.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DisableHunger extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Main.plugin.getConfig().getStringList("worlds").contains(player.getWorld().getName())) continue;
            player.setFoodLevel(20);
        }
    }
}
