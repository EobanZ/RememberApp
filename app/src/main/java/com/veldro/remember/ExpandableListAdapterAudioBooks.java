package com.veldro.remember;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpandableListAdapterAudioBooks extends BaseExpandableListAdapter {

    private Context m_context;
    ArrayList<AudioBookEntry> m_entries;
    private AudioBooksFragment m_fragment;

    public ExpandableListAdapterAudioBooks(Context context, ArrayList<AudioBookEntry> entries, AudioBooksFragment fragment){
        m_context = context;
        m_entries = entries;
        m_fragment = fragment;
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
        final AudioBookEntry entry = (AudioBookEntry) getGroup(groupPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.groupitem_audiobooks, null);
        }

        groupItemViewHolder viewHolder = new groupItemViewHolder();
        viewHolder.nameTextView = convertView.findViewById(R.id.audiobookNameTextView);
        viewHolder.chapterTextView = convertView.findViewById(R.id.groupItemChapterTextView);

        viewHolder.nameTextView.setText(entry.name);
        viewHolder.chapterTextView.setText("Chapter: "+ String.valueOf(entry.chapter));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final AudioBookEntry entry = (AudioBookEntry) getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_audiobooks, null);
        }

        listItemViewHolder viewHolder = new listItemViewHolder();
        viewHolder.chapterCountTextView = convertView.findViewById(R.id.countChapterAudioBookTextView);
        viewHolder.incChapterButton = convertView.findViewById(R.id.incChapterAudioBookButton);
        viewHolder.decChapterButton = convertView.findViewById(R.id.decCapterAudiBookButton);

        viewHolder.chapterCountTextView.setText(String.valueOf(entry.chapter));
        viewHolder.incChapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_fragment.incChapter(entry);
            }
        });
        viewHolder.decChapterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_fragment.decChapter(entry);
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
        TextView chapterTextView;
    }

    static class listItemViewHolder
    {
        Button incChapterButton;
        Button decChapterButton;
        TextView chapterCountTextView;
    }
}
