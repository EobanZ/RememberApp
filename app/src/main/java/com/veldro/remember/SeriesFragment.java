package com.veldro.remember;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SeriesFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRefSeries;

    private OnFragmentInteractionListener mListener;

    ExpandableListView m_seriesListView;
    ArrayList<SeriesEntry> SeriesEntrys = new ArrayList<>();
    ExpandableListAdapterSeries seriesAdapter;

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

        //Fill Arraylist whit Cloud Data
        mDatabaseRefSeries.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SeriesEntrys.clear();
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    SeriesEntrys.add(dsp.getValue(SeriesEntry.class));

                }
                seriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void addNewSeries(String userId, String name){
        if(userId == null)
            return;
        SeriesEntry newEntry = new SeriesEntry(name);
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


}
