/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnexionHTTP;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author apeyt
 */
public class ConnexionThread extends Thread{
    
    private final Callback call;
    private final String requestUrl;

    public ConnexionThread(Callback call, String requestUrl) {
        this.call = call;
        this.requestUrl = requestUrl;
    }

    @Override
    public void run() {

        String result = null;

        // Get Data from API
        try {
            result = getRequest();
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(ConnexionThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Create the JSON and call the DP Callback
        try {
            JSONObject jsonObject = new JSONObject(result);
            call.onWorkDone(jsonObject);
        } catch (JSONException ex) {
            Logger.getLogger(ConnexionThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getRequest() throws InterruptedException, IOException {
        String responseJSON = null;

        URI url = URI.create(requestUrl);

        // create a client
        var client = HttpClient.newHttpClient();

        // create a request
        var request = HttpRequest.newBuilder(url)
           .header("accept", "application/json")
           .build();

        // use the client to send the request
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            responseJSON = response.body();

        } else {
            System.out.println("Page non trouvée.. ");
        }

        return responseJSON;
    }
}
