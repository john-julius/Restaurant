package com.multics.meatandmeet.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.multics.meatandmeet.R;
import com.multics.meatandmeet.adapters.AlcoholAdapter;
import com.multics.meatandmeet.app.AppConfig;
import com.multics.meatandmeet.app.AppController;
import com.multics.meatandmeet.models.Alcohol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlcoholFragment extends Fragment {

    private static final String TAG = AlcoholFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private AlcoholAdapter alcoholAdapter;
    private List<Alcohol> alcohols;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private JsonObjectRequest jsonReq;


    public AlcoholFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alcohol, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)v. findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = (RecyclerView) v.findViewById(R.id.list);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGridLayoutManager);

        alcohols = new ArrayList<Alcohol>();

        alcoholAdapter = new AlcoholAdapter(getActivity(), alcohols);
        recyclerView.setAdapter(alcoholAdapter);
        // We first check for cached request

        Cache cachee = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cachee.get(AppConfig.ITEM_URL_ALCOHOLS);
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
                    AppConfig.ITEM_URL_ALCOHOLS, null, new Response.Listener<JSONObject>() {


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

                Alcohol alcohol = new Alcohol();
                alcohol.setName(feedObj.getString("name"));
                alcohol.setPrice(feedObj.getString("price"));
                alcohol.setPhoto(feedObj.getString("photo"));
                alcohol.setDescription(feedObj.getString("description"));
                alcohols.add(alcohol);
            }

            // notify data changes to list adapater
            alcoholAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
