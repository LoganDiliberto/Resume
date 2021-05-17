package dev.nagolmc.spigot.fishplus;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.sql.SQLException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import dev.nagolmc.spigot.fishplus.inventories;
import dev.nagolmc.spigot.fishplus.sql.MySQL;
import dev.nagolmc.spigot.fishplus.sql.SQLGetter;
import dev.nagolmc.spigot.fishplus.allListen;
import dev.nagolmc.spigot.fishplus.itemWarehouse;

//This Class contains all of the create/get methods for all items/rods but not including FISH
public class itemWarehouse {

    public itemWarehouse(){
        //Create overused variables
    }

    //Return the starter rod
    public ItemStack getStarterRod(){
        ItemStack starter_rod = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = starter_rod.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Starter Rod");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "The rod for a beginner");
        meta.setLore(lore);

        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        //meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setUnbreakable(true);

        starter_rod.setItemMeta(meta);
        
        return starter_rod;
    }

    //Return a Common Treasure Bag
    public ItemStack getCommonTreasureBag(){

        ItemStack common_treasure = new ItemStack(Material.ENDER_CHEST);
        ItemMeta meta  = common_treasure.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "Common Treasure");
        List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GRAY + "Right click to open and get Treasure!");
        meta.setLore(lore);

        common_treasure.setItemMeta(meta);

        return common_treasure;
    }

    public ItemStack[] getCommonTreasureLootTable(){

        ItemStack[] loot = new ItemStack[10];
        
        for(int i = 0; i < loot.length; i++){
            loot[i] = new ItemStack(Material.EMERALD, 8);
        }

        return loot;
    }

    public ItemStack getChickenShard(){

        ItemStack shard = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta  = shard.getItemMeta();

        meta.setDisplayName(ChatColor.BLUE + "Chicken Shard");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Combine 9 in a crafting table to get a complete stone!");
        meta.setLore(lore);

        shard.setItemMeta(meta);

        return shard;
    }
 
}