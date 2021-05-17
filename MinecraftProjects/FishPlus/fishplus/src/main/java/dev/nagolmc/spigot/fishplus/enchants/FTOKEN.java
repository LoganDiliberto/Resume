package dev.nagolmc.spigot.fishplus.enchants;

public class FTOKEN extends CustomEnchants{


    public FTOKEN() {
        
    }


    @Override
    public String getName() {
        return "F-Token";
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