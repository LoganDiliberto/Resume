package dev.nagolmc.spigot.fishplus.lake;

import dev.nagolmc.spigot.fishplus.App;
//import com.godminer.party.Party;
//import com.godminer.schematic.Schematic;
import dev.nagolmc.spigot.fishplus.lake.Data;
//import com.godminer.utils.Items;
import dev.nagolmc.spigot.fishplus.lake.PlayerUtil;
import dev.nagolmc.spigot.fishplus.lake.LakeManager;

//import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CMD_lake implements CommandExecutor {

    public static HashMap<String, String> invites = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player)sender;
        LakeManager lakeManager = new LakeManager();
        
        if(args.length == 2) {

            if(args[0].equalsIgnoreCase("invite")) {

                Player t = Bukkit.getPlayer(args[1]);
                if(t == p) {
                    p.sendMessage(Data.prefix + "§cYou can't send invites to yourself§7!");
                    return false;
                }
                if(t != null) {                    if(lakeManager.hasLake(p)) {
                        if (!lakeManager.isMember(t) && !lakeManager.hasLake(t)) {
                            p.sendMessage(Data.prefix + "You invited §b" + t.getName() + " §7to your lake!");
                            t.sendMessage(Data.prefix + "§b" + p.getName() + " §7sent you a lake invite!");
                            t.sendMessage(Data.prefix + "Use §b/lake accept " + p.getName() + "§7 to join the lake.");
                            invites.put(t.getName(), p.getName());
                        }else {
                            p.sendMessage(Data.prefix + "That player §calready has a lake§7!");
                        }
                    }else {
                        p.sendMessage(Data.prefix + "You §cdon't have a lake§7!");
                    }
                }else {
                    p.sendMessage(Data.prefix + "This player is §coffline§7!");
                }

            }if(args[0].equalsIgnoreCase("accept")) {

                if(!lakeManager.isMember(p) && !lakeManager.hasLake(p)) {
                    Player t = Bukkit.getPlayer(args[1]);
                    if (t != null) {
                        if (invites.containsKey(p.getName())) {
                            if (invites.get(p.getName()).equalsIgnoreCase(t.getName())) {

                                Lake lake = Data.lakeList.get(t.getUniqueId().toString());
                                if(lake != null) {
                                    if(lake.getMember().size() < 5) {
                                        lake.addMember(p.getUniqueId().toString());
                                        t.sendMessage(Data.prefix + "§b" + p.getName() + " §7joined §byour lake§7!");
                                        p.sendMessage(Data.prefix + "You joined §b" + new PlayerUtil().getNameFromUUID(UUID.fromString(lake.getOwner())) + "'s lake§7!");
                                    }else {
                                        p.sendMessage(Data.prefix + "This lake is full!");
                                    }
                                }else {
                                    p.sendMessage(Data.prefix + "That lake §cdoesn't exist anymore§7!");
                                }

                            } else {
                                p.sendMessage(Data.prefix + "7You got §cno invite §7from §c" + t.getName() + "§7!");
                            }
                        } else {
                            p.sendMessage(Data.prefix + "§cNo invites found§7!");
                        }

                    } else {
                        p.sendMessage(Data.prefix + "This player is §coffline§7!");
                    }
                }else {
                    p.sendMessage(Data.prefix + "You are already a §bmember of another lake§7!");
                }

            }else if(args[0].equalsIgnoreCase("kick")) {
                if(lakeManager.hasLake(p)) {
                    String memberUUID = new PlayerUtil().getUUIDFromName(args[1]).toString();//TODO
                    Lake lake = Data.lakeList.get(p.getUniqueId().toString());
                    if(lake.getMember().contains(memberUUID)) {
                        lake.removeMember(memberUUID);
                        p.sendMessage(Data.prefix + "You removed lake §b" + args[1] + "§7!");
                    }else {
                        p.sendMessage(Data.prefix + "That member §cdoesn't exist§7!");
                    }
                }else {
                    p.sendMessage(Data.prefix + "You §cdon't have a lake§7!");
                }

            }else if(args[0].equalsIgnoreCase("deposit")) {

                try {
                    int money = Integer.valueOf(args[1]);
                    if(lakeManager.hasLake(p)) {
                        if(App.eco.has(p.getName(), money)) {
                            Data.lakes_cfg.set("Lakes." + p.getUniqueId().toString() + ".Bank", Data.lakes_cfg.getInt("Lakes." + p.getUniqueId().toString() + ".Bank")+money);
                            Data.saveCfg();
                            App.eco.withdrawPlayer(p.getName(), money);//TODO
                            p.sendMessage(Data.prefix + "You transferred §b" + money + "$ §7to your bank!");
                        }else {
                            p.sendMessage(Data.prefix + "You don't have enough money!");
                        }
                    }else {
                        p.sendMessage(Data.prefix + "You §cdon't have a lake§7!");
                    }
                }catch (Exception ex) {
                    p.sendMessage(Data.prefix + "Argument 2 has to be a number!");
                }

            }else if(args[0].equalsIgnoreCase("withdraw")) {

                try {
                    int money = Integer.valueOf(args[1]);
                    if(lakeManager.hasLake(p)) {
                        if(Data.lakes_cfg.getInt("Lakes." + p.getUniqueId().toString() + ".Bank") >= money) {
                            Data.lakes_cfg.set("Lakes." + p.getUniqueId().toString() + ".Bank", Data.lakes_cfg.getInt("Lakes." + p.getUniqueId().toString() + ".Bank")-money);
                            Data.saveCfg();
                            App.eco.depositPlayer(p.getName(), money);//TODO
                            p.sendMessage(Data.prefix + "You withdrew §b" + money + "$ §7to your balance!");
                        }else {
                            p.sendMessage(Data.prefix + "You don't have enough money in your lake bank!");
                        }
                    }else {
                        p.sendMessage(Data.prefix + "You §cdon't have a lake§7!");
                    }
                }catch (Exception ex) {
                    p.sendMessage(Data.prefix + "Argument 2 has to be a number!");
                }

            }

        }else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("leave")) {

                if(lakeManager.hasLake(p) || lakeManager.isMember(p)) {

                    if(lakeManager.hasLake(p)) {
                        p.sendMessage(Data.prefix + "You have §cdeleted your lake§7!");
                        Data.lakeList.remove(p.getUniqueId().toString());
                        Data.lakes_cfg.set("lakes." + p.getUniqueId().toString(), null);
                        Data.saveCfg();
                    }else if(lakeManager.isMember(p)) {
                        p.sendMessage(Data.prefix + "You have §cleft your lake§7!");
                        Lake lake = lakeManager.getLakeFromMember(p);
                        lake.removeMember(p.getUniqueId().toString());
                        if(Bukkit.getPlayer(UUID.fromString(lake.getOwner())) != null) {
                            Bukkit.getPlayer(UUID.fromString(lake.getOwner())).sendMessage(Data.prefix + "§b" + p.getName() + " §7has left your lake§7!");
                        }
                    }

                }else {
                    p.sendMessage(Data.prefix + "You §cdon't have a lake§7!");
                }

            }else if(args[0].equalsIgnoreCase("rankup")) {

                if(!lakeManager.hasLake(p)) {
                    p.sendMessage(Data.prefix + "You §cdon't have a lake§7!");
                    return false;
                }

                int levelInt = Data.lakes_cfg.getInt("Lakes." + p.getUniqueId().toString() + ".Level");
                String level = "Level" + levelInt;

                int bank = Data.lakes_cfg.getInt("Lakes." + p.getUniqueId().toString() + ".Bank");

                if(Data.level_cfg.contains("LevelInfo." + level)) {
                    int nextLevel = Data.level_cfg.getInt("LevelInfo." + level + ".Next");
                    int cost = Data.level_cfg.getInt("LevelInfo.Level" + nextLevel + ".Cost");
                    if (bank >= cost) {
                        Data.lakes_cfg.set("Lakes." + p.getUniqueId().toString() + ".Bank", bank - cost);
                        Data.lakes_cfg.set("Lakes." + p.getUniqueId().toString() + ".Level", nextLevel);
                        Data.saveCfg();
                        p.sendMessage(Data.prefix + "You upgraded your lake to §blevel " + nextLevel + "§7!");
                    } else {
                        p.sendMessage(Data.prefix + "You §cdon't have §7enough money §cin your lake bank§7!");
                    }
                }else {
                    p.sendMessage(Data.prefix + "You have reached §bthe maximum level§7!");
                }

            }else if(args[0].equalsIgnoreCase("info")) {
                if(lakeManager.hasLake(p)) {
                    p.sendMessage(Data.prefix + "Your money in your lake bank: §b" + Data.lakes_cfg.getInt("Lakes." + p.getUniqueId() + ".Bank") + "$");
                    p.sendMessage(Data.prefix + "Your lake level: §bLevel " + Data.lakes_cfg.getInt("Lakes." + p.getUniqueId().toString() + ".Level"));
                    ArrayList<String> memberList = (ArrayList<String>) Data.lakes_cfg.getList("Lakes." + p.getUniqueId().toString() + ".Members");
                    PlayerUtil playerUtil = new PlayerUtil();
                    p.sendMessage(Data.prefix + "Member:");
                    if(memberList.size() == 0) {
                        p.sendMessage(Data.prefix + "  No members");
                    }else {
                        for (String member : memberList) {
                            p.sendMessage(Data.prefix + "  " + playerUtil.getNameFromUUID(UUID.fromString(member)));
                        }
                    }

                }else {
                    p.sendMessage(Data.prefix + "You §cdon't have a lake§7!");
                }
            }
            else if(args[0].equalsIgnoreCase("create")){
                if(!lakeManager.hasLake(p)) {
                    p.sendMessage(Data.prefix + "Your lake will be generated in a few seconds...");
                    //Schematic schematic = Data.schematics.get(Data.minesconf_cfg.getString("MineSetup.Schematic"));
                    //Location location = new Location(Bukkit.getWorld(Data.minesconf_cfg.getString("MineSetup.World")), 0, 130, Data.minesconf_cfg.getInt("MineSetup.Current"));
                    //schematic.setLocation(location);
                    //schematic.place(p);
                    //Data.minesconf_cfg.set("MineSetup.Current", Data.minesconf_cfg.getInt("MineSetup.Current")+Data.minesconf_cfg.getInt("MineSetup.Distance"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ArrayList<String> members = new ArrayList<>();
                            Data.lakes_cfg.set("Lakes." + p.getUniqueId().toString() + ".Members", members);
                            Data.lakes_cfg.set("Lakes." + p.getUniqueId().toString() + ".Bank", 0);
                            Data.lakes_cfg.set("Lakes." + p.getUniqueId().toString() + ".Level", 1);
                            Data.saveCfg();
                            lakeManager.loadLake(p.getUniqueId().toString());
                        }
                    }.runTaskLater(App.getPlugin(App.class), 20*10);
                }else {
                    if(lakeManager.hasLake(p)) {
                        p.sendMessage(Data.prefix + "You were teleported to §byour lake§7!");
                        Lake lake = Data.lakeList.get(p.getUniqueId().toString());
                        //p.teleport(mine.getSpawn().add(0, 3, 0));
                        /*lake.getSpawn().subtract(0, 3, 0);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                lake.spawnWorldBorder(p);
                            }
                        }.runTaskLater(Main.instance, 20);*/
                    }else if(lakeManager.isMember(p)) {
                        p.sendMessage(Data.prefix + "You were teleported to §byour lake§7!");
                        Lake lake = lakeManager.getLakeFromMember(p);
                        //p.teleport(mine.getSpawn().add(0, 3, 0));
                        //lake.getSpawn().subtract(0, 3, 0);
                        /*new BukkitRunnable() {
                            @Override
                            public void run() {
                                mine.spawnWorldBorder(p);
                            }
                        }.runTaskLater(Main.instance, 20);*/
                    }
                }
            }else if(args[0] == ""){
                if(!lakeManager.hasLake(p)) {
                    p.openInventory(App.getPlugin(App.class).inv.lakeGUI(p));
                }
            }
            else {
                if(!lakeManager.hasLake(p)) {
                    p.openInventory(App.getPlugin(App.class).inv.lakeGUI(p));
                }
            }

        }
        return false;
    }
}
        /*else if(args.length == 0) {
            if(!lakeManager.hasMine(p) && !lakeManager.isMember(p)) {
                p.sendMessage(Data.prefix + "Your lake will be generated in a few seconds...");
                Schematic schematic = Data.schematics.get(Data.minesconf_cfg.getString("MineSetup.Schematic"));
                Location location = new Location(Bukkit.getWorld(Data.minesconf_cfg.getString("MineSetup.World")), 0, 130, Data.minesconf_cfg.getInt("MineSetup.Current"));
                schematic.setLocation(location);
                schematic.place(p);
                Data.minesconf_cfg.set("MineSetup.Current", Data.minesconf_cfg.getInt("MineSetup.Current")+Data.minesconf_cfg.getInt("MineSetup.Distance"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ArrayList<String> members = new ArrayList<>();
                        Data.mines_cfg.set("Mines." + p.getUniqueId().toString() + ".Members", members);
                        Data.mines_cfg.set("Mines." + p.getUniqueId().toString() + ".Bank", 0);
                        Data.mines_cfg.set("Mines." + p.getUniqueId().toString() + ".Level", 1);
                        Data.saveCfg();
                        mineManager.loadMine(p.getUniqueId().toString());
                    }
                }.runTaskLater(Main.getPlugin(Main.class), 20*10);
            }else {
                if(mineManager.hasMine(p)) {
                    p.sendMessage(Data.prefix + "You were teleported to §byour mine§7!");
                    Mine mine = Data.mineList.get(p.getUniqueId().toString());
                    p.teleport(mine.getSpawn().add(0, 3, 0));
                    mine.getSpawn().subtract(0, 3, 0);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mine.spawnWorldBorder(p);
                        }
                    }.runTaskLater(Main.instance, 20);
                }else if(mineManager.isMember(p)) {
                    p.sendMessage(Data.prefix + "You were teleported to §byour mine§7!");
                    Mine mine = mineManager.getMineFromMember(p);
                    p.teleport(mine.getSpawn().add(0, 3, 0));
                    mine.getSpawn().subtract(0, 3, 0);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mine.spawnWorldBorder(p);
                        }
                    }.runTaskLater(Main.instance, 20);
                }
            }
        }*/
