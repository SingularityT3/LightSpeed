package me.mind31313.lightspeed.events;

import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import me.mind31313.lightspeed.tasks.PlayerRespawn;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PaperDeathEvent implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getEntity().getWorld().getName())) return;

        event.setCancelled(true);
        event.getEntity().setHealth(event.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        event.getEntity().getInventory().clear();

        PlayerData playerData = PlayerData.getPlayer(event.getEntity().getUniqueId().toString());
        playerData.setDeaths(playerData.getDeaths() + 1);

        BukkitRunnable respawn = new PlayerRespawn(event.getEntity(), event.getEntity().getGameMode());
        respawn.runTaskTimer(Main.plugin, 0, 20);

        if (event.getEntity().getKiller() != null) {
            for (Player player : event.getEntity().getWorld().getPlayers()) {
                player.playSound(event.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 2, 1);
                if (event.getEntity() != event.getEntity().getKiller())
                    player.sendMessage(ChatColor.BLUE + event.getEntity().getName() + ChatColor.RESET + " was killed by " + ChatColor.RED + event.getEntity().getKiller().getName() + ChatColor.RESET + "!");
                else
                    player.sendMessage(ChatColor.BLUE + event.getEntity().getName() + ChatColor.RESET + " committed suicide!");
            }

            if (event.getEntity() != event.getEntity().getKiller()) {
                PlayerData killerPlayerData = PlayerData.getPlayer(event.getEntity().getKiller().getUniqueId().toString());
                killerPlayerData.setSpeedPoints(killerPlayerData.getSpeedPoints() + Main.plugin.getConfig().getDouble("pointsPerKill"));
                killerPlayerData.setKills(killerPlayerData.getKills() + 1);
                double health = event.getEntity().getKiller().getHealth();
                event.getEntity().getKiller().setHealth(Math.min(health + 6, event.getEntity().getKiller().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
                killerPlayerData.setCombatMode(false);
                ScoreBoardEvents.update(event.getEntity().getKiller());
            }
        }

        ScoreBoardEvents.update(event.getEntity());
    }
}
