package com.veldro.remember;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MoviesAddDialogFragment extends DialogFragment {

    EditText nameEditText;
    Button confirmButton;
    Button cancelButton;

    public MoviesAddDialogFragment(){}

    public static MoviesAddDialogFragment newInstance(String title){
        MoviesAddDialogFragment frag = new MoviesAddDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_add_movie, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmButton = view.findViewById(R.id.confirmAddMovieButton);
        cancelButton = view.findViewById(R.id.cancelAddMovieButton);
        nameEditText = view.findViewById(R.id.moviesAddNameEditText);

        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        nameEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean cancel = false;

                if(TextUtils.isEmpty(nameEditText.getText())&& !cancel)
                {
                    cancel = true;
                    nameEditText.setError("Field Required");
                }

                if(cancel)
                    return;

                sendBackResults();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface AddMovieDialogListener{
        void onFinishAddMovieDialog(String name);
    }

    public void sendBackResults(){
        AddMovieDialogListener listener = (AddMovieDialogListener) getTargetFragment();
        listener.onFinishAddMovieDialog(nameEditText.getText().toString().toLowerCase());
        dismiss();
    }
}
