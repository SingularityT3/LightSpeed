package me.mind31313.lightspeed;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerData implements Serializable {
    private final String UUID;
    private double speedPoints;
    private final List<Kit> kitsPurchased;
    private Kit kit;
    private double speed;
    private boolean combatMode;
    private int kills;
    private int deaths;
    private int streak;
    private int highestStreak;

    private static HashMap<String, PlayerData> map = new HashMap<>();

    private static void addPlayer(String uuid, PlayerData playerData) {
        map.putIfAbsent(uuid, playerData);
    }

    public PlayerData(String UUID) {
        this.UUID = UUID;
        speedPoints = 0;
        kitsPurchased = new ArrayList<>();
        kitsPurchased.add(Kit.DEFAULT);
        kit = Kit.DEFAULT;
        speed = 0;
        combatMode = false;
        kills = 0;
        deaths = 0;
        addPlayer(UUID, this);
    }

    public static PlayerData getPlayer(String uuid) {
        return map.getOrDefault(uuid, new PlayerData(uuid));
    }

    public static void saveToFile(String path) throws IOException {
        FileOutputStream f = new FileOutputStream(path);
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(map);
        o.close();
        f.close();
    }

    @SuppressWarnings("unchecked")
    public static void loadFromFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream f = new FileInputStream(path);
        ObjectInputStream o = new ObjectInputStream(f);
        HashMap<String, PlayerData> loadedMap = (HashMap<String, PlayerData>) o.readObject();
        o.close();
        f.close();
        map = loadedMap;
    }

    public String getUUID() {
        return UUID;
    }

    public double getSpeedPoints() {
        return speedPoints;
    }

    public void setSpeedPoints(double speedPoints) {
        this.speedPoints = speedPoints;
    }

    public List<Kit> getKitsPurchased() {
        return kitsPurchased;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Kit getKit() {
        return kit;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        if (kills > this.kills) {
            for (int i = 0; i < kills - this.kills; i++) {
                addStreak();
            }
        }
        this.kills = kills;
    }

    private void addStreak() {
        this.streak++;
        highestStreak = Math.max(streak, highestStreak);
    }
    
    public int getHighestStreak() {
        return highestStreak;
    }

    public int getKillStreak() {
        return streak;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        if (deaths > this.deaths)
            streak = 0;
        this.deaths = deaths;
    }

    public boolean getCombatMode() {
        return combatMode;
    }

    public void setCombatMode(boolean combatMode) {
        this.combatMode = combatMode;
    }
}
