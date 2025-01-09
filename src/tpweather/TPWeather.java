/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpweather;

import Model.DbManager;
import View.Window;
import Model.WeatherReport;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONException;

/**
 *
 * @author apeyt
 */
public class TPWeather {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws org.json.JSONException
     */
    public static void main(String[] args) throws SQLException, IOException, InterruptedException, JSONException {
        
        String url = "jdbc:sqlite:data/pollution.sqlite"; // URL of the local database
        DbManager dbm = new DbManager(url);

        if (dbm.isOpen())
        {
            dbm.createTable();
        }
        else
        {
            System.out.println( "Database is not open!");
        }
          
        WeatherReport WR = new WeatherReport();

        new Window(WR, dbm).setVisible(true);
        
    }
    
}
