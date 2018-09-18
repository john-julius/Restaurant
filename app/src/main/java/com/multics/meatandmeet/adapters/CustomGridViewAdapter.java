package com.multics.meatandmeet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.multics.meatandmeet.R;
import com.multics.meatandmeet.models.Seat;

import java.util.ArrayList;

public class CustomGridViewAdapter extends ArrayAdapter<Seat> {
    Context context;
    int layoutResourceId;
    ArrayList<Seat> data = new ArrayList<Seat>();

    public CustomGridViewAdapter(Context context, int layoutResourceId, ArrayList<Seat> data)
    {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        RecordHolder holder = null;

        try
        {
            if (row == null)
            {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new RecordHolder();
                holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
                holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
                row.setTag(holder);
            }
            else
            {
                holder = (RecordHolder) row.getTag();
            }

            Seat seat = data.get(position);
            holder.txtTitle.setText(seat.getTitle());
            holder.imageItem.setImageBitmap(seat.getImage());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return row;
    }

    public static class RecordHolder
    {
        public TextView txtTitle;
        public ImageView imageItem;

    }
}
