package com.veldro.remember;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Movie;
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
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class MoviesFragment extends Fragment implements MoviesAddDialogFragment.AddMovieDialogListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRefMovies;
    private Query mMoviesQuery;

    ExpandableListView m_moviesListView;
    ArrayList<MovieEntry> MovieEntries = new ArrayList<>();
    ExpandableListAdapterMovies moviesAdapter;
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
        mMoviesQuery = mDatabaseRefMovies.orderByChild("TimestampChanged");

        m_moviesListView = view.findViewById(R.id.moviesExpListView);
        MovieEntries.clear();

        addNewMovieButton = view.findViewById(R.id.addNewMovieButton);

        moviesAdapter = new ExpandableListAdapterMovies(getContext(), MovieEntries, this);
        m_moviesListView.setAdapter(moviesAdapter);

        m_moviesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteMovieDialog(position);
                return false;
            }
        });

        mMoviesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MovieEntries.clear();
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    MovieEntries.add(dsp.getValue(MovieEntry.class));
                }
                Collections.reverse(MovieEntries);
                moviesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addNewMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddMovieDialog();
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

    private void addNewMovie(String userId, String name){
        if(userId == null)
            return;
        MovieEntry newEntry = new MovieEntry(name);
        mDatabaseRefMovies.child(newEntry.name).setValue(newEntry);
    }

    private void addNewMovie(String userId, String name, int minutes){
        if(userId == null)
            return;
        MovieEntry newEntry = new MovieEntry(name);
        newEntry.minutes = minutes;
        mDatabaseRefMovies.child(newEntry.name).setValue(newEntry);
    }

    public void setMinutes(int minutes, MovieEntry entry){
        entry.minutes = minutes;
        mDatabaseRefMovies.child(entry.name).setValue(entry);
    }

    public void setMinutes(float time, MovieEntry entry){
        int h = ((int) time)*60;
        int m = (int) ((time + 0.001)*100)% 100;

        setMinutes(h+m, entry);
    }

    private void showDeleteMovieDialog(final int position){
        String s = "Do you want to delete " + MovieEntries.get(position).name + "?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(s)
                .setTitle("Delete")
                .setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteMovieEntry(position);
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

    public void DeleteMovieEntry(int position){
        mDatabaseRefMovies.child(MovieEntries.get(position).name).setValue(null);
    }

    private void showAddMovieDialog(){
        FragmentManager fm = getFragmentManager();
        MoviesAddDialogFragment addMovieDialog = MoviesAddDialogFragment.newInstance("Add Movie");
        addMovieDialog.setTargetFragment(MoviesFragment.this, 300);
        addMovieDialog.show(fm,"AddMovieDialog");

    }

    @Override
    public void onFinishAddMovieDialog(String name) {
        addNewMovie(mAuth.getUid(), name);
    }
}
