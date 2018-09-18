package com.multics.meatandmeet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multics.meatandmeet.R;
import com.multics.meatandmeet.models.Food;

import java.util.List;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Food> foods;
    private FoodAdapter.IItemAdapterCallback itemCallback;



    public FoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
        itemCallback = (FoodAdapter.IItemAdapterCallback) context;
    }

    public interface IItemAdapterCallback
    {
        void onItemCallback(Food food);

        void onAddItemCallback(ImageView imageView, Food food);
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int location) {
        return foods.get(location);
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
            convertView = mInflater.inflate(R.layout.new_item, null);

        }
        final Food item = foods.get(position);


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView descrption = (TextView) convertView.findViewById(R.id.descrption);
        TextView price = (TextView) convertView
                .findViewById(R.id.price);

        name.setText(item.getName());
        descrption.setText(item.getDescription());
        price.setText(item.getPrice());
        final ImageView feedImageView =  convertView
                .findViewById(R.id.photo);

        Glide.with(context)
                .load(item.getPhoto())
                .into(feedImageView);

        // Feed image
//        if (item.getPhoto() != null) {
//            feedImageView.setImageUrl(item.getPhoto(), imageLoader);
//            feedImageView.setVisibility(View.VISIBLE);
//            feedImageView
//                    .setResponseObserver(new FeedImageView.ResponseObserver() {
//                        @Override
//                        public void onError() {
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                        }
//                    });
//        } else {
//            feedImageView.setVisibility(View.GONE);
//        }

        Button add_cart = convertView.findViewById(R.id.add_cart);
        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemCallback != null)
                {
                    //final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.scale);
                    //   holder.imgAdd.startAnimation(myAnim);

                    itemCallback.onAddItemCallback(feedImageView, item);
                }

            }
        });

        return convertView;

    }


}


