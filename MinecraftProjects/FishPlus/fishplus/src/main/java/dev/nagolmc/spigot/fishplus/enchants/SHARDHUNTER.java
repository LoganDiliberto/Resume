package dev.nagolmc.spigot.fishplus.enchants;

public class SHARDHUNTER extends CustomEnchants{


    public SHARDHUNTER() {
        
    }


    @Override
    public String getName() {
        return "Shard-Hunter";
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