package com.multics.meatandmeet.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.multics.meatandmeet.R;
import com.multics.meatandmeet.adapters.FoodAdapter;
import com.multics.meatandmeet.app.AppConfig;
import com.multics.meatandmeet.app.AppController;
import com.multics.meatandmeet.models.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {
    private static final String TAG = FoodFragment.class.getSimpleName();
    private ListView listView;
    private FoodAdapter foodAdapter;
    private List<Food> foods;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private JsonObjectRequest jsonReq;



    public FoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_food, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)v. findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        listView = (ListView)v.findViewById(R.id.list);

        foods = new ArrayList<Food>();

        foodAdapter = new FoodAdapter(getActivity(), foods);
        listView.setAdapter(foodAdapter);
        // We first check for cached request

        Cache cachee = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cachee.get(AppConfig.ITEM_URL_FOOD);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json**/
            jsonReq = new JsonObjectRequest(Request.Method.GET,
                    AppConfig.ITEM_URL_FOOD, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

        return v;
    }


    public void parseJsonFeed(JSONObject response) {

        try {
            JSONArray feedArray = response.getJSONArray("items");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                Food food = new Food();
                food.setId(feedObj.getInt("id"));
                food.setName(feedObj.getString("name"));
                food.setPrice(feedObj.getString("price"));
                food.setPhoto(feedObj.getString("photo"));
                food.setDescription(feedObj.getString("description"));
                foods.add(food);
            }

            // notify data changes to list adapater
            foodAdapter.notifyDataSetChanged();
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
