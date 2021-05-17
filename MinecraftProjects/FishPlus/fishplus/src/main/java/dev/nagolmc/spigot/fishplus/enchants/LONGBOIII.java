package dev.nagolmc.spigot.fishplus.enchants;

public class LONGBOIII extends CustomEnchants{


    public LONGBOIII() {
        
    }


    @Override
    public String getName() {
        return "Long-Boiiiiii";
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