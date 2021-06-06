package me.mind31313.lightspeed.tasks;

import me.mind31313.lightspeed.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CombatModeUpdate extends BukkitRunnable {
    private static final HashMap<Player, CombatModeUpdate> map = new HashMap<>();

    private final Player player;

    public CombatModeUpdate(Player player) {
        this.player = player;

        if (map.get(player) != null) {
            map.get(player).cancel();
        }

        map.put(player, this);
    }

    @Override
    public void run() {
        if (player.isOnline()) {
            PlayerData.getPlayer(player.getUniqueId().toString()).setCombatMode(false);
        }
    }
}
