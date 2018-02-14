package maksach.myapplication;

import android.os.AsyncTask;

import java.net.URL;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;

/**
 * Created by sachinmakaram on 11/5/17.
 */

public class ElectricityWebService extends AsyncTask<Void, Void, String> {

    private final static String API_KEY = "f1fede5e78fb75019a26f213ba40f77b";

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url;
            url = new URL("http://api.eia.gov/category/?api_key=" + API_KEY + "&category_id=3");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
