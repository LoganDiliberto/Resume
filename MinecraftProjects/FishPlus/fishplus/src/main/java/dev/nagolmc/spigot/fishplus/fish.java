package dev.nagolmc.spigot.fishplus;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
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
import org.bukkit.event.block.BlockPlaceEvent;
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
import java.util.Random;
import net.milkbowl.vault.economy.Economy;
import dev.nagolmc.spigot.fishplus.sql.MySQL;
import dev.nagolmc.spigot.fishplus.sql.SQLGetter;
import dev.nagolmc.spigot.fishplus.allListen;
import net.md_5.bungee.api.ChatColor;
import java.sql.SQLException;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;

public class fish{

    private App plugin;

    //Create Possible Fish List
    List<String> fishNameList = new ArrayList<String>();
    List<String> fishRarityList = new ArrayList<String>();

    //Setup ItemStack variables
    ItemStack currentFish = new ItemStack(Material.SALMON);
    ItemMeta metaFish = currentFish.getItemMeta();
    List<String> lore = new ArrayList<String>();

    public fish(App plugin){

        this.plugin = plugin;

        //Creating Some possible fish to catch

        //Common Fish Names
        fishNameList.add("Trout");
        fishNameList.add("Bass");
        fishNameList.add("Sunfish");

        for(int i = 0; i < 3; i++){
            fishRarityList.add(ChatColor.GREEN +"Common ");
        }
        
        //Rare
        fishNameList.add("Catfish");
        fishNameList.add("Northern Pike");
        fishNameList.add("Walleye");

        for(int i = 0; i < 3; i++){
            fishRarityList.add(ChatColor.BLUE +"Rare ");
        }
        //Epic Fish
        fishNameList.add("Flounder");
        fishNameList.add("Jelly Fish");
        fishNameList.add("Micro Shrimp");

        for(int i = 0; i < 3; i++){
            fishRarityList.add(ChatColor.DARK_PURPLE +"Epic ");
        }

        //Unique
        fishNameList.add("Sword Fish");
        fishNameList.add("Shark");
        fishNameList.add("Swatooth");

        for(int i = 0; i < 3; i++){
            fishRarityList.add(ChatColor.LIGHT_PURPLE +"Unique ");
        }

        //Legendary Fish
        fishNameList.add("King Salmon");
        fishNameList.add("Megladon");
        fishNameList.add("Kraken");

        for(int i = 0; i < 3; i++){
            fishRarityList.add(ChatColor.GOLD +"Legendary ");
        }
        //Mythical Fish 
    }

    public ItemStack getFish(Player player, int luck){
        
        int rand = getRandomNumber(0,15);
        int money = getRandomNumber(50, 200);
        int length = getRandomNumber(1, 10);
        int weight = getRandomNumber(1, 50);
        ChatColor rarityColor = ChatColor.RED;

        if(fishRarityList.get(rand).contains("Common")){
            rarityColor = ChatColor.GREEN;
        }
        if(fishRarityList.get(rand).contains("Rare")){
            rarityColor = ChatColor.BLUE;
        }
        if(fishRarityList.get(rand).contains("Epic")){
            rarityColor = ChatColor.DARK_PURPLE;
        }
        if(fishRarityList.get(rand).contains("Unique")){
            rarityColor = ChatColor.LIGHT_PURPLE;
        }
        if(fishRarityList.get(rand).contains("Legendary")){
            rarityColor = ChatColor.GOLD;
        }


        //Create fish
        lore.clear();
        metaFish.setDisplayName(ChatColor.BOLD + "" + rarityColor + fishNameList.get(rand));//DisplayName
        lore.add(ChatColor.GRAY + "Rarity: " + fishRarityList.get(rand));//Add Rarity
        lore.add(ChatColor.GRAY + "Length: " + ChatColor.WHITE + String.valueOf(length));//Length
        lore.add(ChatColor.GRAY + "Weight: " + ChatColor.WHITE + String.valueOf(weight));//Weight
        lore.add(ChatColor.GRAY + "Worth " + ChatColor.GREEN + String.valueOf(money));//Money
        lore.add("");
        lore.add(ChatColor.GRAY + "Caught By: " + player.getDisplayName());//Set the Caught By
        metaFish.setLore(lore);//Set the lore on the fish
        currentFish.setItemMeta(metaFish);

        //TODO Incease Money Based on Rarity

        //TODO Change random Length/Weight based on Fish

        //TODO Change the Odds of fishing up a fish based on luck
        
        //Give the player the fish and a message
        player.sendMessage("You caught a " + currentFish.getItemMeta().getDisplayName() + ChatColor.RED + " $" + String.valueOf(money));
        return currentFish;
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public String getWeight(ItemStack fish){
        return "100";
    }

    public String getLength(ItemStack fish){
        return "100";
    }

    public String getRarity(ItemStack fish){
        return "Common";
    }

    public String getCaughtBy(ItemStack fish){
        return "Blaze5959 :D ";
    }

    public int getFishValue(ItemStack fish){
        return 1;
    }
}