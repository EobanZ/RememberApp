package com.veldro.remember;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class MoviesFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRefMovies;
    private Query mSeiresQuery;

    ExpandableListView m_moviesListView;
    ArrayList<SeriesEntry> MovieEntries = new ArrayList<>();
    //ExpandableListAdapterSeries moviesAdapter;
    FloatingActionButton addNewMovieButton;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseRefMovies = mDatabase.child("users").child(String.valueOf(mAuth.getUid())).child("movies");
        mSeiresQuery = mDatabaseRefMovies.orderByChild("TimestampChanged");

        m_moviesListView = view.findViewById(R.id.moviesExpListView);
        MovieEntries.clear();

        addNewMovieButton = view.findViewById(R.id.addNewMovieButton);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
