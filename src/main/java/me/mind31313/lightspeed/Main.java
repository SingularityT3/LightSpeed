package me.mind31313.lightspeed;

import me.mind31313.lightspeed.commands.KitCommand;
import me.mind31313.lightspeed.commands.MainCommand;
import me.mind31313.lightspeed.events.*;
import me.mind31313.lightspeed.tasks.DisableHunger;
import me.mind31313.lightspeed.tasks.KitEffectHandler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends JavaPlugin {
    private final String playerDataPath = getDataFolder().getPath() + "/playerdata.dat";

    public static Main plugin;

    public Main() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Kit.init();

        try {
            PlayerData.loadFromFile(playerDataPath);
            log("Loaded player data");
        } catch (FileNotFoundException e) {
            log(ChatColor.YELLOW, "Player data file not found");
        } catch (IOException | ClassNotFoundException e) {
            log(ChatColor.RED, "Failed to load player data from file!");
            e.printStackTrace();
        }

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new CoreEvents(), this);
        pluginManager.registerEvents(new InventoryEvents(), this);
        pluginManager.registerEvents(new MiscEvents(), this);

        if (getServer().getName().toLowerCase().contains("paper")) {
            pluginManager.registerEvents(new PaperDeathEvent(), this);
            log("Paper detected");
        } else {
            pluginManager.registerEvents(new SpigotDeathEvent(), this);
        }

        getCommand("lightspeed").setExecutor(new MainCommand());
        getCommand("kit").setExecutor(new KitCommand());

        BukkitRunnable kitEffects = new KitEffectHandler();
        kitEffects.runTaskTimer(this, 100, 60);

        if (getConfig().getBoolean("disableHunger")) {
            BukkitRunnable disableHunger = new DisableHunger();
            disableHunger.runTaskTimer(this, 100, 20);
        }

        log("Plugin enabled");
    }

    @Override
    public void onDisable() {
        try {
            PlayerData.saveToFile(playerDataPath);
            log("Saved player data");
        } catch (IOException e) {
            log(ChatColor.RED, "Failed to save player data to file!" );
            e.printStackTrace();
        }

        log("Plugin disabled");
    }

    private void log(String message) {
        log(ChatColor.RESET, message);
    }

    private void log(ChatColor color, String message) {
        getServer().getConsoleSender().sendMessage(color + "[LightSpeed] " + message);
    }
}
