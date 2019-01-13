package com.veldro.remember;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExpandableListAdapterBooks extends BaseExpandableListAdapter {

    private Context m_context;
    ArrayList<BookEntry> m_entries;
    private BooksFragment m_fragment;

    public ExpandableListAdapterBooks(Context context, ArrayList<BookEntry> entries, BooksFragment fragment){
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
        final BookEntry entry = (BookEntry) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.groupitem_books, null);
        }

        groupItemViewHolder viewHolder = new groupItemViewHolder();
        viewHolder.nameTextView = convertView.findViewById(R.id.bookNameTextView);
        viewHolder.pageTextView = convertView.findViewById(R.id.groupItemPageTextView);

        viewHolder.nameTextView.setText(entry.name);
        viewHolder.pageTextView.setText("Page: " + String.valueOf(entry.page));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final BookEntry entry = (BookEntry) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_books, null);
        }

        final listItemViewHolder viewHolder = new listItemViewHolder();
        viewHolder.newPageEditText = convertView.findViewById(R.id.pageListItemEditText);
        viewHolder.setPageButton = convertView.findViewById(R.id.setPageListItemButton);

        viewHolder.newPageEditText.setText(String.valueOf(entry.page));
        viewHolder.setPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(viewHolder.newPageEditText.getText())){
                    viewHolder.newPageEditText.setError("Field Requiered");
                    return;
                }
                int p = Integer.parseInt(viewHolder.newPageEditText.getText().toString());
                m_fragment.setPage(p, entry);
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
        TextView pageTextView;
    }

    static class listItemViewHolder
    {
        Button setPageButton;
        EditText newPageEditText;
    }
}
