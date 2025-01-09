/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConnexionHTTP;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author apeyt
 */
public interface Callback{
    public void onWorkDone(JSONObject JO)throws JSONException;
}


