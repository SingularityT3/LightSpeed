package me.mind31313.lightspeed.events;

import me.mind31313.lightspeed.ItemUtils;
import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import me.mind31313.lightspeed.inventories.KitSelection;
import me.mind31313.lightspeed.tasks.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class MiscEvents implements Listener {
    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        if (event.getItem() == null || !(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) || !Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) return;

        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
        ItemStack item = event.getItem();

        if (item.equals(ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"))) {
            event.setCancelled(true);
            double diff = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - player.getHealth();
            player.setHealth(player.getHealth() + Math.min(6, diff));

            ItemStack emptyStew = ItemUtils.getItem(Material.BOWL, 1, "Mythical Stew");
            player.getInventory().setItem(event.getHand(), emptyStew);

            BukkitRunnable refreshFood = new ReplaceItem(player, emptyStew, ItemUtils.getItem(Material.MUSHROOM_STEW, 1, "Mythical Stew"), playerData.getKit(), playerData.getDeaths());
            refreshFood.runTaskLater(Main.plugin, 500);
        } else if (item.getType() == Material.SPLASH_POTION && playerData.getKit() == Kit.ALCHEMIST) {
            removeProtection(player);

            BukkitRunnable refreshPotion = new RefreshItem(player, Kit.ALCHEMIST, item.clone(), playerData.getDeaths());
            refreshPotion.runTaskLater(Main.plugin, Kit.getPotionCooldown(((PotionMeta) item.getItemMeta()).getCustomEffects().get(0).getType()));
        } else if (item.getType() == Material.SNOWBALL && playerData.getKit() == Kit.SNOWMAN) {
            removeProtection(player);
            ItemStack item1 = item.clone();
            item1.setAmount(1);

            BukkitRunnable refreshSnowball = new RefreshItem(player, Kit.SNOWMAN, item1, playerData.getDeaths());
            refreshSnowball.runTaskLater(Main.plugin, 300);
        } else if (item.getType() == Material.ENDER_PEARL && playerData.getKit() == Kit.ENDERMAN) {
            BukkitRunnable refreshPearl = new RefreshItem(player, Kit.ENDERMAN, item.clone(), playerData.getDeaths());
            refreshPearl.runTaskLater(Main.plugin, 300);
        } else if (item.equals(ItemUtils.getLightningStaff(true)) && playerData.getKit() == Kit.ZEUS) {
            removeProtection(player);

            for (Entity entity : player.getNearbyEntities(10, 10, 10)) {
                if (entity instanceof LivingEntity && !entity.getUniqueId().equals(player.getUniqueId())) {
                    player.getWorld().strikeLightning(entity.getLocation());
                }
            }

            ItemStack discharged = ItemUtils.getLightningStaff(false);
            player.getInventory().setItem(event.getHand(), discharged);

            BukkitRunnable chargeStaff = new ReplaceItem(player, discharged, ItemUtils.getLightningStaff(true), playerData.getKit(), playerData.getDeaths());
            chargeStaff.runTaskLater(Main.plugin, 250);
        } else if (item.getType() == Material.FIRE_CHARGE && playerData.getKit() == Kit.PYROMANCER) {
            Fireball fireball = player.launchProjectile(Fireball.class);
            fireball.setShooter(player);
            fireball.setVelocity(player.getLocation().getDirection().multiply(2));
            fireball.setIsIncendiary(true);

            BukkitRunnable refreshFireball = new RefreshItem(player, Kit.PYROMANCER, item.clone(), playerData.getDeaths());
            refreshFireball.runTaskLater(Main.plugin, 100);
            item.setAmount(0);
        } else if (item.getType() == Material.TNT && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            Location clickedLocation = event.getClickedBlock().getLocation();
            Location location = new Location(clickedLocation.getWorld(), clickedLocation.getBlockX() + 0.5, clickedLocation.getBlockY() + 1.5, clickedLocation.getBlockZ() + 0.5);

            if (location.getWorld().getBlockAt(location).getType() != Material.AIR) return;

            removeProtection(player);
            player.getInventory().removeItem(ItemUtils.getItem(Material.TNT));
            TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
            tnt.setSource(player);
            tnt.setFuseTicks(20);

            BukkitRunnable refreshTNT = new RefreshItem(player, playerData.getKit(), ItemUtils.getItem(Material.TNT), playerData.getDeaths());
            refreshTNT.runTaskLater(Main.plugin, 300);
        }
    }

    @EventHandler
    public void onWorldJoin(PlayerChangedWorldEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) return;

        Player player = event.getPlayer();
        checkCombatLogging(player);

        if (player.getGameMode() == GameMode.SPECTATOR) {
            BukkitRunnable respawn = new PlayerRespawn(player, GameMode.ADVENTURE);
            respawn.runTaskTimer(Main.plugin, 0, 1);
        }

        if (!Main.plugin.getConfig().getBoolean("setKitOnJoinWorld")) return;

        KitSelection.applyKit(player, PlayerData.getPlayer(player.getUniqueId().toString()).getKit());
    }

    @EventHandler
    public void onServerJoin(PlayerJoinEvent event) {
        if (!Main.plugin.getConfig().getBoolean("setKitOnJoinServer") || !Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) return;

        Player player = event.getPlayer();
        KitSelection.applyKit(player, PlayerData.getPlayer(player.getUniqueId().toString()).getKit());
        checkCombatLogging(player);

        if (player.getGameMode() == GameMode.SPECTATOR) {
            BukkitRunnable respawn = new PlayerRespawn(player, GameMode.ADVENTURE);
            respawn.runTaskTimer(Main.plugin, 0, 20);
        }
    }

    @EventHandler
    public void onServerQuit(PlayerQuitEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) return;

        removeProtection(event.getPlayer());
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getEntity().getWorld().getName())) return;

        if (event.getEntity() instanceof Player) {
            removeProtection((Player) event.getEntity());
            Player player = ((Player) event.getEntity());
            if (PlayerData.getPlayer(player.getUniqueId().toString()).getKit() == Kit.BATMAN) {
                GrapplingHook.addPlayer(player, (Arrow) event.getProjectile());
            }
        }
    }

    @EventHandler
    public void snowball(ProjectileLaunchEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getEntity().getWorld().getName())) return;

        if (event.getEntity() instanceof Snowball) {
            event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.6));
        }
    }

    @EventHandler
    public void onExplode(ExplosionPrimeEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getEntity().getWorld().getName())) return;

        if (event.getEntity() instanceof TNTPrimed) {
            event.setCancelled(true);
            event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 3, false, false, event.getEntity());
            event.getEntity().remove();
        } else if (event.getEntity() instanceof Fireball) {
            event.setRadius(1.2f);
        }
    }

    @EventHandler
    public void onEnterPyroRadius(PlayerMoveEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) return;

        Player player = event.getPlayer();

        for (Entity entity : player.getNearbyEntities(1.5, 1.5, 1.5)) {
            if (entity instanceof Player) {
                Player player1 = (Player) entity;
                if (PlayerData.getPlayer(player1.getUniqueId().toString()).getKit() == Kit.PYROMANCER) {
                    player.setFireTicks(20);
                    break;
                }
            }
        }
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(event.getDamager().getWorld().getName())) return;

        if (event.getEntity() instanceof Player && Main.plugin.getConfig().getBoolean("showHealth")) {
            Player damager;
            if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            } else if ((event.getDamager() instanceof Arrow || event.getDamager() instanceof Fireball) && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
                damager = (Player) ((Projectile) event.getDamager()).getShooter();
            } else if (event.getDamager() instanceof TNTPrimed && ((TNTPrimed) event.getDamager()).getSource() instanceof Player) {
                damager = (Player) ((TNTPrimed) event.getDamager()).getSource();
            } else {
                damager = null;
            }

            if (damager != null) {
                int damagerHealth = (int) Math.ceil(damager.getHealth());
                int entityHealth = Math.max((int) Math.ceil((((Player) event.getEntity()).getHealth() - event.getFinalDamage())), 0);
                entityHealth = (entityHealth == 20 && event.getFinalDamage() > 0) ? 0 : entityHealth;

                String damagerMessage = ChatColor.YELLOW + event.getEntity().getName() + " is on " + ChatColor.RED + entityHealth + "hp";
                String entityMessage = ChatColor.YELLOW + damager.getName() + " is on " + ChatColor.RED + damagerHealth + "hp";

                damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(damagerMessage));
                ((Player) event.getEntity()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(entityMessage));
            }
        }

        if (event.getDamager() instanceof Player) {
            removeProtection((Player) event.getDamager());

            PlayerData.getPlayer(event.getDamager().getUniqueId().toString()).setCombatMode(true);
            BukkitRunnable combatModeUpdater = new CombatModeUpdate(((Player) event.getDamager()));
            combatModeUpdater.runTaskLaterAsynchronously(Main.plugin, 100);
        } else if (event.getDamager() instanceof Snowball && event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = ((LivingEntity) event.getEntity());
            if (((Snowball) event.getDamager()).getItem().equals(ItemUtils.getIceBall())) {
                entity.setVelocity(event.getDamager().getVelocity().multiply(0.5));
                entity.damage(7);
            } else {
                entity.setVelocity(event.getDamager().getVelocity().multiply(0.9));
                entity.damage(1);
            }
        }
    }

    private void removeProtection(Player player) {
        if (!Main.plugin.getConfig().getStringList("worlds").contains(player.getWorld().getName())) return;

        if (player.isInvulnerable()) {
            player.setInvulnerable(false);
            try {
                player.sendMessage(ChatColor.RED + "Spawn protection expired!");
            } catch (NullPointerException ignored) {}
        }
    }

    private void checkCombatLogging(Player player) {
        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());

        if (playerData.getCombatMode()) {
            playerData.setCombatMode(false);
            if (Main.plugin.getConfig().getBoolean("combatLoggingPenaltyEnabled")) {
                double speedPoints = playerData.getSpeedPoints() - Main.plugin.getConfig().getInt("combatLoggingPenaltyAmount");
                speedPoints = Math.max(speedPoints, 0);
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You lost " + (int) Math.floor(playerData.getSpeedPoints() - speedPoints) + " Speedpoints due to combat logging!");
                playerData.setSpeedPoints(speedPoints);
            }
        }
    }
}
