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
import com.multics.meatandmeet.models.Alcohol;

import java.util.List;

public class AlcoholAdapter  extends  RecyclerView.Adapter<AlcoholAdapter.PlaceViewHolder>  {
    private Context context;
    private List<Alcohol> alcohols;
    private IItemAdapterCallback itemCallback;

    public AlcoholAdapter(Context context, List<Alcohol> alcohols) {
        this.context = context;
        this.alcohols = alcohols;
        itemCallback = (AlcoholAdapter.IItemAdapterCallback) context;
    }

    public interface IItemAdapterCallback
    {
        void onItemCallback(Alcohol alcohol);

        void onAddItemCallback(ImageView imageView, Alcohol alcohol);
    }


    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alcohol_feed,
                parent, false);

        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaceViewHolder holder, int position) {

        final Alcohol alcohol= alcohols.get(position);

        holder.name.setText(alcohol.getName());
        holder.price.setText(alcohol.getPrice());
        holder.description.setText(alcohol.getDescription());
        Glide.with(context)
                .load(alcohol.getPhoto())
                .into(holder.feedImageView);
        holder.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemCallback != null)
                {
                    //final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.scale);
                    //   holder.imgAdd.startAnimation(myAnim);

                    itemCallback.onAddItemCallback(holder.feedImageView, alcohol);
                }

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return alcohols.size();
    }

    @Override
    public int getItemCount() {
        return alcohols.size();
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder {

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

