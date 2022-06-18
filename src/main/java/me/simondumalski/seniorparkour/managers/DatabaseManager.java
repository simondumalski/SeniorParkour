package me.simondumalski.seniorparkour.managers;

import me.simondumalski.seniorparkour.Main;
import me.simondumalski.seniorparkour.utils.Log;
import me.simondumalski.seniorparkour.utils.Parkour;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {

    private final Main plugin = Main.getInstance();

    private final String HOST;
    private final int PORT;
    private final String DATABASE;
    private final String USERNAME;
    private final String PASSWORD;

    private final String CONNECTION_URL;

    private Connection connection;

    public DatabaseManager() {

        HOST = plugin.getConfig().getString("mysql.host");
        PORT = plugin.getConfig().getInt("mysql.port");
        DATABASE = plugin.getConfig().getString("mysql.database");
        USERNAME = plugin.getConfig().getString("mysql.username");
        PASSWORD = plugin.getConfig().getString("mysql.password");

        CONNECTION_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;

    }

    /**
     * Inserts player statistics into the database
     * @param player Player that the stats belong to
     * @param parkour Parkour the stats belong to
     * @param time Time the parkour was completed in
     */
    public void insertStats(Player player, Parkour parkour, int time) {

        String playerUUID = player.getUniqueId().toString();
        String courseName = parkour.getCourseName();
        insertStats(playerUUID, courseName, time);

    }

    /**
     * Inserts player statistics into the database
     * @param playerUUID UUID of the player that the stats belong to
     * @param courseName Name of the parkour course that the stats belong to
     * @param time Time the parkour was completed in
     */
    public void insertStats(String playerUUID, String courseName, int time) {

        try {

            //Check if the player has existing stats for the specified parkour
            PreparedStatement statsCheck = connection.prepareStatement("SELECT course_name FROM playerdata WHERE uuid = ?;");
            statsCheck.setString(1, playerUUID);
            ResultSet set1 = statsCheck.executeQuery();

            boolean hasStats = false;

            while (set1.next()) {

                //Get the parkour course name
                String resultsSetCourseName = set1.getString(1);

                if (resultsSetCourseName == null) {
                    break;
                }

                if (resultsSetCourseName.equals(courseName)) {
                    hasStats = true;
                    break;
                }

            }

            //If the player already has stats for the parkour course, change them
            if (hasStats) {

                PreparedStatement statement = connection.prepareStatement("UPDATE playerdata SET time_completed = ? WHERE uuid = ? AND course_name = ?;");
                statement.setInt(1, time);
                statement.setString(2, playerUUID);
                statement.setString(3, courseName);
                statement.executeUpdate();

            //Otherwise, insert the stats into the database
            } else {

                PreparedStatement statement = connection.prepareStatement("INSERT INTO playerdata (uuid, course_name, time_completed) VALUES (?, ?, ?);");
                statement.setString(1, playerUUID);
                statement.setString(2, courseName);
                statement.setInt(3, time);
                statement.executeUpdate();

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Removes all the data for the specified Parkour Course.
     * Used when a Parkour Course is deleted
     * @param parkour Parkour Course whose data will be deleted
     */
    public void removeData(Parkour parkour) {

        String courseName = parkour.getCourseName();

        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM playerdata WHERE course_name = ?;");
            statement.setString(1, courseName);
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Retrieves all player stats for the specified player from the database
     * @param player Player whose stats to get
     * @return HashMap of the parkours the player has completed, and the time they were completed in
     */
    public HashMap<Parkour, Integer> getPlayerStats(Player player) {

        try {

            String playerUUID = player.getUniqueId().toString();
            PreparedStatement statement = connection.prepareStatement("SELECT course_name, time_completed FROM playerdata WHERE uuid = ?;");
            statement.setString(1, playerUUID);
            ResultSet resultSet = statement.executeQuery();

            HashMap<Parkour, Integer> dataHashmap = new HashMap<>();

            while (resultSet.next()) {

                //Get the parkour
                Parkour parkour = plugin.getParkourManager().getParkour(resultSet.getString(1));

                if (parkour == null) {
                    continue;
                }

                //Get the time completed
                int timeCompleted = resultSet.getInt(2);

                //Add the data to the hashmap
                dataHashmap.put(parkour, timeCompleted);

            }

            return dataHashmap;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Returns the statistics for the specified Parkour Course
     * @param parkour Parkour Course to get the stats for
     * @return HashMap of players and their completed times in the course
     */
    public HashMap<UUID, Integer> getParkourStats(Parkour parkour) {

        try {

            String courseName = parkour.getCourseName();

            PreparedStatement statement = connection.prepareStatement("SELECT uuid, time_completed FROM playerdata WHERE course_name = ?;");
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();

            HashMap<UUID, Integer> data = new HashMap<>();

            while (resultSet.next()) {

                UUID playerUUID = UUID.fromString(resultSet.getString(1));
                int timeCompleted = resultSet.getInt(2);

                data.put(playerUUID, timeCompleted);

            }

            return data;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * Connects to the MySQL Database
     */
    public void connect() {

        try {
            connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
            MessageManager.log(Log.DATABASE_CONNECTED, Level.INFO, null);
        } catch (SQLException ex) {
            ex.printStackTrace();
            MessageManager.log(Log.DATABASE_ERROR_CONNECTING, Level.WARNING, null);
        }

    }

    /**
     * Disconnects from the MySQL Database
     */
    public void disconnect() {

        if (isConnected()) {
            try {
                connection.close();
                MessageManager.log(Log.DATABASE_DISCONNECTED, Level.INFO, null);
            } catch (SQLException ex) {
                ex.printStackTrace();
                MessageManager.log(Log.DATABASE_ERROR_DISCONNECTING, Level.WARNING, null);
            }
        }

    }

    /**
     * Prepares the MySQL database for storing player data if it was not already prepared
     */
    public void prepareDatabase() {

        try {

            //Create the PlayerData table if it does not exist
            PreparedStatement createTables = connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata ( uuid varchar(36) NOT NULL, course_name varchar(100) NOT NULL, time_completed int(11) NOT NULL );");
            createTables.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Returns the connection to the MySQL Database
     * @return Connection to database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Returns true/false if the plugin is connected to the MySQL database
     * @return True/false
     */
    public boolean isConnected() {
        return connection != null;
    }

}
