package me.mind31313.lightspeed.events;

import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CoreEvents implements Listener {
    private final double crashThreshold = 0.1;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isFlying() || !Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) {
            return;
        }

        String uuid = player.getUniqueId().toString();
        PlayerData playerData = PlayerData.getPlayer(uuid);
        double currentSpeed = playerData.getSpeed();
        double xDiff = event.getTo().getX() - event.getFrom().getX();
        double zDiff = event.getTo().getZ() - event.getFrom().getZ();
        int x = event.getTo().getBlockX();
        int y = event.getTo().getBlockY();
        int z = event.getTo().getBlockZ();
        World world = player.getWorld();

        if (Math.abs(xDiff) < crashThreshold && Math.abs(zDiff) < crashThreshold && currentSpeed > 0 && player.getPotionEffect(PotionEffectType.SPEED) != null) {
            playerData.setSpeed(0);
        } else if (Math.abs(xDiff) > 0.15 || Math.abs(zDiff) > 0.15) {
            playerData.setSpeed(currentSpeed + Main.plugin.getConfig().getDouble("speedUpRate"));
            playerData.setSpeedPoints(playerData.getSpeedPoints() + Main.plugin.getConfig().getDouble("pointGainRate"));
        } else if (currentSpeed != 0) {
            playerData.setSpeed(0);
        }

        if (playerData.getKit().getEffects().containsKey(PotionEffectType.SPEED)) {
            return;
        }

        double updatedSpeed = playerData.getSpeed();
        PotionEffect applied = player.getPotionEffect(PotionEffectType.SPEED);

        if ((applied == null && getEffectStrength(updatedSpeed, playerData.getKit()) != 0) || (applied != null && applied.getAmplifier() != getEffectStrength(updatedSpeed, playerData.getKit()))) {
            PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 5, getEffectStrength(updatedSpeed, playerData.getKit()), true, false, false);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(effect);
        }
    }

    private int getEffectStrength(double speed, Kit kit) {
        speed = Math.min(speed, 20);
        int effectStrength = (int) (-1 * Math.pow(speed, 1.4) + 4.565 * speed);
        return Math.min(effectStrength, kit.getMaxSpeed());
    }
}
