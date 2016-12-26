package one.path.pathonetracking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import one.path.pathonetracking.trackingservice.TrackingUtils;

public class RaceList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] raceName;
    private final String[] raceImage;
    public RaceList(Activity context,
                      String[] raceName, String[] raceImage) {
        super(context, R.layout.list_single, raceName);
        this.context = context;
        this.raceName = raceName;
        this.raceImage = raceImage;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(raceName[position]);

        // imageView.setImageResource(raceImage[position]);

        TrackingUtils.loadImageFromURL(raceImage[position],imageView);
        return rowView;
    }





}
