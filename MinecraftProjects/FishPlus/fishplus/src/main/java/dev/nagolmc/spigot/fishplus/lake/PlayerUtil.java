package dev.nagolmc.spigot.fishplus.lake;

import org.bukkit.Bukkit;
import java.util.UUID;

public class PlayerUtil {

    public String getNameFromUUID(UUID uuid) {
        if(Bukkit.getPlayer(uuid) != null) {
            return Bukkit.getPlayer(uuid).getName();
        }else {
            return Bukkit.getOfflinePlayer(uuid).getName();
        }
    }

    public UUID getUUIDFromName(String name) {
        if(Bukkit.getPlayer(name) != null) {
            return Bukkit.getPlayer(name).getUniqueId();
        }else {
            return Bukkit.getOfflinePlayer(name).getUniqueId();
        }
    }

}