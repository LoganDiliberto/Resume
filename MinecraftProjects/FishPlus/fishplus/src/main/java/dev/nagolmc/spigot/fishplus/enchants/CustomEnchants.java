package dev.nagolmc.spigot.fishplus.enchants;

import net.md_5.bungee.api.ChatColor;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import org.bukkit.Bukkit;

public class CustomEnchants {

    public CustomEnchants() {
    }


    public String getName() {
        return "CustomEnchantWrapper";
    }

    public int getStartLevel() {
        return 1;
    }

    public boolean isCursed() {
        return false;
    }

    public boolean isTreasure() {
        return false;
    }

    public int getMaxLevel() {
        return 50;
    }

    public boolean hasCustomEnchant(String ench, List<String> lore){
        if(lore==null){
            return false;
        }
        String name = ChatColor.RESET + "" + ChatColor.GRAY + ench;
        for (int i = 0; i < lore.size(); i++) {
            //Bukkit.getLogger().info("HasCustomEnchant Loop " + String.valueOf(i) + " " + lore.get(i));
            if(lore.get(i).contains(ench)){
                return true;
            }
        }
        return false;
    }

    public int getLevel(String ench, List<String> lore){

        String name = ChatColor.RESET + "" + ChatColor.GRAY + ench;
        if(lore == null){
            return 0;
        }
        else{
            for (int i = 0; i < lore.size(); i++) {
                if(lore.get(i).contains(ench)){
                    int temp = 0;
                    temp = getLast(lore.get(i));
                    return temp;
                }
            }
        }
        return 0;
    }

    public static int getLast(String line)
    {
        int offset = line.length();
        for (int i = line.length() - 1; i >= 0; i--)
        {
            char c = line.charAt(i);
            if (Character.isDigit(c))
            {
                offset--;
            }
            else
            {
                if (offset == line.length())
                {
                    // No int at the end
                    return Integer.MIN_VALUE;
                }
                return Integer.parseInt(line.substring(offset));
            }
        }
        return Integer.parseInt(line.substring(offset));
    }

    public String returnEnchantmentName(String ench, int enchLevel){
        return ChatColor.RESET + "" + ChatColor.GRAY + ench + " " + String.valueOf(enchLevel);
    }

    public List<String> increaseCustomEnchantLevel(String ench, List<String> lore){

        String name = ChatColor.RESET + "" + ChatColor.GRAY + ench;
        int level = 0;

        if(hasCustomEnchant(ench, lore)){
            for (int i = 0; i < lore.size(); i++) {
                if(lore.get(i).contains(ench)){
                    int temp = 0;
                    temp = getLast(lore.get(i))+1;
                    lore.set(i, name + " " + String.valueOf(temp));
                    return lore;
                }
            }
        }
        if(lore==null){
            List<String> loreT = new ArrayList<String>();
            loreT.add(name + " " + String.valueOf(level+1));
            return loreT;
        }
        else{
            lore.add(name + " " + String.valueOf(level+1));
            return lore;
        }
    }
}