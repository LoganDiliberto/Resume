package dev.nagolmc.spigot.fishplus.lake;

//import com.godminer.main.Main;
//import com.godminer.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class LakeManager {

    public boolean hasLake(Player player) {
        //Bukkit.getConsoleSender().sendMessage("File Contains? " + Data.lakesFileContains("7bf9878b-7d2e-425b-8678-d6673fa71573"));
        //Bukkit.getConsoleSender().sendMessage("Members. found: " + Data.lakes_cfg.get("Lakes." + player.getUniqueId().toString() + ".Members"));
        //Bukkit.getConsoleSender().sendMessage("Lakes. found: " + Data.lakes_cfg.get("Lakes." + player.getUniqueId().toString()));
        return Data.lakes_cfg.contains("Lakes." + player.getUniqueId().toString()) || Data.lakes_cfg.contains("Members." + player.getUniqueId().toString());
    }

    public void loadLakes() {
        if(Data.lakes_cfg.contains("Lakes")) {
            for (String lakes : Data.lakes_cfg.getConfigurationSection("Lakes").getKeys(false)) {

                String owner = lakes;

                int bank = Data.lakes_cfg.getInt("Lakes." + lakes + ".Bank");
                int level = Data.lakes_cfg.getInt("Lakes." + lakes + ".Level");
                ArrayList<String> member = (ArrayList<String>) Data.lakes_cfg.getList("Lakes." + lakes + ".Members");

                Lake lake = new Lake(bank, level, member, owner);
                System.out.println(owner + "   " + lake);
                Data.lakeList.put(owner, lake);
            }
        }
        Bukkit.getConsoleSender().sendMessage(Data.prefix + "Loaded all §blakes §7successfully!");
    }

    public void loadLake(String owner) {
        if(Data.lakes_cfg.contains("Lakes." + owner)) {
            /*int middleX, middleY, middleZ, spawnX, spawnY, spawnZ;
            spawnX = Data.mines_cfg.getInt("Mines." + owner + ".Spawn.X");
            spawnY = Data.mines_cfg.getInt("Mines." + owner + ".Spawn.Y");
            spawnZ = Data.mines_cfg.getInt("Mines." + owner + ".Spawn.Z");
            middleX = Data.mines_cfg.getInt("Mines." + owner + ".Middle.X");
            middleY = Data.mines_cfg.getInt("Mines." + owner + ".Middle.Y");
            middleZ = Data.mines_cfg.getInt("Mines." + owner + ".Middle.Z");
            World world = Bukkit.getWorld(Data.minesconf_cfg.getString("MineSetup.World"));
            Location spawn = new Location(world, spawnX, spawnY, spawnZ);
            Location middle = new Location(world, middleX, middleY, middleZ);*/

            int bank = Data.lakes_cfg.getInt("Lakes." + owner + ".Bank");
            int level = Data.lakes_cfg.getInt("Lakes." + owner + ".Level");
            ArrayList<String> member = (ArrayList<String>) Data.lakes_cfg.getList("Lakes." + owner + ".Members");

            Lake lake = new Lake(bank, level, member, owner);
            Data.lakeList.put(owner, lake);
        }
    }


    public Lake getLake(Player player) {
        if(Data.lakeList.containsKey(player.getUniqueId().toString())) {
            return Data.lakeList.get(player.getUniqueId().toString());
        }
        return null;
    }

    public Lake getLakeFromMember(Player player) {
        for(Lake lake : Data.lakeList.values()) {
            if(lake.getMember().contains(player.getUniqueId().toString())) {
                return lake;
            }
        }
        return null;
    }

    public boolean isMember(Player player) {
        for(Lake lake : Data.lakeList.values()) {
            if(lake.getMember().contains(player.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }




}