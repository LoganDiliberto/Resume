package dev.nagolmc.spigot.fishplus.enchants;

public class GREED extends CustomEnchants{


    public GREED() {
        
    }


    @Override
    public String getName() {
        return "Greed";
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