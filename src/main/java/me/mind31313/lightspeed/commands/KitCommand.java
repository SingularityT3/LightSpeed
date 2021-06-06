package me.mind31313.lightspeed.commands;

import me.mind31313.lightspeed.Kit;
import me.mind31313.lightspeed.Main;
import me.mind31313.lightspeed.PlayerData;
import me.mind31313.lightspeed.inventories.KitSelection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kit")) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only players can use that command!");
                    return true;
                } if (!Main.plugin.getConfig().getStringList("worlds").contains(((Player) sender).getWorld().getName())) {
                    sender.sendMessage(ChatColor.RED + "You cannot do that here!");
                    return true;
                } else if (PlayerData.getPlayer(((Player) sender).getUniqueId().toString()).getCombatMode()) {
                    sender.sendMessage(ChatColor.RED + "You cannot change kit while in combat");
                    return true;
                }

                KitSelection kits = new KitSelection();
                Player player = (Player) sender;
                kits.init(player);
                player.openInventory(kits.getInventory());
            } else if (args[0].equalsIgnoreCase("set")) {
                if (!sender.hasPermission("lightspeed.kit.set")) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                    return true;
                }

                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /kit set <player> <kit>");
                    return true;
                }

                Player player = sender.getServer().getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }

                Kit kit = getKitFromString(args[2]);
                if (kit == null) {
                    sender.sendMessage(ChatColor.RED + "Kit not found!");
                    return true;
                }

                if (PlayerData.getPlayer(player.getUniqueId().toString()).getKitsPurchased().contains(kit)) {
                    KitSelection.applyKit(player, kit);
                    sender.sendMessage(ChatColor.GREEN + "Kit applied!");
                } else {
                    sender.sendMessage(ChatColor.RED + "That player does not have this kit");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("grant") || args[0].equalsIgnoreCase("revoke")) {
                boolean action = args[0].equalsIgnoreCase("grant");
                if ((!sender.hasPermission("lightspeed.kit.grant") && action) || (!sender.hasPermission("lightspeed.kit.revoke") && !action)) {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                    return true;
                }

                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /kit " + (action ? "grant" : "revoke") + " <player> <kit>");
                    return true;
                }

                Player player = sender.getServer().getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }

                Kit kit = getKitFromString(args[2]);
                if (kit == null) {
                    if (!args[2].equals("*")) {
                        sender.sendMessage(ChatColor.RED + "Kit not found!");
                        return true;
                    } else {
                        for (Kit kit1 : Kit.values()) {
                            if (kit1 == Kit.DEFAULT) continue;
                            kitGrantRevoke(action, player, kit1);
                            sender.sendMessage(ChatColor.GREEN + "Kit " + kit1.getDisplayName() + (action ? " granted" : " revoked") + "!");
                        }
                        return true;
                    }
                }

                kitGrantRevoke(action, player, kit);
                sender.sendMessage(ChatColor.GREEN + "Kit " + kit.getDisplayName() + (action ? " granted" : " revoked") + "!");
            }
        }
        return true;
    }

    private Kit getKitFromString(String s) {
        for (Kit kit : Kit.values()) {
            if (kit.getDisplayName().equalsIgnoreCase(s)) {
                return kit;
            }
        }
        return null;
    }

    private void kitGrantRevoke(boolean action, Player player, Kit kit) {
        PlayerData playerData = PlayerData.getPlayer(player.getUniqueId().toString());
        if (action) {
            playerData.getKitsPurchased().add(kit);
        } else {
            playerData.getKitsPurchased().remove(kit);
            playerData.setKit(Kit.DEFAULT);
        }
    }
}
