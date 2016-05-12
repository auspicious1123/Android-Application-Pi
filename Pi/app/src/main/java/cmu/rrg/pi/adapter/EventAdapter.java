package cmu.rrg.pi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cmu.rrg.pi.R;
import cmu.rrg.pi.model.EventLite;
import cmu.rrg.pi.model.Meetup;

/**
 * Created by zhangyihe on 4/4/16.
 * this class used to apaptor event class shown as list
 */
public class EventAdapter extends ArrayAdapter<Meetup> {
    private int hostInfoId;

    public EventAdapter(Context context, int textViewHostInfoId, List<Meetup>objects) {
        super(context, textViewHostInfoId, objects);
        this.hostInfoId = textViewHostInfoId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Meetup meetup = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(hostInfoId, null);
        ImageView hostImage = (ImageView) view.findViewById(R.id.host_image);
        TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        TextView eventContent = (TextView) view.findViewById(R.id.event_content);
        TextView eventTime = (TextView) view.findViewById(R.id.event_time);
        eventTitle.setText(meetup.getName());
        eventContent.setText(meetup.getContent());
        eventTime.setText(meetup.getStartTime());
        return view;
    }
}
