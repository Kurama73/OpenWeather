/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import ConnexionHTTP.Callback;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Observable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author apeyt
 */
public class DbManager extends Observable implements Callback {
    Connection con;
    // https://blog.paumard.org/cours/jdbc/chap02-apercu-exemple.html
    // https://learn.microsoft.com/fr-fr/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver16

    public Connection getCon() {
        return con;
    }

    /**
     * @throws java.sql.SQLException
     * @brief Constructor
     *
     * Constructor sets up connection with db and opens it
     * @param url - absolute path to db file
     */
    public DbManager(String url) throws SQLException {
        con = DriverManager.getConnection(url);
    }
    
    /**
     * @brief Test if the connexion is open or close
     * @return
     */
    public Boolean isOpen()
    {
        return con != null;
    }

    /**
     * @brief Creates a new 'pollution' table if it doesn't already exist
     * @return true - 'pollution' table created successfully, false - table not created
     */
    public Boolean createTable()
    {
        String query = "CREATE TABLE IF NOT EXISTS pollution(id INTEGER PRIMARY KEY, dt INTEGER, aqi INTEGER);";
        return executeUpdate( query );
    }

    /**
     * @brief Add data to db
     * @param dt - date time
     * @param aqi - air quality indice
     * @return true - data added successfully, false - data not added
     */
    public Boolean addData(int dt, int aqi) 
    {
        if (!entryExists(dt))
        {
            String query = "INSERT INTO pollution (dt,aqi) VALUES ("+dt+","+aqi+")";
            return executeUpdate( query );
        }
        
        return false;
    }

    /**
     * @brief Remove data of dt "dt" from db
     * @param dt - dt of data to remove.
     * @return true - data removed successfully, false - data not removed
     */
    public Boolean removeData(int dt)
    {
        String query = "DELETE FROM pollution WHERE dt = "+ dt;
        return executeUpdate( query );
    }

    /**
     * @brief Print values of all data in db
     */
    public void printAllData()
    {
        String query = "SELECT * FROM pollution";
        try (Statement statement = con.createStatement();) {

            // Create and execute a SELECT SQL statement.
            ResultSet resultSet = statement.executeQuery(query);

            // Print results from select statement
            while (resultSet.next()) {
                int dt = resultSet.getInt("dt");
                int aqi = resultSet.getInt("aqi");
                
                System.out.println("===" + dt + " " + aqi);
            }
        }
        catch (SQLException e) {
            System.out.println("Error : printAllData()"+ e.getMessage());
        }
    }

    /**
     * @brief Check if data of dt "dt" exists in db
     * @param dt - dt of data to  to check.
     * @return true - data exists, false - data does not exist
     */
    public Boolean entryExists(int dt)
    {
        Boolean exists = false;

        String query = "SELECT dt FROM pollution WHERE dt = " + dt;
        try (Statement statement = con.createStatement();) {

            // Create and execute a SELECT SQL statement.
            ResultSet resultSet = statement.executeQuery(query);

            // Print results from select statement
            exists = resultSet.next();
        }
        catch (SQLException e) {
            System.out.println("Error : entryExists()");
        }

        return exists;
    }

    /**
     * @brief Remove all data from db
     * @return true - all data removed successfully, false - not removed
     */
    public Boolean removeAllData()
    {
        String query = "DELETE FROM pollution";
        return executeUpdate(query );
    }
    
    /**
     * @brief Generic function to execute a Query to update the data
     * @return true - all data modified successfully, false - not modified
     */
    private Boolean executeUpdate(String query )
    {
        Boolean success = false;
        
        try (Statement statement = con.createStatement();) {

            // Create and execute a SELECT SQL statement.
            int number = statement.executeUpdate(query);

            if (number>0) success = true;
        }
        catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }
        
        return success;
    }

    @Override
    public void onWorkDone(JSONObject jsonObj) throws JSONException {

        JSONArray list = jsonObj.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject forecast = list.getJSONObject(i);
            int dt = forecast.getInt("dt");

            int aqi = forecast.getJSONObject("main").getInt("aqi");

            // Ajout des données dans la base de données
            addData(dt, aqi);
        }

        setChanged();
        notifyObservers();
    }




}
