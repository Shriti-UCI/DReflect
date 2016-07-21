package edu.umich.si.inteco.minuku_2.view.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.umich.si.inteco.minuku_2.R;

/**
 * Created by shriti on 7/20/16.
 */
public class StableArrayAdapter extends ArrayAdapter<ActionObject> {
    public final Context context;
    public final ActionObject[] items;

    public StableArrayAdapter(Context context, ActionObject[] itemname) {
        super(context, R.layout.listitem_homescreen, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.items=itemname;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.listitem_homescreen, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(items[position].label);
        imageView.setImageResource(items[position].imageResourceId);
        return rowView;
    }

}