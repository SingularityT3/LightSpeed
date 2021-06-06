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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SpigotDeathEvent implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getEntity().getWorld().getName())) return;
        Player player;
        try {
            player = (Player) event.getEntity();
        } catch (ClassCastException e) {
            return;
        }

        if ((((player.getHealth() - event.getFinalDamage()) <= 0) && event.getEntity() instanceof Player)) {
            event.setCancelled(true);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

            player.getInventory().clear();

            PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
            playerData.setDeaths(playerData.getDeaths() + 1);

            BukkitRunnable respawn = new PlayerRespawn(player, player.getGameMode());
            respawn.runTaskTimer(Main.plugin, 0, 20);

            if (player.getKiller() != null) {
                for (Player player1 : player.getWorld().getPlayers()) {
                    player1.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 2, 1);
                    if (player != player.getKiller())
                        player1.sendMessage(ChatColor.BLUE + player1.getName() + ChatColor.RESET + " was killed by " + ChatColor.RED + player1.getKiller().getName() + ChatColor.RESET + "!");
                    else
                        player1.sendMessage(ChatColor.BLUE + event.getEntity().getName() + ChatColor.RESET + " committed suicide!");
                }

                if (player != player.getKiller()) {
                    PlayerData killerPlayerData = PlayerData.getPlayer(player.getKiller().getUniqueId().toString());
                    killerPlayerData.setSpeedPoints(killerPlayerData.getSpeedPoints() + Main.plugin.getConfig().getDouble("pointsPerKill"));
                    killerPlayerData.setKills(killerPlayerData.getKills() + 1);
                    double health = player.getKiller().getHealth();
                    player.getKiller().setHealth(Math.min(health + 6, player.getKiller().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
                    killerPlayerData.setCombatMode(false);
                    ScoreBoardEvents.update(player.getKiller());
                }
            }

            ScoreBoardEvents.update(player);
        }
    }
}
