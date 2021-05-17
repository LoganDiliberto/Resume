package dev.nagolmc.spigot.fishplus.enchants;

public class THICCC extends CustomEnchants{


    public THICCC() {
        
    }


    @Override
    public String getName() {
        return "Thiccc";
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