package com.multics.meatandmeet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.multics.meatandmeet.R;
import com.multics.meatandmeet.models.SoftDrink;

import java.util.List;

public class SoftDrinkAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<SoftDrink> softDrinks;
    private SoftDrinkAdapter.IItemAdapterCallback itemCallback;

    public SoftDrinkAdapter(Context context, List<SoftDrink> softDrinks) {
        this.context = context;
        this.softDrinks = softDrinks;
        itemCallback = (SoftDrinkAdapter.IItemAdapterCallback) context;
    }

    public interface IItemAdapterCallback
    {
        void onItemCallback(SoftDrink softDrink);

        void onAddItemCallback(ImageView imageView, SoftDrink softDrink);
    }

    @Override
    public int getCount() {
        return softDrinks.size();
    }

    @Override
    public Object getItem(int location) {
        return softDrinks.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.feed_item, null);
        }


        return convertView;

    }

}
