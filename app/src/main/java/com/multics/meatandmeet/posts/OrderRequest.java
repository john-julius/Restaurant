package com.multics.meatandmeet.posts;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.multics.meatandmeet.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderRequest extends StringRequest {
    private Map<String,String> params;
    private JSONObject jsonObj;




    public OrderRequest(JSONObject seatss, JSONArray items, Response.Listener<String> listener){
        super(Method.POST, AppConfig.ITEM_URL,listener,null);
        Log.v("DATA :","zinaenda");

        params = new HashMap<>();
        params.put("items", String.valueOf(items));
        params.put("seats", String.valueOf(seatss));
        Log.v("data sent", String.valueOf(params));
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }

}
