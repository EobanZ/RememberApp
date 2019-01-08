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

public class BooksFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseRefBooks;
    private Query mBooksQuery;

    ExpandableListView m_booksListView;
    ArrayList<BookEntry> BookEntries = new ArrayList<>();
    ExpandableListAdapterBooks booksAdapter;
    FloatingActionButton addNewBookButton;

    public BooksFragment(){
        // Required empty public constructor
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseRefBooks = mDatabase.child("users").child(String.valueOf(mAuth.getUid())).child("books");
        mBooksQuery = mDatabaseRefBooks.orderByChild("TimestampChanged");

        booksAdapter = new ExpandableListAdapterBooks(getContext(),BookEntries, this);


        m_booksListView = view.findViewById(R.id.booksExpListView);
        m_booksListView.setAdapter(booksAdapter);
        m_booksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteBookDialog(position);
                return false;
            }
        });

        addNewBookButton = view.findViewById(R.id.addNewBookButton);
        addNewBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddBookDialog();
            }
        });

        BookEntries.clear();

        mBooksQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BookEntries.clear();
                for(DataSnapshot dsp: dataSnapshot.getChildren()){
                    BookEntries.add(dsp.getValue(BookEntry.class));
                }
                Collections.reverse(BookEntries);
                booksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addNewBook(String userId, String name){
        addNewBook(userId, name, 0);
    }

    private void addNewBook(String userId, String name, int page){
        if(userId == null)
            return;
        BookEntry newEntry = new BookEntry(name, page);
        mDatabaseRefBooks.child(newEntry.name).setValue(newEntry);
    }


    private void addNewMovie(String userId, String name, int page){
        if(userId == null)
            return;
        BookEntry newEntry = new BookEntry(name,page);
        mDatabaseRefBooks.child(newEntry.name).setValue(newEntry);
    }

    public void setPage(int page, BookEntry entry){
        entry.page = page;
        mDatabaseRefBooks.child(entry.name).setValue(entry);
    }

    public void DeleteBookEntry(int position){
        mDatabaseRefBooks.child(BookEntries.get(position).name).setValue(null);
    }

    private void showAddBookDialog(){
        final LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialogfragment_add_book, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Add Book");

        final EditText etBookName = (EditText) view.findViewById(R.id.bookNameAddEditText);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addNewBook(mAuth.getUid(),  etBookName.getText().toString());
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

    private void showDeleteBookDialog(final int position){
        String s = "Do you want to delete " + BookEntries.get(position).name + "?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(s)
                .setTitle("Delete")
                .setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteBookEntry(position);
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
