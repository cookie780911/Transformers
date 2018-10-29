package com.kevinhuang.ServerUtil;

import java.lang.*;
import java.io.*;
import java.util.*;

import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TransformerRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private JsonObject requestBody = null;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public TransformerRequest(int type, String url, Class<T> clazz, Map<String, String> headers, JsonObject body,
                              Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(type, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.requestBody = body;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return requestBody != null ? gson.toJson(requestBody).getBytes() : null;
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }
}