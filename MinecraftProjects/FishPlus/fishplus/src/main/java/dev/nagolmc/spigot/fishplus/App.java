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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

import dev.nagolmc.spigot.fishplus.inventories;
import dev.nagolmc.spigot.fishplus.sql.MySQL;
import dev.nagolmc.spigot.fishplus.sql.SQLGetter;
import dev.nagolmc.spigot.fishplus.allListen;
import dev.nagolmc.spigot.fishplus.itemWarehouse;
import dev.nagolmc.spigot.fishplus.enchants.CustomEnchants;
import dev.nagolmc.spigot.fishplus.enchants.FTOKEN;
import dev.nagolmc.spigot.fishplus.enchants.GREED;
import dev.nagolmc.spigot.fishplus.enchants.KEYFINDER;
import dev.nagolmc.spigot.fishplus.enchants.LONGBOIII;
import dev.nagolmc.spigot.fishplus.enchants.MOBCATCHER;
import dev.nagolmc.spigot.fishplus.enchants.MOBEXPERT;
import dev.nagolmc.spigot.fishplus.enchants.MULTIHOOK;
import dev.nagolmc.spigot.fishplus.enchants.PIRATE;
import dev.nagolmc.spigot.fishplus.enchants.SHARDHUNTER;
import dev.nagolmc.spigot.fishplus.enchants.SPONGE;
import dev.nagolmc.spigot.fishplus.enchants.THICCC;
import dev.nagolmc.spigot.fishplus.enchants.TREASUREFINDER;
//import dev.nagolmc.spigot.fishplus.scoreboard;

public class App extends JavaPlugin{
    
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    //Starting Variables
    Economy eco = null;
    public MySQL SQL;
    public SQLGetter data;
    public itemWarehouse warehouse;
    public inventories inv;
    public treasure crates;
    public fish fishy;
    public FTOKEN ftoken;
    public GREED greed;
    public KEYFINDER keyfinder;
    public LONGBOIII longboiii;
    public MOBCATCHER mobcatcher;
    public MOBEXPERT mobexpert;
    public PIRATE pirate;
    public SHARDHUNTER shardhunter;
    public SPONGE sponge;
    public THICCC thiccc;
    public TREASUREFINDER treasurefinder;
    public MULTIHOOK multihook;
    
    //Log to console with simple print();
    public void print(String text){
        console.sendMessage(ChatColor.AQUA+text);
        //It will print in sserver's console.
    }
    
