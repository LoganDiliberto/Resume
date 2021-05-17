package dev.nagolmc.spigot.fishplus.sql;

import dev.nagolmc.spigot.fishplus.App;
import org.bukkit.entity.*;
import java.util.*;
import java.sql.*;

public class SQLGetter {

    private App plugin;
    public SQLGetter(App plugin){
        this.plugin = plugin;
        
    }

    public void createTable(){
        PreparedStatement ps;
        try{
            //Everytime the server loads try to create a table if one doesn't exist already
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS fokens " + "(NAME VARCHAR(100), UUID VARCHAR(100), FOKENS INT(100), PRIMARY KEY (NAME))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Create player and add them to the database
    public void createPlayer(Player player) {
        try{
            UUID uuid = player.getUniqueId();
            if(!exists(uuid)){
                PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO fokens" + " (NAME, UUID) VALUES (?,?)");
                ps2.setString(1, player.getDisplayName());
                ps2.setString(2, uuid.toString());
                ps2.executeUpdate();

                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Check if player already exists in the database, if they do don't create them
    public boolean exists(UUID uuid){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM fokens WHERE UUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if(results.next()){
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addFokens(UUID uuid, int fokens){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE fokens SET FOKENS=? WHERE UUID=?");
            ps.setInt(1, (getFokens(uuid)+fokens));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void subtractFokens(UUID uuid, int fokens){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE fokens SET FOKENS=? WHERE UUID=?");
            if(getFokens(uuid)-fokens < 0){
                fokens = 0;
            }
            ps.setInt(1, (getFokens(uuid)-fokens));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getFokens(UUID uuid){
        try{
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM fokens WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            int fokens = 0;
            if(rs.next()){
                fokens = rs.getInt("FOKENS");
                return fokens;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}