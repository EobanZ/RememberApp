package com.veldro.remember;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class AudioBooksFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRefAudioBooks;
    private Query mAudioBooksQuery;

    ExpandableListView m_audiobooksListView;
    ArrayList<AudioBookEntry> AudioBookEntries = new ArrayList<>();
    ExpandableListAdapterAudioBooks audiobooksAdapter;
    FloatingActionButton addNewAudioBookButton;

    public AudioBooksFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audiobooks,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseRefAudioBooks = mDatabase.child("users").child(String.valueOf(mAuth.getUid())).child("audiobooks");
        mAudioBooksQuery = mDatabaseRefAudioBooks.orderByChild("TimestampChanged");

        audiobooksAdapter = new ExpandableListAdapterAudioBooks(getContext(),AudioBookEntries, this);

        m_audiobooksListView = view.findViewById(R.id.audioBooksExpListView);

        m_audiobooksListView.setAdapter(audiobooksAdapter);
        m_audiobooksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteAudioBookDialog(position);
                return false;
            }
        });

        addNewAudioBookButton = view.findViewById(R.id.addNewAudioBookButton);
        addNewAudioBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAudioBookDialog();
            }
        });

        AudioBookEntries.clear();

        mAudioBooksQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AudioBookEntries.clear();
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    AudioBookEntries.add(dsp.getValue(AudioBookEntry.class));
                }
                Collections.reverse(AudioBookEntries);
                audiobooksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addNewAudioBook(String userId, String name, int chapter){
        if(userId == null)
            return;
        AudioBookEntry entry = new AudioBookEntry(name, chapter);
        mDatabaseRefAudioBooks.child(entry.name).setValue(entry);
    }

    private void addNewAudioBook(String userId, String name){
        this.addNewAudioBook(userId,name,0);
    }

    public void incChapter(AudioBookEntry entry){
        entry.incChapter();
        mDatabaseRefAudioBooks.child(entry.name).setValue(entry);
    }

    public void decChapter(AudioBookEntry entry){
        entry.decChapter();
        mDatabaseRefAudioBooks.child(entry.name).setValue(entry);
    }

    private void deleteEntry(int position){
        mDatabaseRefAudioBooks.child(AudioBookEntries.get(position).name).setValue(null);
    }

    private void showAddAudioBookDialog(){
        final LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialogfragment_add_audiobook, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Add Audio Book");

        final EditText etAudioBookName = (EditText) view.findViewById(R.id.audiobookNameAddEditText);
        final EditText etChapter = (EditText) view.findViewById(R.id.chapterAddEditText);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(TextUtils.isEmpty(etChapter.getText().toString())){
                    addNewAudioBook(mAuth.getUid(), etAudioBookName.getText().toString());
                }
                else
                    addNewAudioBook(mAuth.getUid(),  etAudioBookName.getText().toString(), Integer.parseInt(etChapter.getText().toString()));
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void showDeleteAudioBookDialog(final int position){
        String s = "Do you want to delete " + AudioBookEntries.get(position).name + "?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(s)
                .setTitle("Delete")
                .setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEntry(position);
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
}
