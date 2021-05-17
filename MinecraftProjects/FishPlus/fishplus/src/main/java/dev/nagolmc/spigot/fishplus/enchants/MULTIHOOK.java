package dev.nagolmc.spigot.fishplus.enchants;

public class MULTIHOOK extends CustomEnchants{


    public MULTIHOOK() {
        
    }


    @Override
    public String getName() {
        return "Multi-Hook";
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