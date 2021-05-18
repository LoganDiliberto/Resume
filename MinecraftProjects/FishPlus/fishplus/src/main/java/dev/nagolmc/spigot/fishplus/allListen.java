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
import org.bukkit.entity.EntityType;
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
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.NamespacedKey;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
//import org.bukkit.command;



public class allListen implements Listener{

    private App plugin;
    private static final String[] NUMERALS = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
    public BukkitAPIHelper  mmhelper = new BukkitAPIHelper();
    public ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public allListen(App plugin){
        this.plugin = plugin;
        
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        //Give the player their scoreboard on join
        plugin.createBoard(event.getPlayer());
        //Create player in the SQL Database if not already there
        plugin.data.createPlayer(event.getPlayer());
    }

    @EventHandler
    public void onFish(PlayerFishEvent event){
        //Get Player
        Player player = (Player) event.getPlayer();
        ItemStack currentItemHeld = player.getEquipment().getItemInMainHand();
        ItemMeta meta = currentItemHeld.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore = meta.getLore();

        if(event.getState() == State.CAUGHT_FISH){

            //Remove Vanilla Drops
            event.getCaught().remove();
            ItemStack fish;

            //Random Numbers for Catch Chances
            int catchTreasureChance = getRandomNumber(1,1000);
            int catchMobChance = getRandomNumber(1,1000);
            int catchSpongeChance = getRandomNumber(1,1000);
            int catchShardChance = getRandomNumber(1,1000);
            int ftokenNumber = getRandomNumber(35,100000);
            boolean spongeCaught = false;
            boolean shardCaught = false;

            //Treasure-Finder Enchant
            catchTreasureChance += 5*(plugin.treasurefinder.getLevel("Treasure-Finder", lore));//Replace 1 with Treasure-FinderLevel Max of 10
            //Mob-Catcher Enchant
            catchMobChance += 5*(plugin.mobcatcher.getLevel("Mob-Catcher", lore));//Replace 1 with Treasure-FinderLevel Max of 10
            //Sponge Enchant
            catchSpongeChance += 1*(plugin.sponge.getLevel("Sponge", lore));//Replace 1 with Sponge Max of 100
            //Shard-Hunter Enchant
            catchShardChance += 1*(plugin.shardhunter.getLevel("Shard-Hunter", lore));//Replace 1 with Sponge Max of 100
            //F-Token Enchant 
            ftokenNumber += getRandomNumber(1,10)*(plugin.ftoken.getLevel("F-Token", lore));//Replace 1 with F-Token Level
            if(plugin.ftoken.getLevel("F-Token", lore) > 0){
                player.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "F-Token Bonus + " + String.valueOf(ftokenNumber));
            }
            plugin.data.addFokens(player.getUniqueId(),ftokenNumber);

            //Default 2.5% chance of getting Boss Shard
            if(catchShardChance > 975 ){
                player.getInventory().addItem(plugin.warehouse.getChickenShard());
            }
            //Default 2.5% chance of getting Sponge
            else if(catchSpongeChance > 975){
                player.getInventory().addItem( new ItemStack(Material.SPONGE));
            }
            //Default 5% chance of getting a Mob
            else if(catchMobChance > 950){
                //SkeletalKnight
                Location loc = player.getLocation();
                loc.setY(loc.getY()+1);

                //loc.getWorld().spawnEntity(loc, EntityType.COW);
                String command  = "mm mobs spawn AngrySquid 1 world,"+ loc.getX() + "," + loc.getY() + "," + loc.getZ();
                player.sendMessage(command);
                Bukkit.dispatchCommand(console, command);
            }
            //Default 2.5% chance of getting Treasure
            else if(catchTreasureChance > 975){
                //Caught Treasure
                player.getInventory().addItem(plugin.warehouse.getCommonTreasureBag());
            }
            else{
                //Caught Fish
                //Give the player the fish and a message
                fish = plugin.fishy.getFish(player,1);
                player.getInventory().addItem(fish);

                //Give the player the money for the fish
                //plugin.eco.depositPlayer(player, plugin.fishy.getFishValue(fish));

                //Give the player fishing tokens
                //plugin.data.addFokens(player.getUniqueId(),r);
            }
        }
        //plugin.createBoard(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = (Player) event.getPlayer();
        plugin.createBoard(player);

