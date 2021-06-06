package me.mind31313.lightspeed.tasks;

import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KitEffectHandler extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Main.plugin.getConfig().getStringList("worlds").contains(player.getWorld().getName())) continue;
            PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
            for (PotionEffectType effectType : playerData.getKit().getEffects().keySet()) {
                player.addPotionEffect(new PotionEffect(effectType, 100, playerData.getKit().getEffects().get(effectType), true, false, false));
            }
        }
    }
}
