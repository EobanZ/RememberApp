package com.veldro.remember;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class ExpandableListAdapterSeries extends BaseExpandableListAdapter {

    private Context m_context;
    ArrayList<SeriesEntry> m_entries;
    private SeriesFragment m_fragment;


    public ExpandableListAdapterSeries(Context context, ArrayList<SeriesEntry> entries)
    {
        m_context = context;
        m_entries = entries;

    }

    public ExpandableListAdapterSeries(Context context, ArrayList<SeriesEntry> entries, SeriesFragment fragment)
    {
        m_context = context;
        m_entries = entries;
        m_fragment = fragment;
    }

    @Override
    public int getGroupCount() {
        return m_entries.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return m_entries.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.m_entries.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final SeriesEntry entry = (SeriesEntry) getGroup(i);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.groupitem_series, null);
        }
        grouItemViewHolder grouItemHolder = new grouItemViewHolder();
        grouItemHolder.nameTextView = view.findViewById(R.id.seriesNameTextView);
        grouItemHolder.seasonTextView = view.findViewById(R.id.groupItemSeasonSeriesTextView);
        grouItemHolder.episodeTextView = view. findViewById(R.id.groupItemEpisodeSeriesTextView);

        grouItemHolder.nameTextView.setText(entry.name);
        grouItemHolder.seasonTextView.setText("S: " +String.valueOf(entry.season));
        grouItemHolder.episodeTextView.setText("E: " +String.valueOf(entry.episode));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final SeriesEntry entry = (SeriesEntry) getChild(i, i1);

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listitem_series, null);
        }
        listItemViewHolder listItemViewHolder = new listItemViewHolder();
        listItemViewHolder.decEpisodeButton = view.findViewById(R.id.decEpisodeSeriesButton);
        listItemViewHolder.decSeasonButton = view.findViewById(R.id.decSeasonSeriesButton);
        listItemViewHolder.incEpisodeButton = view.findViewById(R.id.incEpisodeSeriesButton);
        listItemViewHolder.incSeasonButton = view.findViewById(R.id.incSeasonSeriesButton);
        listItemViewHolder.episodeCountTextView = view.findViewById(R.id.countEpisodeTextView);
        listItemViewHolder.seasonCountTextView = view.findViewById(R.id.countSeriesTextView);

        listItemViewHolder.episodeCountTextView.setText(String.valueOf(entry.episode));
        listItemViewHolder.seasonCountTextView.setText(String.valueOf(entry.season));

        listItemViewHolder.incSeasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_fragment.IncSeasonSeriesEntry(entry);
                //Notify Dataset Changed im Firebase Listener
            }
        });

        listItemViewHolder.incEpisodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_fragment.IncEpisodeSeriesEntry(entry);
            }
        });

        listItemViewHolder.decSeasonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_fragment.DecSeasonSeriesEntry(entry);
            }
        });

        listItemViewHolder.decEpisodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_fragment.DecEpisodeSeriesEntry(entry);
                //entry.decEpisode();
                //notifyDataSetChanged();
            }
        });



        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class grouItemViewHolder
    {
        TextView nameTextView;
        TextView seasonTextView;
        TextView episodeTextView;
    }

    static class listItemViewHolder
    {
        Button incSeasonButton;
        Button decSeasonButton;
        Button incEpisodeButton;
        Button decEpisodeButton;
        TextView seasonCountTextView;
        TextView episodeCountTextView;
    }


}
