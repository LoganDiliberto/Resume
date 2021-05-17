package dev.nagolmc.spigot.fishplus.enchants;

public class MOBEXPERT extends CustomEnchants{


    public MOBEXPERT() {
        
    }


    @Override
    public String getName() {
        return "Mob-Expert";
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