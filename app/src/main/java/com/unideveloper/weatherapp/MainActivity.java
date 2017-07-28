package com.unideveloper.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends ActionBarActivity implements ListFragment.LVItemClickListener
        , MainFragment.ChangeLocListener {

    MainFragment mainFragment;
    ListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFragment = new ListFragment();
        mainFragment = new MainFragment();

        Bundle bundle = new Bundle();
        bundle.putString("city_name", "N/A");
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .add(R.id.activity_main, mainFragment).commit();

    }

    @Override
    public void lvItemClick(String city_name) {
        Bundle bundle = new Bundle();
        bundle.putString("city_name", city_name);
        mainFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.activity_main, mainFragment).commit();

    }

    @Override
    public void changeLocation() {
        getSupportFragmentManager().beginTransaction().setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.activity_main, listFragment).commit();
    }
}
