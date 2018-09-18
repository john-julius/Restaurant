package com.multics.meatandmeet.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.multics.meatandmeet.R;
import com.multics.meatandmeet.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.PlaceViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Item> items;
    private IItemAdapterCallback itemCallback;


    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
        itemCallback = (IItemAdapterCallback) context;
    }

    public interface IItemAdapterCallback {
        void onItemCallback(Item item);

        void onAddItemCallback(ImageView imageView, Item item);
    }


    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item,
                parent, false);

        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaceViewHolder holder, int position) {
        final Item item = items.get(position);

        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());
        holder.description.setText(item.getDescription());
        Glide.with(context)
                .load(item.getPhoto())
                .into(holder.feedImageView);
        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemCallback != null)
                {
                    //final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.scale);
                    //   holder.imgAdd.startAnimation(myAnim);

                    itemCallback.onAddItemCallback(holder.feedImageView, item);
                }

            }
        });


    }


    @Override
    public long getItemId(int position) {
        return items.size();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, description;
        ImageView feedImageView;
        Button add_cart;

        public PlaceViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.descrption);
            feedImageView = itemView.findViewById(R.id.photo);
            add_cart = itemView.findViewById(R.id.add_cart);



        }


    }


}

