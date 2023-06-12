package com.example.fantaf1.network;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ResourceManagerInternal;

import com.example.fantaf1.R;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class F1APIservice {
    private AppCompatActivity context;
    private URL BASE_URL;

    public F1APIservice(AppCompatActivity aContext){
        context = aContext;
    }

    public JSONObject fetchData(String url){
        try {

            BASE_URL = new URL("https://ergast.com/api/f1/current/last/drivers/"+url);
            HttpURLConnection conn = (HttpURLConnection) BASE_URL.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(BASE_URL.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string into a json object
                JSONObject data_obj = new JSONObject(inline);

                return data_obj;
            }

        } catch (Exception e) {

            context.runOnUiThread(()-> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        return null;
    }
}
