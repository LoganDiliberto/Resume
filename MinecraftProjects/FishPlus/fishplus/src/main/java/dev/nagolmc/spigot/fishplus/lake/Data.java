package dev.nagolmc.spigot.fishplus.lake;

import dev.nagolmc.spigot.fishplus.lake.Lake;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    public static final String prefix = "§bFishPlus §7| ";

    //public static HashMap<String, Schematic> schematics = new HashMap<>();
    public static HashMap<String, Lake> lakeList = new HashMap<>();
    public static File levels = new File("plugins/FishPlus/levels", "levels.yml");
    public static FileConfiguration level_cfg = YamlConfiguration.loadConfiguration(levels);

    public static File lakes = new File("plugins/FishPlus/lakes", "lakes.yml");
    public static FileConfiguration lakes_cfg = YamlConfiguration.loadConfiguration(lakes);

    public static void saveCfg() {
        try {
            lakes_cfg.save(lakes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean lakesFileContains(String s){
        if (lakes.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(lakes), s))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    if(line.contains(s)){
                        return true;
                    }
                }
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else {
            System.out.println("file doesn't exist");
            return false;
        }
    }

}