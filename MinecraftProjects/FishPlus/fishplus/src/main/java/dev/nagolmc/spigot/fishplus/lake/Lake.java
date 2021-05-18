package dev.nagolmc.spigot.fishplus.lake;


import dev.nagolmc.spigot.fishplus.lake.Data;
//import net.minecraft.server.v1_15_R1.PacketPlayOutWorldBorder;
//import net.minecraft.server.v1_15_R1.WorldBorder;
import org.bukkit.Location;
//import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
//import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Lake {

    //private Location spawn;

    private int bank;
    private int level;

    private ArrayList<String> member;

    private String owner;

    public Lake(int bank, int level, ArrayList<String> member, String owner) {
        this.bank = bank;
        this.level = level;
        this.member = member;
        this.owner = owner;
    }

    /*public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }*/

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<String> getMember() {
        return member;
    }
    public void addMember(String member) {
        getMember().add(member);
        Data.lakes_cfg.set("Lakes." + getOwner() + ".Members", getMember());
        Data.saveCfg();
    }
    public void removeMember(String member) {
        getMember().remove(member);
        Data.lakes_cfg.set("Lakes." + getOwner() + ".Members", getMember());
        Data.saveCfg();
    }
    public void setMember(ArrayList<String> member) {
        this.member = member;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}