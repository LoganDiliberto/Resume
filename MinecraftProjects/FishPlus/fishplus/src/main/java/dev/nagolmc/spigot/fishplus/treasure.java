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
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import dev.nagolmc.spigot.fishplus.inventories;
import dev.nagolmc.spigot.fishplus.sql.MySQL;
import dev.nagolmc.spigot.fishplus.sql.SQLGetter;
import dev.nagolmc.spigot.fishplus.allListen;
import dev.nagolmc.spigot.fishplus.itemWarehouse;

import java.util.concurrent.ThreadLocalRandom;

public class treasure {

    List<Inventory> invs = new ArrayList<Inventory>();
    public static ItemStack[] contents;
    private int itemIndex = 0;

    public void shuffle(Inventory inv, ItemStack[] loot){

        List<String> lore = new ArrayList<String>();
        ItemStack glassPane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta glassPaneMeta = glassPane.getItemMeta();
        glassPaneMeta.setDisplayName("Let's Get Rollin!");
        lore.add("***********");
        glassPaneMeta.setLore(lore);
        glassPane.setItemMeta(glassPaneMeta);

        if(contents == null){
            ItemStack[] items = loot;
            contents = items;
        }

        int startingIndex = ThreadLocalRandom.current().nextInt(contents.length);
        
        //Add Glass Panes
        for(int index = 0; index < 27; index++){
            inv.setItem(index,glassPane);
        }

        for(int index = 0; index < startingIndex; index++){
            for(int itemstacks = 9; itemstacks < 18; itemstacks++){
                inv.setItem(itemstacks, contents[(itemstacks + itemIndex) % contents.length]);
            }
            itemIndex++;
        }

        //Customize
        ItemStack item = new ItemStack(Material.HOPPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "|");
        item.setItemMeta(meta);
        inv.setItem(4,item);
    }

    public void spin(final Player player, ItemStack[] treasure){

        Inventory inv = Bukkit.createInventory(null, 27, "GoodLuck!");
        shuffle(inv, treasure);
        invs.add(inv);
        player.openInventory(inv);

        Random r = new Random();
        double seconds = 7.0 + (12.0 - 7) * r.nextDouble();

        new BukkitRunnable(){
            //Variables run 1 time
            double delay = 0;
            int ticks = 0;
            boolean done = false;

            public void run(){
                if(done){
                    return;//When done is set to true then stop spinning
                }
                ticks++;
                delay += 1/ (20 *seconds);//
                if(ticks > delay*10){
                    ticks = 0;
                    
                    //Delay is small so shuffle item
                    //The 9 and 18(17) are the item slots where the items are
                    for(int itemstacks = 9; itemstacks < 18; itemstacks++){
                        inv.setItem(itemstacks, contents[(itemstacks + itemIndex) % contents.length]);
                    }
                    itemIndex++;//Move to next item

                    if(delay >= 0.5){
                        done = true;
                        new BukkitRunnable(){
                            //1 second equals 20 ticks
                            //Give out the winning prize
                            public void run(){
                                ItemStack item = inv.getItem(13);
                                player.getInventory().addItem(item);//TODO check if inventory is full drop item if it is
                                player.updateInventory();
                                player.closeInventory();
                                cancel();
                            }
                        }.runTaskLater(App.getPlugin(App.class), 50);
                        cancel();
                    }

                }   
            }
        }.runTaskTimer(App.getPlugin(App.class),0,1);//Run this task 0 (Start the BUkkitRunable right now) 1(Do the run feature every 1 tick)
    }
}