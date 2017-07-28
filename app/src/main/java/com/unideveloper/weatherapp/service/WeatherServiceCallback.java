package com.unideveloper.weatherapp.service;

import com.unideveloper.weatherapp.data.Channel;

/**
 * Created by Subhajit Mondal on 24-07-2017.
 */

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
