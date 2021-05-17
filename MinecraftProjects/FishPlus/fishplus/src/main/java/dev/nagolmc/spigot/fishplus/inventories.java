
package dev.nagolmc.spigot.fishplus;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import java.util.Random;
import net.milkbowl.vault.economy.Economy;
import dev.nagolmc.spigot.fishplus.sql.MySQL;
import dev.nagolmc.spigot.fishplus.sql.SQLGetter;
import dev.nagolmc.spigot.fishplus.allListen;
import net.md_5.bungee.api.ChatColor;
import java.sql.SQLException;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import dev.nagolmc.spigot.fishplus.itemWarehouse;

public class inventories {

    private App plugin;
    ItemStack currentItemHeld;
        ItemMeta currentMeta;

    public inventories(App plugin){
        this.plugin = plugin;
    }

    public void updateInventory(Player player){
        currentItemHeld = player.getEquipment().getItemInMainHand();
        currentMeta = currentItemHeld.getItemMeta();
    }

    public String getLast(String s){
        final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
        String input = "...";
        Matcher matcher = lastIntPattern.matcher(input);
        if (matcher.find()) {
            String someNumberStr = matcher.group(1);
            return someNumberStr;
        }
        return "0";
    }


    //The Upgrade a Rod Window
    public Inventory createUpgradeUI(Player player){
        Inventory inv = Bukkit.createInventory(player, 36, ChatColor.GOLD + "" + ChatColor.BOLD + "Upgrade");

        ItemStack item = new ItemStack(Material.SALMON);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        List<String> loreT = new ArrayList<String>();

        //Lure Upgrade
        currentItemHeld = player.getEquipment().getItemInMainHand();
        currentMeta = currentItemHeld.getItemMeta();
        loreT = currentMeta.getLore();

        int lureLevel = currentItemHeld.getEnchantmentLevel(Enchantment.LURE);
        int luckLevel = currentItemHeld.getEnchantmentLevel(Enchantment.LUCK);

        inv.setItem(31, currentItemHeld);

        meta.setDisplayName(ChatColor.AQUA +"Lure ");
        
        lore.add(ChatColor.RED+ String.valueOf(lureLevel)+ChatColor.GRAY+"/"+ChatColor.GREEN+"100");
        lore.add(ChatColor.GRAY + "Decreases the time it takes to catch a fish");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(10, item);

        //Luck Of The Sea
        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Luck Of The Sea ");
        item.setType(Material.RABBIT_FOOT);
        lore.add(ChatColor.RED+ String.valueOf(luckLevel)+ChatColor.GRAY+"/"+ChatColor.GREEN+"100");
        lore.add(ChatColor.GRAY + "Increases the chance of getting better fish");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(11, item);


        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"F-Token ");
        item.setType(Material.SUNFLOWER);
        lore.add(ChatColor.RED + String.valueOf(plugin.ftoken.getLevel("F-Token",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "100");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.ftoken.getLevel("F-Token",loreT)+1)*500) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increases the number of F-Tokens you get per catch");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(12, item);
        
        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Greed ");
        item.setType(Material.GOLD_INGOT);
        lore.add(ChatColor.RED + String.valueOf(plugin.greed.getLevel("Greed",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "100");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.greed.getLevel("Greed",loreT)+1)*250) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increases the value of fish");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(13, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Multi-Hook ");
        item.setType(Material.TRIPWIRE_HOOK);
        lore.add(ChatColor.RED + String.valueOf(plugin.multihook.getLevel("Multi-Hook",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "5");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.multihook.getLevel("Greed",loreT)+1)*5000) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Chance of catching more than 1 item at a time");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(14, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Treasure-Finder ");
        item.setType(Material.ENDER_CHEST);
        lore.add(ChatColor.RED + String.valueOf(plugin.treasurefinder.getLevel("Treasure-Finder",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "25");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.treasurefinder.getLevel("Treasure-Finder",loreT)+1)*250) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increases the chance of catching treasure");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(15, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Pirate ");
        item.setType(Material.GOLD_NUGGET);
        lore.add(ChatColor.RED + String.valueOf(plugin.pirate.getLevel("Pirate",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "10");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.pirate.getLevel("Greed",loreT)+1)*10000) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Find higher level treaure chests");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(16, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Shard-Hunter ");
        item.setType(Material.NETHER_STAR);
        lore.add(ChatColor.RED + String.valueOf(plugin.shardhunter.getLevel("Shard-Hunter",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "25");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.shardhunter.getLevel("Shard-Hunter",loreT)+1)*250) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increases the chance of catching shards");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(19, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Mob-Catcher ");
        item.setType(Material.EGG);
        lore.add(ChatColor.RED + String.valueOf(plugin.mobcatcher.getLevel("Mob-Catcher",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "25");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.mobcatcher.getLevel("Greed",loreT)+1)*250) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increases the chance of catching mobs");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(20, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Mob-Expert ");
        item.setType(Material.ZOMBIE_SPAWN_EGG);
        lore.add(ChatColor.RED + String.valueOf(plugin.mobexpert.getLevel("Mob-Expert",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "10");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.mobexpert.getLevel("Mob-Expert",loreT)+1)*250) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Catcher higher tiered mobs");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(21, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Key-Finder ");
        item.setType(Material.HEART_OF_THE_SEA);
        lore.add(ChatColor.RED + String.valueOf(plugin.keyfinder.getLevel("Key-Finder",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "10");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.keyfinder.getLevel("Key-Finder",loreT)+1)*1000) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increased chance of finding crate keys");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(22, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Sponge ");
        item.setType(Material.SPONGE);
        lore.add(ChatColor.RED + String.valueOf(plugin.sponge.getLevel("Sponge",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "1000");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.greed.getLevel("Greed",loreT)+1)*1000) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increased chance of catching sponges");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(23, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Thiccc ");
        item.setType(Material.PORKCHOP);
        lore.add(ChatColor.RED + String.valueOf(plugin.thiccc.getLevel("Thiccc",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "100");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.thiccc.getLevel("Thiccc",loreT)+1)*250) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increased odds of getting thicccer fish");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(24, item);

        lore.clear();
        meta.setDisplayName(ChatColor.AQUA +"Long-Boiii ");
        item.setType(Material.STICK);
        lore.add(ChatColor.RED + String.valueOf(plugin.longboiii.getLevel("Long-Boiii",loreT)) + ChatColor.GRAY + "/" + ChatColor.GREEN + "100");
        lore.add(ChatColor.WHITE + "Price: " + ChatColor.YELLOW + String.valueOf((plugin.longboiii.getLevel("Long-Boiii",loreT)+1)*250) + ChatColor.AQUA + " F-Tokens");
        lore.add(ChatColor.GRAY + "Increased odds of getting longer fish");
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(25, item);

        //Enchants to add
        //Greed (Incease the value of your fish)
        //Multi-Hook (Chance of catching more than 1 fish at a time)
        //F-Token (Get more F-Tokens per catch)
        //Treasure-Finder (Higher Chance of Finding Treasure)
        //Pirate (Find higher level treasures)
        //Shard-Hunter (Higher Chance of finding boss shards)
        //Mob-Catcher (Higher Chance of catching mobs)
        //Mob-Expert (Higher Chance of finding higher level mobs)
        //Key Finder (Increased Chance of finding Crate Keys)
        //Sponger (Increased Chance of finding Sponges)
        //ThicccFish (Increase the odds of getting thicccer fish)
        //LongBoiiii (Increase the odds of getting longer fish)

        lore.clear();
        lore.add("***********");
        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(0, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(1, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(2, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(3, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(4, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(5, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(6, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(7, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(8, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(9, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(17, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(18, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(26, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(27, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(28, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(29, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(30, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(32, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(33, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(34, item);

        meta.setDisplayName("Click on an enchant to uprade your rod!");
        item.setType(Material.BLUE_STAINED_GLASS_PANE);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(35, item);

        return inv;
    }

}