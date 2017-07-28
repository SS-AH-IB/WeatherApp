package com.unideveloper.weatherapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    LVItemClickListener lvItemClickListener;
    ArrayAdapter<String> arrayAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    public interface LVItemClickListener{
        public void lvItemClick(String city_name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lvItemClickListener=(LVItemClickListener)activity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView lv=(ListView)view.findViewById(R.id.lv);
        ArrayList<String> cities=new ArrayList<>();
        cities.addAll(Arrays.asList(getResources().getStringArray(R.array.cities)));
        Collections.sort(cities);
        arrayAdapter=new ArrayAdapter<String>(getContext(),R.layout.lvrow,R.id.lvrow_tv,cities);
        lv.setAdapter(arrayAdapter);

        final SearchView searchView=(SearchView)view.findViewById(R.id.lv_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s=adapterView.getItemAtPosition(i).toString();
                lvItemClickListener.lvItemClick(s);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.cus_loc)
        {
            final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=layoutInflater.inflate(R.layout.alert_dialog,null);
            final EditText et=(EditText)view.findViewById(R.id.et);
            alertDialog.setView(view);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Go", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.hide();
                    lvItemClickListener.lvItemClick(et.getText().toString());
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.hide();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
}
