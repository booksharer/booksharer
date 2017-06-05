package com.booksharer.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ConnectTask extends AsyncTask<HashMap<String, String>, String, Boolean> {
    String response;

    @Override
    protected Boolean doInBackground(HashMap<String, String>... params) {
        try {
            response = postParams(params[0]);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }

    public String postParams(HashMap<String, String> params) throws IOException {

        StringBuilder response = new StringBuilder();
        String urlSpec = "http://172.27.211.7:8080/Server4android/Search.do";
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());
        StringBuilder content = new StringBuilder();
        Iterator i = params.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (java.util.Map.Entry) i.next();
            content.append("&" + entry.getKey() + "=" + entry.getValue());
        }
        Log.d("test", content.substring(1));
        out.writeBytes(content.substring(1));

        out.flush();
        out.close();
        try {
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            String line;
            response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return response.toString();

    }

}
