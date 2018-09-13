package com.android.schemas.volleytest;

import android.util.Log;
import com.android.volley.Response;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class VolleyResponseListener implements Response.Listener {

    @Override
    public void onResponse(Object o) {
        String status = "";
        JSONObject response = (JSONObject) o;
        try {
            status = response.getString("status");
            if (status != null && status.toString().contains("fail"))
                Log.d("error", response.getString("status").toString());
            //else if (data != null)
            //netInfo=gson.fromJson(data.toString(), NetInfo.class);

            //else
            //netInfo=gson.fromJson(response.toString(),NetInfo.class);
            JSONArray jsonArray = response.getJSONArray("data");
            //initial utxoList
            /*
            if (!utxoList.isEmpty()) {
                utxoList.clear();
                amount = 0;
                num = 0;
                responseText.setText("");
            }*/
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Gson gson = new Gson();
                Utxo utxo = gson.fromJson(jsonObject.toString(), Utxo.class);
                if (utxo.getAsset_id().equals("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")) {
                    //  utxoList.add(utxo);
                }
                //utxoList.add(utxo);
                Log.d("utxo", utxo.toString());
            }

            Log.d("TAG", response.getString("status").toString());
        } catch (JSONException e) {
            Log.e("TAG", e.getMessage(), e);
            e.printStackTrace();
        }
        // Collections.sort(utxoList);
        // utxoAdapter = new UtxoAdapter(MainActivity.this,
        //        R.layout.utxo_item, utxoList);
        // listView.setAdapter(utxoAdapter);
        //responseText.setText(response.toString());
        Log.d("data", response.toString());
    }
}
