package me.mind31313.lightspeed.tasks;

import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import me.mind31313.lightspeed.inventories.KitSelection;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class PlayerRespawn extends BukkitRunnable {
    private final Player player;
    private Location spawnpoint;
    private final GameMode gamemode;
    private int countdown;

    public PlayerRespawn(Player player, GameMode gamemode) {
        this.player = player;
        this.gamemode = gamemode;
        countdown = Main.plugin.getConfig().getInt("respawnCooldown");
        ConfigurationSection spawns = Main.plugin.getConfig().getConfigurationSection("spawns");
        if (spawns == null || !spawns.contains(player.getWorld().getName())) {
            spawnpoint = player.getWorld().getSpawnLocation();
        } else {
            try {
                Random random = new Random();
                List<List<Integer>> worldSpawns = (List<List<Integer>>) spawns.getList(player.getWorld().getName());
                List<Integer> coordinates = worldSpawns.get(random.nextInt(worldSpawns.size()));
                spawnpoint = new Location(player.getWorld(), coordinates.get(0), coordinates.get(1), coordinates.get(2));
            } catch (Exception e) {
                spawnpoint = player.getWorld().getSpawnLocation();
            }
        }
    }

    @Override
    public void run() {
        if (player.getGameMode() != GameMode.SPECTATOR && countdown > 0) player.setGameMode(GameMode.SPECTATOR);

        if (countdown == 0) {
            player.teleport(spawnpoint, PlayerTeleportEvent.TeleportCause.PLUGIN);
            player.setInvulnerable(true);
            player.setGameMode(gamemode);
        }

        if (countdown == -1 * (Main.plugin.getConfig().getInt("spawnProtection"))) {
            if (player.isInvulnerable()) {
                player.setInvulnerable(false);
                player.sendMessage(ChatColor.RED + "Spawn protection expired!");
            }
            cancel();
            return;
        }


        if (countdown >= 0) {
            player.sendTitle(ChatColor.RED + "You Died!", "Respawning in " + countdown + " seconds...", 0, 20, 0);
            player.playSound(player.getLocation(), (countdown != 0 ? Sound.UI_BUTTON_CLICK : Sound.ENTITY_ENDER_DRAGON_AMBIENT), SoundCategory.AMBIENT, 3, 1);
            KitSelection.applyKit(player, PlayerData.getPlayer(player.getUniqueId().toString()).getKit());
            if (countdown == 0) {
                player.sendMessage(ChatColor.RED + "Spawn protection expires in " + Main.plugin.getConfig().getInt("spawnProtection") + " seconds!");
            }
        } else if (!player.isInvulnerable()) {
            cancel();
        }

        countdown--;
    }
}
