package com.veldro.remember;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableListAdapterMovies extends BaseExpandableListAdapter {

    public enum TimeMode{
        minutes,
        hours
    }

    private Context m_context;
    ArrayList<MovieEntry> m_entries;
    private MoviesFragment m_fragment;
    private TimeMode timeMode = TimeMode.minutes;

    public ExpandableListAdapterMovies(Context context, ArrayList<MovieEntry> entries, MoviesFragment fragment){
        this.m_context = context;
        this.m_entries = entries;
        this.m_fragment = fragment;
    }

    @Override
    public int getGroupCount() {
        return m_entries.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return m_entries.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return m_entries.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final MovieEntry entry = (MovieEntry) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.groupitem_movies, null);
        }
        groupItemViewHolder viewHolder = new groupItemViewHolder();
        viewHolder.nameTextView = convertView.findViewById(R.id.movieNameTextView);
        viewHolder.minutesTextView = convertView.findViewById(R.id.groupItemMinuteMovieTextView);
        viewHolder.hoursTextView = convertView.findViewById(R.id.groupItemHourMovieTextView);


        viewHolder.nameTextView.setText(entry.name);
        viewHolder.minutesTextView.setText("Min:" +Integer.toString(entry.minutes));
        viewHolder.hoursTextView.setText("Time: " + Integer.toString(entry.minutes/60)+":"+ checkMinLenth(entry.minutes % 60));
        return convertView;
    }

    private String checkMinLenth(int i)
    {
        if (i<10)
            return "0"+String.valueOf(i);
        else
            return String.valueOf(i);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final MovieEntry entry = (MovieEntry) getChild(groupPosition,childPosition);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_movies, null);
        }

        final listItemViewHolder viewHolder = new listItemViewHolder();
        viewHolder.movieRadioGroup = convertView.findViewById(R.id.movieRadioGroup);
        viewHolder.minutesRadioButton = convertView.findViewById(R.id.minutesRadioButton);
        viewHolder.hoursRadioButton = convertView.findViewById(R.id.hoursRadioButton);
        viewHolder.setSeasonButton = convertView.findViewById(R.id.setMinutesHoursButton);
        viewHolder.newTimeEditText = convertView.findViewById(R.id.minutesHoursEditText);

        viewHolder.movieRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioId = group.getCheckedRadioButtonId();
                switch (checkedRadioId){
                    case R.id.minutesRadioButton:
                        timeMode = TimeMode.minutes;
                        viewHolder.newTimeEditText.setText("0");
                        viewHolder.newTimeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        break;
                    case R.id.hoursRadioButton:
                        timeMode = TimeMode.hours;
                        viewHolder.newTimeEditText.setText("0.00");
                        viewHolder.newTimeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED );
                        break;
                    default:
                        break;

                }
            }
        });

        viewHolder.newTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.newTimeEditText.setText("");
            }
        });

        viewHolder.setSeasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = viewHolder.newTimeEditText.getText().toString();

                switch (timeMode) {
                    case minutes:
                        int m = Integer.parseInt(input);
                        if(m < 0)
                            m = m * -1;
                        m_fragment.setMinutes(m, entry);
                        break;
                    case hours:
                        float h = Float.parseFloat(input);
                        if(h < 0)
                            h = h * -1;
                        m_fragment.setMinutes(h, entry);
                        break;
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class groupItemViewHolder
    {
        TextView nameTextView;
        TextView minutesTextView;
        TextView hoursTextView;
    }

    static class listItemViewHolder
    {
        Button setSeasonButton;
        RadioGroup movieRadioGroup;
        RadioButton minutesRadioButton;
        RadioButton hoursRadioButton;
        EditText newTimeEditText;
    }

}
