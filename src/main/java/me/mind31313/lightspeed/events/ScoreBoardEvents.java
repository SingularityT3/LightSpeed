package me.mind31313.lightspeed.events;

import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

@SuppressWarnings("deprecation")
public class ScoreBoardEvents implements Listener {
    @EventHandler
    public void onWorldJoin(PlayerChangedWorldEvent event) {
        if (Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) {
            update(event.getPlayer());
        } else {
            event.getPlayer().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    @EventHandler
    public void onServerJoin(PlayerJoinEvent event) {
        if (Main.plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getWorld().getName())) {
            update(event.getPlayer());
        }
    }

    public static void update(Player player) {
        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("LightSpeedBoard", "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + "LightSpeed");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = objective.getScore(ChatColor.GREEN + "----------------");
        score.setScore(6);
        Score score1 = objective.getScore("");
        score1.setScore(5);
        Score score2 = objective.getScore(ChatColor.AQUA + "Speedpoints: " + (int) Math.floor(playerData.getSpeedPoints()));
        score2.setScore(4);
        Score score3 = objective.getScore(ChatColor.GREEN + "Kit: " + playerData.getKit().getDisplayName());
        score3.setScore(3);
        Score score4 = objective.getScore(ChatColor.RED + "Kill Streak: " + playerData.getKillStreak());
        score4.setScore(2);
        Score score5 = objective.getScore(" ");
        score5.setScore(1);
        Score score6 = objective.getScore(ChatColor.YELLOW + Main.plugin.getConfig().getString("serverIP"));
        score6.setScore(0);
        player.setScoreboard(board);
    }
}
