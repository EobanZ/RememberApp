package com.veldro.remember;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;


public class SeriesAddDialogFragment extends DialogFragment {

    EditText nameEditText;
    EditText seasonEditText;
    EditText episodeEditText;
    Button confirmButton;
    Button cancellButton;

    public SeriesAddDialogFragment(){}

    public static SeriesAddDialogFragment newInstance(String title){
        SeriesAddDialogFragment frag  = new SeriesAddDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        frag.setArguments(args);
        return  frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogfragment_add_series,container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         nameEditText = view.findViewById(R.id.sieriesAddNameEditText);
         seasonEditText = view.findViewById(R.id.seriesAddSeasonEditText);
         episodeEditText = view.findViewById(R.id.seriesAddEpisodeEditText);
         confirmButton = view.findViewById(R.id.confirmAddSeriesButton);
         cancellButton = view.findViewById(R.id.cancelAddSeriesButton);

         String title = getArguments().getString("title");
         getDialog().setTitle(title);
         nameEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Check if all needed fields are valid
                boolean cancel = false;

                nameEditText.setError(null);
                episodeEditText.setError(null);
                seasonEditText.setError(null);

                final String email = nameEditText.getText().toString();
                final String season = seasonEditText.getText().toString();
                final String episode = episodeEditText.getText().toString();


                //Check for valid email adress
                if(TextUtils.isEmpty(email))
                {
                    nameEditText.setError("Field Required");
                    cancel = true;
                }

                if(TextUtils.isEmpty(season)&&!cancel){
                    seasonEditText.setError("Field Required");
                    cancel = true;
                }

                if(!isValidSeasonOrEpisode(season)&&!cancel) {
                    seasonEditText.setError("Invalid Input");
                    cancel = true;
                }

                if(TextUtils.isEmpty(episode)&&!cancel){
                    episodeEditText.setError("Field Required");
                    cancel = true;
                }

                if(!isValidSeasonOrEpisode(episode)&& !cancel){
                    episodeEditText.setError("Invalid Input");
                    cancel = true;
                }

                if(cancel)
                    return;

                sendBackResults();



            }
        });

        cancellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private boolean isValidSeasonOrEpisode(String s)
    {
        if(TextUtils.isEmpty(s))
            return false;
        int i = Integer.parseInt(s);
        Log.i("testesttest", String.valueOf(i));
        return i>=0 && i<Integer.MAX_VALUE;
    }

    public interface AddSeriesDialogListener{
        void onFinishAddSeriesDialog(String name, int season, int episode);
    }

    public void sendBackResults(){
        AddSeriesDialogListener listener = (AddSeriesDialogListener) getTargetFragment();
        listener.onFinishAddSeriesDialog(nameEditText.getText().toString(), Integer.parseInt(seasonEditText.getText().toString()), Integer.parseInt(episodeEditText.getText().toString()));
        dismiss();
    }
}
