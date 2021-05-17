package dev.nagolmc.spigot.fishplus.enchants;

public class MOBCATCHER extends CustomEnchants{


    public MOBCATCHER() {
        
    }


    @Override
    public String getName() {
        return "Mob-Catcher";
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