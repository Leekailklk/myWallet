package com.android.schemas.volleytest;
/*
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
*/

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final int SHOW_RESPONSE = 0;
    private EditText editText;
    private List<Utxo> utxoList = new ArrayList<Utxo>();
    private Button sendRequest;
    private Button calUtxo;
    private TextView responseText;
    private RequestQueue mRequestQueue;
    private ListView listView;
    private UtxoAdapter utxoAdapter;
    private long amount;
    private int num;

    /*
     private Handler handler = new Handler() {
         public void handleMessage(Message msg) {
             switch (msg.what) {
                 case SHOW_RESPONSE:
                     String response = (String) msg.obj;
 // 在这里进行UI操作，将结果显示到界面上
                     responseText.setText(response);
             }
         }
     };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //editText = (EditText) findViewById(R.id.edit_text);
        sendRequest = (Button) findViewById(R.id.send_request);
        calUtxo = (Button) findViewById(R.id.cal_utxo);
        listView = (ListView) findViewById(R.id.list_view);


        responseText = (TextView) findViewById(R.id.text_response);
        sendRequest.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 取得ViewHolder对象
                UtxoAdapter.ViewHolder viewHolder = (UtxoAdapter.ViewHolder) view.getTag();
// 改变CheckBox的状态
                viewHolder.useIt.toggle();
// 将CheckBox的选中状况记录下来
                utxoList.get(position).setUse_it(viewHolder.useIt.isChecked());
                // 刷新
                utxoAdapter.notifyDataSetChanged();
                // 调整选定条目
                if (viewHolder.useIt.isChecked() == true) {
                    num++;
                    amount += utxoList.get(position).getAmount();
                } else {
                    num--;
                    amount -= utxoList.get(position).getAmount();
                }
                // 用TextView显示
                responseText.setText("一共选了" + num + "项," + "总额是" + amount + " nuo");
            }

        });
    }

    @Override
    public void onClick(View v) {


        getRequestQueue();
        String reqURL = "http://192.168.1.102:9888/list-unspent-outputs";
        //String reqURL =editText.getText().toString();
        //Map<String, Integer> content = new HashMap<String, Integer>();
        //content.put("block_height", Integer.parseInt(editText.getText().toString()));

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject("{}");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //JSONObject gsona=null;
/*
        try {
             gsona=new JSONObject(gson.toJson(content));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        // Post params to be sent to the server
        //HashMap<String, String> params = new HashMap<String, String>();
        //params.put("block_hash", "7612c8b3379e18c32d1b108682f7e35b6c7268d2e4ae94d7b24c1f622ee6dd25");
        //params.put("assetID", "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
        //JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(reqURL,gsona,
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reqURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
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
                            if (!utxoList.isEmpty()) {
                                utxoList.clear();
                                amount = 0;
                                num = 0;
                                responseText.setText("");
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Gson gson = new Gson();
                                Utxo utxo = gson.fromJson(jsonObject.toString(), Utxo.class);
                                utxoList.add(utxo);
                                Log.d("utxo", utxo.toString());
                            }

                            Log.d("TAG", response.getString("status").toString());
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                            e.printStackTrace();
                        }
                        Collections.sort(utxoList);
                        utxoAdapter = new UtxoAdapter(MainActivity.this,
                                R.layout.utxo_item, utxoList);
                        listView.setAdapter(utxoAdapter);
                        //responseText.setText(response.toString());
                        Log.d("data", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }

        }) {


            @Override

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                String encoding="";
                encoding =new String(Base64.encodeBase64(("tester:d4d21b5e786a782b2c65013fc46373ed719fc0a4a52aae23d7cfefaa00db7e56").getBytes()));

                String authorization = "Basic " + encoding;
                headers.put("Authorization", authorization);
                //headers.put("password", "d4d21b5e786a782b2c65013fc46373ed719fc0a4a52aae23d7cfefaa00db7e56");
                return headers;
            }
        };
            /*
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("address", "sm1qm78e4sx7cduf9qg680dsqrrhazgss9x7t3z062");
                //map.put("params2", "value2");
                return map;
            }
        };*/
        mRequestQueue.add(jsonObjectRequest);
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}



