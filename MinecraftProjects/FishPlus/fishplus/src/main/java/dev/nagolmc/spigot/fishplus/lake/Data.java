package dev.nagolmc.spigot.fishplus.lake;

import dev.nagolmc.spigot.fishplus.lake.Lake;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {

    public static final String prefix = "§bFishPlus §7| ";

    public static HashMap<String, Schematic> schematics = new HashMap<>();
    public static HashMap<String, Mine> mineList = new HashMap<>();

    public static File lakes = new File("plugins/FishPlus/lakes", "lakes.yml");
    public static FileConfiguration mines_cfg = YamlConfiguration.loadConfiguration(mines);

    public static File levels = new File("plugins/FishPlus/levels", "levels.yml");
    public static FileConfiguration level_cfg = YamlConfiguration.loadConfiguration(levels);

    public static void saveCfg() {
        try {
            mines_cfg.save(mines);
            level_cfg.save(levels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}