        //Open Upgrade Menu
        if(player.isSneaking()){
            if(event.getAction() == Action.LEFT_CLICK_AIR){
                if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD)){
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }
        }

        //Open Treasure 
        if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Treasure")){
            if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()){
                if(event.getAction() == Action.RIGHT_CLICK_AIR){

                    ItemStack[] treasure;
                    treasure = plugin.warehouse.getCommonTreasureLootTable();

                    //Check what kind of crate it is and spin that one TODO
                    plugin.crates.spin(player, treasure);
                    player.sendMessage("Opened a Common Treasure");
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){

        //Don't place Treasure
        if(event.getItemInHand().getItemMeta().getDisplayName().contains("Treasure")){
            event.setCancelled(true);//If item name has Treasure in it then make it unplaceable
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event){

        //Check for inv using glass pane
        List<String> lore = new ArrayList<String>();
        ItemStack glassPane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta glassPaneMeta = glassPane.getItemMeta();
        glassPaneMeta.setDisplayName("Click on an enchant to uprade your rod!");
        lore.add("***********");
        glassPaneMeta.setLore(lore);
        glassPane.setItemMeta(glassPaneMeta);

        Player player = (Player) event.getWhoClicked();

        if(event.getClickedInventory().contains(glassPane)){
            event.setCancelled(true);
        
            ItemStack currentItemHeld = player.getEquipment().getItemInMainHand();
            ItemMeta meta = currentItemHeld.getItemMeta();
            int lure = meta.getEnchantLevel(Enchantment.LURE);
            int luck = meta.getEnchantLevel(Enchantment.LUCK);

            if(event.getCurrentItem() == null) return;
            if(event.getCurrentItem().getItemMeta() == null) return;
            if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            if(event.getSlot() == 10){
                if(lure < 3){
                    if(plugin.data.getFokens(player.getUniqueId()) >= 10){

                        plugin.data.subtractFokens(player.getUniqueId(), 10);
                        currentItemHeld.addEnchantment(Enchantment.LURE, lure+1);
                        player.openInventory(plugin.inv.createUpgradeUI(player));
                    }
                }
            }

            if(event.getSlot() == 11){
                if(luck < 3){
                    if(plugin.data.getFokens(player.getUniqueId()) >= 10){

                        plugin.data.subtractFokens(player.getUniqueId(), 10);
                        currentItemHeld.addEnchantment(Enchantment.LUCK, luck+1);
                        player.openInventory(plugin.inv.createUpgradeUI(player));
                    }
                }
            }

            if(event.getSlot() == 12){
                lore = meta.getLore();
                if(plugin.ftoken.getLevel("F-Token",lore) < 100){
                    if(plugin.data.getFokens(player.getUniqueId()) >= 500*(plugin.ftoken.getLevel("F-Token",lore)+1)){ 
                        List<String> testLore = new ArrayList<String>();
                        testLore = plugin.ftoken.increaseCustomEnchantLevel("F-Token",lore);
                        plugin.data.subtractFokens(player.getUniqueId(), 500*(plugin.ftoken.getLevel("F-Token",lore)));          
                        meta.setLore(testLore);
                        currentItemHeld.setItemMeta(meta);
                        player.openInventory(plugin.inv.createUpgradeUI(player)); 
                    }
                }
            }

            if(event.getSlot() == 13){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.greed.increaseCustomEnchantLevel("Greed",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 14){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.multihook.increaseCustomEnchantLevel("Multi-Hook",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 15){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.treasurefinder.increaseCustomEnchantLevel("Treasure-Finder",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 16){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.pirate.increaseCustomEnchantLevel("Pirate",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 19){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.shardhunter.increaseCustomEnchantLevel("Shard-Hunter",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 20){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.mobcatcher.increaseCustomEnchantLevel("Mob-Catcher",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }
            
            if(event.getSlot() == 21){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.mobexpert.increaseCustomEnchantLevel("Mob-Expert",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 22){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.keyfinder.increaseCustomEnchantLevel("Key-Finder",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 23){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.sponge.increaseCustomEnchantLevel("Sponge",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 24){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.thiccc.increaseCustomEnchantLevel("Thiccc",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }

            if(event.getSlot() == 25){
                lore = meta.getLore();
                if(plugin.data.getFokens(player.getUniqueId()) >= 10){
                    List<String> testLore = new ArrayList<String>();
                    testLore = plugin.longboiii.increaseCustomEnchantLevel("Long-Boiii",lore);
                    plugin.data.subtractFokens(player.getUniqueId(), 10);           
                    meta.setLore(testLore);
                    currentItemHeld.setItemMeta(meta);
                    player.openInventory(plugin.inv.createUpgradeUI(player));
                }
            }
        }

        if(event.getClickedInventory().contains(Material.BLUE_STAINED_GLASS_PANE)){
            if(event.getClickedInventory().getItem(0).getItemMeta().hasLore()){
                event.setCancelled(true);
            }
        }
    }
}