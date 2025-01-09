/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnexionHTTP;
/**
 *
 * @author apeyt
 */
public class ConnexionManager {
    
    public static final String URL_Weather = "https://api.openweathermap.org/data/2.5/weather?q=lyon,fr&units=metric&lang=fr&appid=xxxxx";
    public static final String URL_Polution = "https://api.openweathermap.org/data/2.5/air_pollution/forecast?lat=46.0398&lon=5.4133&units=metric&lang=fr&appid=xxxxx";
    private static ConnexionManager manager = null;
    private final Callback callWeather;
    private final Callback callPolution;

    private ConnexionManager(Callback callWeather, Callback callPolution){
        this.callWeather = callWeather;
        this.callPolution = callPolution;
    }
        
    public static ConnexionManager getConnexionManager(Callback callW, Callback callP){  // Point d'entrée du singleton : une seule instance possible
        if (manager==null){
            manager = new ConnexionManager(callW, callP); // Appel du contructeur privé
        }
        return manager;
    }
    
    public void loadWeather() {
        ConnexionThread connexionThread = new ConnexionThread(callWeather, URL_Weather);
        connexionThread.start(); 
    }

    public void loadPolution() {
        ConnexionThread connexionThread = new ConnexionThread(callPolution, URL_Polution);
        connexionThread.start();
    }
}
