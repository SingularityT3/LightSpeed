package me.mind31313.lightspeed.commands;

import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {
    private final String noPermissionMessage = ChatColor.RED + "You do not have permission to do that!";

    @Override
    @SuppressWarnings("unchecked")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("lightspeed")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /lightspeed <speedpoints | spawns | reload>");
            } else if (args[0].equalsIgnoreCase("speedpoints")) {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /lightspeed speedpoints <get | set> <player> <points>");
                } else if (args[1].equalsIgnoreCase("get") || args[1].equalsIgnoreCase("set")) {
                    Player player = sender.getServer().getPlayer(args[2]);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found!");
                    } else if (sender.hasPermission("lightspeed.speedpoints")) {
                        if (args[1].equalsIgnoreCase("get")) {
                            sender.sendMessage("Speedpoints: " + (int) Math.floor(PlayerData.getPlayer(player.getUniqueId().toString()).getSpeedPoints()));
                            return true;
                        }

                        if (args.length < 4) {
                            sender.sendMessage(ChatColor.RED + "Points not specified!");
                        } else {
                            try {
                                int points = Integer.parseInt(args[3]);
                                PlayerData.getPlayer(player.getUniqueId().toString()).setSpeedPoints(points);
                                sender.sendMessage(ChatColor.GREEN + "Updated!");
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.RED + "Points must be a number!");
                            }
                        }
                    } else {
                        sender.sendMessage(noPermissionMessage);
                    }
                }
            } else if (args[0].equalsIgnoreCase("spawns")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /lightspeed spawns <list | add | remove>");
                } else if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                } else if (args[1].equalsIgnoreCase("list")) {
                    if (!sender.hasPermission("lightspeed.spawns.list")) {
                        sender.sendMessage(noPermissionMessage);
                    } else {
                        StringBuilder message = new StringBuilder();
                        List<?> spawns = Main.plugin.getConfig().getList("spawns." + ((Player) sender).getWorld().getName());
                        if (spawns == null) {
                            message.append("None");
                        } else {
                            for (Object spawn : spawns) {
                                message.append(spawn.toString());
                                message.append("\n");
                            }
                        }
                        sender.sendMessage("Spawnpoints in this world:\n" + message.toString().trim());
                    }
                } else if (args[1].equalsIgnoreCase("add")) {
                    if (!sender.hasPermission("lightspeed.spawns.add")) {
                        sender.sendMessage(noPermissionMessage);
                    } else {
                        Player player = ((Player) sender);
                        List<List<Integer>> spawns = (List<List<Integer>>) Main.plugin.getConfig().getList("spawns." + player.getWorld().getName());
                        if (spawns == null) spawns = new ArrayList<>();
                        List<Integer> spawn = new ArrayList<>();
                        spawn.add(player.getLocation().getBlockX());
                        spawn.add(player.getLocation().getBlockY());
                        spawn.add(player.getLocation().getBlockZ());
                        spawns.add(spawn);
                        Main.plugin.getConfig().set("spawns." + player.getWorld().getName(), spawns);
                        Main.plugin.saveConfig();
                        player.sendMessage(ChatColor.GREEN + "Added " + spawn + " to spawns");
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("lightspeed.spawns.remove")) {
                        sender.sendMessage(noPermissionMessage);
                    } else {
                        Player player = ((Player) sender);
                        List<List<Integer>> spawns = (List<List<Integer>>) Main.plugin.getConfig().getList("spawns." + player.getWorld().getName());
                        if (spawns == null) spawns = new ArrayList<>();
                        List<Integer> playerLocation = new ArrayList<>();
                        playerLocation.add(player.getLocation().getBlockX());
                        playerLocation.add(player.getLocation().getBlockY());
                        playerLocation.add(player.getLocation().getBlockZ());
                        if (spawns.contains(playerLocation)) {
                            spawns.remove(playerLocation);
                            Main.plugin.saveConfig();
                            player.sendMessage(ChatColor.GREEN + "Removed " + playerLocation);
                        } else {
                            player.sendMessage(ChatColor.RED + "This is not a spawn location!");
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.isOp()) {
                    sender.sendMessage(noPermissionMessage);
                } else {
                    Main.plugin.reloadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Config reloaded");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid argument!");
            }
        }
        return true;
    }
}
