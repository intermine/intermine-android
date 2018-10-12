package org.intermine.app.net.request.get;


import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetIntermineListRequest {
    private static String[] mineNamesArray;
    private static String[] mineNamesUrls;
    private static String[] mineNamesWebAppUrls;
    private static JSONObject object;

    public static void parseJson() {
        try{
            JSONArray jsonArray = object.getJSONArray("instances");
            mineNamesArray = new String[jsonArray.length()];
            mineNamesUrls = new String[jsonArray.length()];
            mineNamesWebAppUrls = new String[jsonArray.length()];
            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject mineObject = jsonArray.getJSONObject(i);
                mineNamesArray[i] = mineObject.getString("name");

                StringBuffer url = new StringBuffer(mineObject.getString("url"));
                mineNamesWebAppUrls[i] = url.toString();
                if(url.charAt(url.length()-1) != '/') {
                 url.append('/');
                }
                url.append("service");
                mineNamesUrls[i] = url.toString();

            }
        }catch (JSONException jsonException){

        }catch (Exception exception){

        }
    }

    public static class LoadIntermineList extends AsyncTask<String, Void , String>{
        Exception mException = null;

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            final String urlString = "http://registry.intermine.org/service/instances?mines=all";
            HttpURLConnection urlConnection = null;
            URL url = null;
            InputStream inputStream = null;

            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(5000);
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder response = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null) {
                    response.append(temp);
                }
                String responseString = response.toString();
                responseString = responseString.replaceFirst(",\"statusCode.*\\}","}");
                object = new JSONObject(responseString);

            } catch (Exception e) {
                result = e.toString();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException ignored) {

                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
            return result;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            this.mException = null;
        }

    }

    public static String[] getMineNamesArray(){
        return mineNamesArray;
    }

    public static String[] getMineNamesUrls(){
        return mineNamesUrls;
    }

    public static String[] getMineNamesWebAppUrls(){
        return mineNamesWebAppUrls;
    }
}
