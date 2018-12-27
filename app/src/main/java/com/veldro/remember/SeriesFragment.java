package com.veldro.remember;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class SeriesFragment extends Fragment implements SeriesAddDialogFragment.AddSeriesDialogListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRefSeries;

    ExpandableListView m_seriesListView;
    ArrayList<SeriesEntry> SeriesEntrys = new ArrayList<>();
    ExpandableListAdapterSeries seriesAdapter;
    FloatingActionButton addNewSeriesButton;

    public enum OrderModeSeries{
        NAME,
        LASTCHANGED,
        AGE
    }

    public static OrderModeSeries orderModeSeries = OrderModeSeries.LASTCHANGED;


    public SeriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseRefSeries = mDatabase.child("users").child(String.valueOf(mAuth.getUid())).child("series");


        m_seriesListView = view.findViewById(R.id.seriesExpListView);
        SeriesEntrys.clear();

        seriesAdapter = new ExpandableListAdapterSeries(getContext(), SeriesEntrys, this);
        m_seriesListView.setAdapter(seriesAdapter);

        m_seriesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteSeriesDialog(position);
                return false;
            }
        });

        //Fill Arraylist whit Cloud Data
        mDatabaseRefSeries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SeriesEntrys.clear();
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    SeriesEntrys.add(dsp.getValue(SeriesEntry.class));

                }
                OrderSeriesEntrys(SeriesEntrys, orderModeSeries);
                seriesAdapter.notifyDataSetChanged();
                //Toast.makeText(getContext(),"Timestamp to int: "+ SeriesEntrys.get(0).TimestampChanged, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addNewSeriesButton = view.findViewById(R.id.addNewSeriesButton);
        addNewSeriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSeriesDialog();
            }
        });



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFinishAddSeriesDialog(String name, int season, int episode) {
        addNewSeries(mAuth.getUid(), name, season, episode);
    }


    private void addNewSeries(String userId, String name){
        if(userId == null)
            return;
        SeriesEntry newEntry = new SeriesEntry(name);
        mDatabaseRefSeries.child(newEntry.name).setValue(newEntry);
    }

    private void addNewSeries(String userId, String name, int season, int episode){
        if(userId == null)
            return;
        SeriesEntry newEntry = new SeriesEntry(name,season,episode);
        mDatabaseRefSeries.child(newEntry.name).setValue(newEntry);
    }

    public void IncEpisodeSeriesEntry(SeriesEntry entry){
        //Do FireBase Stuff
        entry.incEpisode();
        mDatabaseRefSeries.child(entry.name).setValue(entry);
    }

    public void IncSeasonSeriesEntry(SeriesEntry entry){
        entry.incSeason();
        mDatabaseRefSeries.child(entry.name).setValue(entry);
    }

    public void DecEpisodeSeriesEntry(SeriesEntry entry){
        entry.decEpisode();
        mDatabaseRefSeries.child(entry.name).setValue(entry);
    }

    public void DecSeasonSeriesEntry(SeriesEntry entry){
        entry.decSeason();
        mDatabaseRefSeries.child(entry.name).setValue(entry);
    }

    public void DeleteSeriesEntry(int position){
        mDatabaseRefSeries.child(SeriesEntrys.get(position).name).setValue(null);
    }

    private void showAddSeriesDialog(){
        FragmentManager fm = getFragmentManager();
        SeriesAddDialogFragment addSeriesDialog = SeriesAddDialogFragment.newInstance("Add Series");
        addSeriesDialog.setTargetFragment(SeriesFragment.this, 300);
        addSeriesDialog.show(fm,"AddSeriesDialog");
    }

    private void showDeleteSeriesDialog(final int position){
        String s = "Do you want to delete " + SeriesEntrys.get(position).name + "?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(s)
                .setTitle("Delete")
                .setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteSeriesEntry(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void OrderSeriesEntrys(ArrayList<SeriesEntry> entries , OrderModeSeries mode){

        SeriesEntry temp;
        if (entries.size()>1) // check if the number of orders is larger than 1
        {
            switch (mode) {
                case NAME:
                    break;
                case LASTCHANGED:

                    for (int x=0; x<entries.size(); x++) // bubble sort outer loop
                    {
                        for (int i=0; i < entries.size()- x - 1; i++) {
                            if (Long.parseLong(entries.get(i).TimestampChanged.toString())-Long.parseLong(entries.get(i+1).TimestampChanged.toString()) < 0);
                            {
                                temp = entries.get(i);
                                entries.set(i,entries.get(i+1) );
                                entries.set(i+1, temp);
                            }
                        }
                    }
                    Toast.makeText(getContext(),"First element is: " + entries.get(0).name, Toast.LENGTH_LONG).show();
                    break;
                case AGE:
                    break;
            }

        }
    }




}
