package me.mind31313.lightspeed.tasks;

import me.mind31313.lightspeed.ItemUtils;
import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GrapplingHook extends BukkitRunnable {
    private int ticks = 0;
    private final Player player;
    private final Arrow arrow;

    public GrapplingHook(Player player, Arrow arrow) {
        this.player = player;
        this.arrow = arrow;
    }

    public static void addPlayer(Player player, Arrow arrow) {
        BukkitRunnable gp = new GrapplingHook(player, arrow);
        gp.runTaskTimer(Main.plugin, 0, 1);
    }

    @Override
    public void run() {
        if (ticks == 200) {
            if (player.isOnline()) {
                returnArrow();
            }
            cancel();
        }

        if (arrow.isOnGround()) {
            if (ticks % 2 == 0) {
                player.playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, SoundCategory.AMBIENT, 1, 0);
                for (Entity entity : player.getNearbyEntities(15, 15, 15)) {
                    if (entity instanceof Player)
                        ((Player) entity).playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, SoundCategory.AMBIENT, 1, 0);
                }
            }
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            Location arrowLocation = arrow.getLocation();
            Location playerLocation = player.getLocation();
            double x = (arrowLocation.getX() - playerLocation.getX()) / 5;
            double y = (arrowLocation.getY() - playerLocation.getY()) / 5 + 0.7;
            double z = (arrowLocation.getZ() - playerLocation.getZ()) / 5;
            player.setVelocity(new Vector(x ,y, z));

            if (x * 5 < 2 && y * 5 < 2 && z * 5 < 2) {
                returnArrow();
                arrow.remove();
                cancel();
            }
        }

        ticks++;
    }

    private void returnArrow() {
        BukkitRunnable refreshArrow = new RefreshItem(player, Kit.BATMAN, ItemUtils.getItem(Material.ARROW, 1, "Hook"), PlayerData.getPlayer(player.getUniqueId().toString()).getDeaths());
        refreshArrow.runTaskLater(Main.plugin, 80);
    }
}
