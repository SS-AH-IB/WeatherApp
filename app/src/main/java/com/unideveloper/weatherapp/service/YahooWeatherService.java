package com.unideveloper.weatherapp.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.unideveloper.weatherapp.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Subhajit Mondal on 24-07-2017.
 */

public class YahooWeatherService {
    private Context context;
    private WeatherServiceCallback weatherServiceCallback;
    private String location;
    private Exception error;

    public YahooWeatherService(Context context,WeatherServiceCallback weatherServiceCallback) {
        this.context=context;
        this.weatherServiceCallback = weatherServiceCallback;
    }

    public String getLocation() {
        return location;
    }

    public void refreshWeather(String location){
        this.location=location;
        new AsyncTask<String,String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String YQL=String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='C'",strings[0]);
                String endpoint=String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                try {
                    URL url=new URL(endpoint);
                    URLConnection connection=url.openConnection();
                    InputStream inputStream=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null)
                    {
                        result.append(line);
                    }
                    return result.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s==null&&error!=null){
                    weatherServiceCallback.serviceFailure(error);
                }
                try {
                    JSONObject data=new JSONObject(s);
                    JSONObject queryResults=data.optJSONObject("query");
                    int count=queryResults.optInt("count");
                    if(count==0)
                    {
                        weatherServiceCallback.serviceFailure(
                                new LocationWeatherException("No weather information found for "+getLocation()));
                    }
                    else
                    {
                        Channel channel=new Channel();
                        channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                        weatherServiceCallback.serviceSuccess(channel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(this.location);
    }

    private class LocationWeatherException extends Exception {
        public LocationWeatherException(String s) {
            Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
        }
    }

}
