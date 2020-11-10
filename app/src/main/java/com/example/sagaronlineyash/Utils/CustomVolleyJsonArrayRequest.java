package com.example.sagaronlineyash.Utils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpHeaderParser;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;


public class CustomVolleyJsonArrayRequest extends Request<JSONArray> {

    private com.android.volley.Response.Listener<JSONArray> listener;
    private java.util.Map<String, String> params;

    public CustomVolleyJsonArrayRequest(String url, java.util.Map<String, String> params,
                                        com.android.volley.Response.Listener<JSONArray> reponseListener, com.android.volley.Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomVolleyJsonArrayRequest(int method, String url, java.util.Map<String, String> params,
                                        com.android.volley.Response.Listener<JSONArray> reponseListener, com.android.volley.Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    protected java.util.Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected com.android.volley.Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return com.android.volley.Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return com.android.volley.Response.error(new ParseError(e));
        } catch (JSONException je) {
            return com.android.volley.Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONArray response) {
        // TODO Auto-generated method stub
        listener.onResponse(response);
    }
}
