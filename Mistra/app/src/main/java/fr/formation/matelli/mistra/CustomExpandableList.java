package fr.formation.matelli.mistra;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by matelli on 22/10/2014.
 */
public class CustomExpandableList extends BaseExpandableListAdapter {

    private Context context;
    private HashMap<String, List<String>> listTitres;

    //private List<String> listDataHeader; // header titles
    /* private TreeMap<String, List<String>> listDataChild; */
    //private TreeMap<String, List<String>> listDataChild;

    public CustomExpandableList(Context context, HashMap<String, List<String>> listTitres) {
        this.context = context;
        this.listTitres = listTitres;
    }

    /*public CustomExpandableList(Context context, List<String> listDataHeader) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    /* public CustomExpandableList(Context context,  TreeMap<String, List<String>> listChildData) {*/
    /* public CustomExpandableList(Context context,  TreeMap<String, List<String>> listChildData) {
        this.context = context;
        this.listDataHeader = new ArrayList<String>();
        for(String entry : listChildData.keySet() ){
            this.listDataHeader.add(entry);
          //  Log.e("==> titre :",entry);

        }
        this.listDataChild = listChildData;
    } */

    @Override
    public int getGroupCount() {
        return this.listTitres.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int cpt=0;
        for (HashMap.Entry<String, List<String>> entry : listTitres.entrySet())
        {
            if(cpt==groupPosition) {
                return entry.getValue().size();
            }
            cpt++;
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        int cpt=0;
        for (HashMap.Entry<String, List<String>> entry : listTitres.entrySet())
        {
            if(cpt==groupPosition) {
                return entry.getKey();
            }
            cpt++;
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        int cpt=0;
        for (HashMap.Entry<String, List<String>> entry : listTitres.entrySet())
        {
            if(cpt==groupPosition) {
                return entry.getValue().get(childPosition);
            }
            cpt++;
        }
        return null;
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header_item_for_list, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.headerList);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.simple_item_list, null);
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.itemSubList);
        txtListChild.setText(childText);
        return convertView;


    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