    //When the plugin Starts Up
    @Override
    public void onEnable() {

        //Initialize Classes
        warehouse = new itemWarehouse();
        inv = new inventories(this);
        crates = new treasure();
        fishy = new fish(this);
        ftoken = new FTOKEN();
        greed = new GREED();
        keyfinder = new KEYFINDER();
        longboiii = new LONGBOIII();
        mobcatcher = new MOBCATCHER();
        mobexpert = new MOBEXPERT();
        pirate = new PIRATE();
        shardhunter = new SHARDHUNTER();
        sponge = new SPONGE();
        thiccc = new THICCC();
        treasurefinder = new TREASUREFINDER();
        multihook = new MULTIHOOK();

        //SQL Setup
        this.SQL = new MySQL();
        this.data = new SQLGetter(this);

        try{
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e){
            //Login Info Incorrect
            Bukkit.getLogger().info("Database is not connected");
        }

        if(SQL.isConnected()){
            Bukkit.getLogger().info("Database is connected");
            data.createTable();
        }

        //Add Custom Recipies
        //Bukkit.addRecipe(getRecipe());

        //Economy Setup
        if(!setupEconomy()){
            print("You need vault and an Economy Plugin installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Wakey Wakey Listeners
        getServer().getPluginManager().registerEvents(new allListen(this), this);
        getLogger().info("Hello, SpigotMC this is FishKing here!");

        //Allow Listener Events/Registered Events
        //this.getServer().getPluginManager().registerEvents(this, this);

        if(!Bukkit.getOnlinePlayers().isEmpty()){
            for(Player online : Bukkit.getOnlinePlayers()){
                createBoard(online);
            }
        }
    }

    //When the plugin shuts off
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!"); 
    }

    public ShapedRecipe getRecipe(){
        ItemStack item = new ItemStack(Material.DIAMOND);

        NamespacedKey key = new NamespacedKey(this, "nether_star");

        ShapedRecipe recipe = new ShapedRecipe(key,item);
        recipe.shape(" D ", "DDD", " D ");

        recipe.setIngredient('D', Material.DIRT);

        return recipe;
    }

    //Economy Setup
    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null){
            eco = economyProvider.getProvider();
        }
        return (eco != null);
    }

    //On a '/' command of whatever the label is
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(label.equalsIgnoreCase("hello")){
            if(sender instanceof Player){
                //Player
                Player player = (Player) sender;
                if (player.hasPermission("hello.use")){
                    player.sendMessage(ChatColor.AQUA + "Hey welcome to the server!");
                    return true;
                }
                player.sendMessage(ChatColor.RED + "You don't have permission!");
                return true;
            }
            else{
                //console
                sender.sendMessage("Hey Console!");
                return true;
            }
        }
        
        if(label.equalsIgnoreCase("getrod")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(player.getInventory().firstEmpty() == -1){//Inventory Full
                    Location loc = player.getLocation();
                    World world = player.getWorld();
                    world.dropItemNaturally(loc, warehouse.getStarterRod());
                    player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "Your inventory was full, item dropped");
                    return true;
                }
                player.getInventory().addItem(warehouse.getStarterRod());
                return true;
            }
            else{
                sender.sendMessage("Consoles can't fish :(");
                return true;
            }
        }

        if(label.equalsIgnoreCase("getcommontreasure")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(player.getInventory().firstEmpty() == -1){//Inventory Full
                    Location loc = player.getLocation();
                    World world = player.getWorld();
                    world.dropItemNaturally(loc, warehouse.getCommonTreasureBag());
                    player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "Your inventory was full, item dropped");
                    return true;
                }
                player.getInventory().addItem(warehouse.getCommonTreasureBag());
                return true;
            }
            else{
                sender.sendMessage("Consoles can't fish :(");
                return true;
            }
        }
        if(label.equalsIgnoreCase("upgrade")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)){
                    player.openInventory(inv.createUpgradeUI(player));
                }else{
                    player.sendMessage("Try /upgrade while holding a Fishing Rod");
                }
            }
        }
        if(label.equalsIgnoreCase("gettestrod")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                List<String> lore = new ArrayList<String>();
                ItemStack test_rod = warehouse.getStarterRod();

                ItemMeta meta = test_rod.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Test Rod");

                lore.add( ChatColor.RESET + "" + ChatColor.GRAY + "F-Token 1");
                meta.setLore(lore);
                test_rod.setItemMeta(meta);
                player.getInventory().addItem(test_rod);
                return true;
            }
            return true;
        }
        return true;
    }

    //Create the scoreboard for each player
    public void createBoard(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective(ChatColor.GREEN+"FishKing","dumy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = obj.getScore(ChatColor.BLUE + "=-=-=-=-=-=-=-=-=");
        score.setScore(4);

        Score score5 = obj.getScore(ChatColor.AQUA + "Current Fokens: " + ChatColor.RED + String.valueOf(data.getFokens(player.getUniqueId())));
        score5.setScore(4);
        Score score4 = obj.getScore(ChatColor.AQUA + "Current Bal: " + ChatColor.GREEN+ String.valueOf(eco.getBalance(player)));
        score4.setScore(3);
        Score score2 = obj.getScore(ChatColor.AQUA + "Current XP: " + ChatColor.DARK_AQUA + String.valueOf(player.getTotalExperience()));
        score2.setScore(2);
        Score score3 = obj.getScore(ChatColor.BLUE + "=-=-=-=-=-=-=-=-=");
        score3.setScore(1);

        player.setScoreboard(board);
    }
}