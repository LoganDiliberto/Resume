package dev.nagolmc.spigot.fishplus.sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private String host = "147.135.91.138";
    private String port = "3306"; //Might be something else 3308 maybe
    private String database = "serverpro_db";
    private String username = "root";
    private String password = "yjsSe7jhaiw0jgH";

    private Connection connection;

    public boolean isConnected(){
        return (connection == null ? false : true);//Are we connected?
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()){
            connection = DriverManager.getConnection("jdbc:mysql://" +
            host + ":" + port + "/" + database + "?useSSL=false",
            username, password);   
        }
    }

    public void disconnect(){
        if(!isConnected()){
            try{
                connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}


