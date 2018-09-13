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
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

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
    private Button buildUtxo;
    private TextView responseText;
    private RequestQueue mRequestQueue;
    private ListView listView;
    private UtxoAdapter utxoAdapter;
    private long amount;
    private int num;
    private VolleyResponseErrorListener volleyResponseErrorListener;
private String result;
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
        buildUtxo = (Button) findViewById(R.id.build_utxo);
        listView = (ListView) findViewById(R.id.list_view);


        responseText = (TextView) findViewById(R.id.text_response);
        sendRequest.setOnClickListener(this);
        buildUtxo.setOnClickListener(this);
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
                responseText.setText("一共选了" + num + "项," + "总额是" + amount + "BTM");
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.build_utxo:
// 在此处添加逻辑
                Log.d("okaaa", "okllllllllllllllllllllllllllllllllllllllllll");
                SendBuildRequest();
                break;
            case R.id.send_request:
                SendQueryRequest();
// 在此处添加逻辑
                break;
            default:
                break;
        }

    }

    public void SendBuildRequest() {

        getRequestQueue().add(buildMergeRequest());
    }

    public void SendQueryRequest() {

        getRequestQueue().add(buildQueryJsonRequest());
    }
    public void SignTransactionRequest(String action, JSONObject sourceObject) {

        getRequestQueue().add(buildSignJsonRequest(action, sourceObject ));
    }
    public void SubmitTransactionRequest(String action, JSONObject sourceObject){
        getRequestQueue().add(buildSubmitJsonRequest(action, sourceObject ));
    }
    public JsonObjectRequest buildSubmitJsonRequest(String action, JSONObject sourceObject ){
        String reqURL = "http://192.168.1.102:9888/"+action;
        JSONObject jsonObject = buildSubmitJson(sourceObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reqURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("getResponse", response.toString());
                        String status = "";
                        try {
                            status = response.getString("status");
                            if (status != null && status.toString().contains("fail"))
                                Log.d("error", response.getString("status").toString());
                            responseText.setText(result+"   submit  "+response.toString());
                            JSONObject responseJson = response.getJSONObject("data");


                            Log.d("TAG", response.getString("status").toString());
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                            e.printStackTrace();
                        }

                        Log.d("submitdata", response.toString());
                    }
                }, volleyResponseErrorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }
    };
        return jsonObjectRequest;
    }
public  JsonObjectRequest buildSignJsonRequest(String action,JSONObject sourceObject){
    String reqURL = "http://192.168.1.102:9888/"+action;
    JSONObject jsonObject = buildSignJson(sourceObject);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reqURL, jsonObject,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.d("getResponse", response.toString());
                    String status = "";
                    try {
                        status = response.getString("status");
                        if (status != null && status.toString().contains("fail"))
                            Log.d("error", response.getString("status").toString());
                        result+="   sign  "+status;
                        JSONObject responseJson = response.getJSONObject("data");
                        SubmitTransactionRequest("submit-transaction", responseJson);

                        Log.d("TAG", response.getString("status").toString());
                    } catch (JSONException e) {
                        Log.e("TAG", e.getMessage(), e);
                        e.printStackTrace();
                    }

                    Log.d("signdata", response.toString());
                }
            }, volleyResponseErrorListener) {
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return setHeaders();
        }
    };
    return jsonObjectRequest;
}
    public JsonObjectRequest buildQueryJsonRequest() {
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
                                if (utxo.getAsset_id().equals("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")&&utxo.getValid_height()>100) {
                                    utxoList.add(utxo);
                                }
                                //utxoList.add(utxo);
                               // Log.d("utxo", utxo.toString());
                            }

                           // Log.d("TAG", response.getString("status").toString());
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                            e.printStackTrace();
                        }
                        Collections.sort(utxoList);
                        utxoAdapter = new UtxoAdapter(MainActivity.this,
                                R.layout.utxo_item, utxoList);
                        listView.setAdapter(utxoAdapter);
                        //responseText.setText(response.toString());
                        //Log.d("data", response.toString());
                    }
                }, volleyResponseErrorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }
        };
        return jsonObjectRequest;
    }

    public JsonObjectRequest buildMergeRequest() {
        String reqURL = "http://192.168.1.102:9888/build-transaction";
        JSONObject jsonObject = buildMergeJson();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reqURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("getResponse", response.toString());
                        String status = "";
                        try {
                            status = response.getString("status");
                            if (status != null && status.toString().contains("fail"))
                                Log.d("error", response.getString("status").toString());
                            result="build  "+status;
                            JSONObject responseJson = response.getJSONObject("data");
                            SignTransactionRequest("sign-transaction", responseJson);

                           // Log.d("TAG", response.getString("status").toString());
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                            e.printStackTrace();
                        }

                        Log.d("signdata", response.toString());
                    }
                }, volleyResponseErrorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }
        };
        return jsonObjectRequest;
    }
public JSONObject buildSignJson(JSONObject sourceObject){
    JSONObject jsonObject = new JSONObject();

    try {
        jsonObject.put("password","test1");
        jsonObject.put("transaction",sourceObject);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return jsonObject;
}
public JSONObject buildSubmitJson(JSONObject sourceObject){
    JSONObject responseJson = null;
    try {
        responseJson = sourceObject.getJSONObject("transaction");
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return responseJson;
}
    public JSONObject buildMergeJson() {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonoarray = new JSONArray();
        JSONObject requestObject = new JSONObject();
        String address = "";
        for (Utxo utxo : utxoList) {
            //Log.d("mergeUtxo", utxo.Use_it() + "");
            if (utxo.Use_it()) {
                                try {
                    jsonObject = new JSONObject(gson.toJson(new SpendUtxo("spend_account_unspent_output",utxo.getId()
                    ), SpendUtxo.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("utxo", jsonObject.toString());
                jsonoarray.put(jsonObject);
            }

        }

        try {
            jsonObject = new JSONObject(gson.toJson(new ControlAddress("sm1qm78e4sx7cduf9qg680dsqrrhazgss9x7t3z062", amount-100000000, "control_address", "BTM"), ControlAddress.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonoarray.put(jsonObject);
        try {
            requestObject.put("actions", jsonoarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("requestObject", requestObject.toString());
        return requestObject;
    }

    private Map<String, String> setHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        String encoding = "";
        encoding = new String(Base64.encodeBase64(("tester:d4d21b5e786a782b2c65013fc46373ed719fc0a4a52aae23d7cfefaa00db7e56").getBytes()));

        String authorization = "Basic " + encoding;
        headers.put("Authorization", authorization);
        return headers;
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



