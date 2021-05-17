package dev.nagolmc.spigot.fishplus.enchants;

public class SPONGE extends CustomEnchants{


    public SPONGE() {
        
    }


    @Override
    public String getName() {
        return "Sponge";
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