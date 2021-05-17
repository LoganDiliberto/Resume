package dev.nagolmc.spigot.fishplus.enchants;

public class TREASUREFINDER extends CustomEnchants{


    public TREASUREFINDER() {
        
    }


    @Override
    public String getName() {
        return "Treasure-Finder";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }
}