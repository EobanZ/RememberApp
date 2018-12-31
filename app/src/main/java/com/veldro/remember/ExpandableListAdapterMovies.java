package com.veldro.remember;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableListAdapterMovies extends BaseExpandableListAdapter {

    private Context m_context;
    ArrayList<MovieEntry> m_entries;
    private MoviesFragment m_fragment;

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
        viewHolder.minutesTextView = convertView.findViewById(R.id.groupItemHourMovieTextView);

        //TODO: Hier weiter machen!
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
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
    }

    private int[] MinToHour(int min){
        int h;
        int m;
        h = min / 60;
        m = min % 60;
        int[]res = {h,m};
        return  res;
    }
}
