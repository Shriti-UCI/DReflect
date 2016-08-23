package edu.umich.si.inteco.minuku_2.preferences;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.umich.si.inteco.minuku.config.LocationPreference;
import edu.umich.si.inteco.minuku.config.SelectedLocation;
import edu.umich.si.inteco.minuku_2.R;

/**
 * Created by shriti on 8/16/16.
 */
public class LocationArrayListAdapter extends ArrayAdapter<SelectedLocation> {

    public final Context context;
    public final List<SelectedLocation> items;


    public LocationArrayListAdapter(Context context, List<SelectedLocation> objects) {
        super(context, R.layout.listitem_locationpreference, objects);
        this.context = context;
        this.items = objects;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.listitem_locationpreference, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(LocationPreference.getInstance().getLocations().get(position));
                LocationPreference.getInstance().deleteLocation(position);
            }
        });

        txtTitle.setText(items.get(position).getPlace());
        imageView.setImageResource(items.get(position).getImageResourceId());
        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void remove(SelectedLocation object) {
        super.remove(object);
        notifyDataSetChanged();
    }
}
