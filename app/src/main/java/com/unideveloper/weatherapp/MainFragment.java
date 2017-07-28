package com.unideveloper.weatherapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.ActionMenuItem;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.unideveloper.weatherapp.data.Channel;
import com.unideveloper.weatherapp.service.WeatherServiceCallback;
import com.unideveloper.weatherapp.service.YahooWeatherService;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements WeatherServiceCallback {

    private ImageView icon;
    private TextView temperature,condition,location;
    TextView selectedLoc;
    ChangeLocListener changeLocListener;
    private YahooWeatherService yahooWeatherService;
    private ProgressDialog progressDialog;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void serviceSuccess(Channel channel) {
        progressDialog.hide();
        int resourceId=getResources().getIdentifier("drawable/icon"+channel.getItem().getCondition().getCode(),null,getActivity().getPackageName());
        Drawable iconId=getResources().getDrawable(resourceId);
        if(!selectedLoc.getText().toString().equals("N/A"))
        {
            icon.setImageDrawable(iconId);
            temperature.setText(channel.getItem().getCondition().getTemperature()+"\u00B0 "+channel.getUnits().getTemperature());
            location.setText(yahooWeatherService.getLocation());
            condition.setText(channel.getItem().getCondition().getDescription());

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        progressDialog.hide();
        Toast.makeText(getContext(),exception.getMessage(),Toast.LENGTH_SHORT).show();
    }

    public interface ChangeLocListener{
        public void changeLocation();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changeLocListener=(ChangeLocListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView changeLoc=(TextView)view.findViewById(R.id.changeLoc);
        selectedLoc=(TextView)view.findViewById(R.id.selectedLoc);
        selectedLoc.setText(getArguments().getString("city_name"));
        changeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLocListener.changeLocation();
            }
        });
        icon=(ImageView)view.findViewById(R.id.selectedWeaIc);
        temperature=(TextView)view.findViewById(R.id.selectedTemp);
        condition=(TextView)view.findViewById(R.id.selectedWea);
        location=(TextView)view.findViewById(R.id.selectedLoc);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating..");
        progressDialog.show();
        if(isNetworkAvailable())
        {
            yahooWeatherService=new YahooWeatherService(getContext(),this);
            yahooWeatherService.refreshWeather(location.getText().toString());
        }
        else
        {
            progressDialog.hide();
            Toast.makeText(getContext(),"Sorry, No Internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh)
        {
            FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
            fragmentTransaction.detach(this).attach(this).commit();
        }
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
