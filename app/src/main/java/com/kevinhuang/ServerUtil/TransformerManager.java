package com.kevinhuang.ServerUtil;

import android.content.*;
import android.app.*;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kevinhuang.Utility.CommonUtil;
import com.kevinhuang.transformer.MainActivity;
import com.kevinhuang.transformer.R;
import com.kevinhuang.transformer.Transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TransformerManager {
    private static TransformerManager transformer_instance = null;

    private HashMap<String, Transformer> transformerList = new HashMap<String, Transformer>();
    private String allSparkKey = "";

    // private constructor restricted to this class itself
    private TransformerManager() {
    }

    // static method to create instance of Singleton class
    public static TransformerManager getInstance() {
        if (transformer_instance == null)
            transformer_instance = new TransformerManager();

        return transformer_instance;
    }

    // for testing purposes
    public void SetTransformerList(HashMap<String, Transformer> testList) { transformerList = testList; }
    public HashMap<String, Transformer> GetTransformerList() { return transformerList; }
    public boolean IsAllSparkKeyReady() {
        return !allSparkKey.isEmpty();
    }

    public void getAllSparkKey(Activity ctx, final boolean reset) {
        final SharedPreferences sharedPref = ctx.getPreferences(Context.MODE_PRIVATE);
        allSparkKey = sharedPref.getString(CommonUtil.ALL_SPARK, "");

        if (allSparkKey.isEmpty() || reset) {
            RequestQueue queue = Volley.newRequestQueue(ctx);
            final Context context = ctx;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, CommonUtil.ALL_SPARK_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            allSparkKey = response;
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(CommonUtil.ALL_SPARK, allSparkKey);
                            editor.commit();

                            if (reset) {
                                transformerList.clear();

                                Intent addedEditedIntent = new Intent(CommonUtil.INTENT_FILTER_COMMUNICATION);
                                addedEditedIntent.putExtra(CommonUtil.INTENT_TRANSFORMERS_UPDATED, true);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(addedEditedIntent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showFailAlertDialog(error, context, R.string.msg_allspark_not_ready, "AllSpark");
                        }
                    }
            );

            queue.add(stringRequest);
        }
    }

    public void createTransformer(Activity ctx, JsonObject data) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        final Activity context = ctx;

        Map<String, String> header = new HashMap<>();
        header.put(CommonUtil.HEADER_AUTH, "Bearer " + allSparkKey);
        header.put(CommonUtil.HEADER_CONTENT_TYPE, CommonUtil.HEADER_CONTENT_TYPE_VALUE);

        TransformerRequest request = new TransformerRequest(Request.Method.POST, CommonUtil.TRANSFORMER_URL, JsonObject.class, header, data,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        Transformer newTR = new Gson().fromJson(response, Transformer.class);
                        transformerList.put(newTR.id, newTR);

                        Intent addedEditedIntent = new Intent(CommonUtil.INTENT_FILTER_COMMUNICATION);
                        addedEditedIntent.putExtra(CommonUtil.INTENT_TRANSFORMERS_UPDATED, true);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(addedEditedIntent);

                        context.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showFailAlertDialog(error, context, R.string.msg_fail_create, "CreateTransformer");
                    }
                }
        );

        queue.add(request);
    }

    public void getTransformers(Activity ctx) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        final Context context = ctx;

        Map<String, String> header = new HashMap<>();
        header.put(CommonUtil.HEADER_AUTH, "Bearer " + allSparkKey);
        header.put(CommonUtil.HEADER_CONTENT_TYPE, CommonUtil.HEADER_CONTENT_TYPE_VALUE);

        TransformerRequest request = new TransformerRequest(Request.Method.GET, CommonUtil.TRANSFORMER_URL, JsonObject.class, header, null,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        transformerList.clear();
                        JsonArray trArray = (JsonArray)response.get(CommonUtil.TRANSFORMERS);

                        for (JsonElement transformer : trArray) {
                            JsonObject transformerObj = transformer.getAsJsonObject();
                            Transformer tf = new Gson().fromJson(transformerObj, Transformer.class);
                            transformerList.put(tf.id, tf);
                        }

                        Intent updatedIntent = new Intent(CommonUtil.INTENT_FILTER_COMMUNICATION);
                        updatedIntent.putExtra(CommonUtil.INTENT_TRANSFORMERS_UPDATED, true);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(updatedIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showFailAlertDialog(error, context, R.string.msg_fail_retrieve, "GetTransformer");
                    }
                }
        );

        queue.add(request);
    }

    public void editTransformer(Activity ctx, JsonObject data) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        final Activity context = ctx;

        Map<String, String> header = new HashMap<>();
        header.put(CommonUtil.HEADER_AUTH, "Bearer " + allSparkKey);
        header.put(CommonUtil.HEADER_CONTENT_TYPE, CommonUtil.HEADER_CONTENT_TYPE_VALUE);

        TransformerRequest request = new TransformerRequest(Request.Method.PUT, CommonUtil.TRANSFORMER_URL, JsonObject.class, header, data,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        Transformer newTR = new Gson().fromJson(response, Transformer.class);
                        transformerList.put(newTR.id, newTR);

                        Intent addedEditedIntent = new Intent(CommonUtil.INTENT_FILTER_COMMUNICATION);
                        addedEditedIntent.putExtra(CommonUtil.INTENT_TRANSFORMERS_UPDATED, true);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(addedEditedIntent);

                        context.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showFailAlertDialog(error, context, R.string.msg_fail_edit, "EditTransformer");
                    }
                }
        );

        queue.add(request);
    }

    public void deleteTransformer(Context ctx, String id) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        final Context context = ctx;
        final String deletedID = id;

        Map<String, String> header = new HashMap<>();
        header.put(CommonUtil.HEADER_AUTH, "Bearer " + allSparkKey);
        header.put(CommonUtil.HEADER_CONTENT_TYPE, CommonUtil.HEADER_CONTENT_TYPE_VALUE);

        TransformerRequest request = new TransformerRequest(Request.Method.DELETE, CommonUtil.TRANSFORMER_URL + "/" + id + "", JsonObject.class, header, null,
                new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        transformerList.remove(deletedID);

                        Intent deletedIntent = new Intent(CommonUtil.INTENT_FILTER_COMMUNICATION);
                        deletedIntent.putExtra(CommonUtil.INTENT_TRANSFORMERS_UPDATED, true);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(deletedIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showFailAlertDialog(error, context, R.string.msg_delete, "DeleteTransformer");
                    }
                }
        );

        queue.add(request);
    }

    public int startBattle() {
        ArrayList<Transformer> autobots = new ArrayList<Transformer>();
        ArrayList<Transformer> decepticons = new ArrayList<Transformer>();

        for (Transformer tr : transformerList.values()) {
            if (tr.team == CommonUtil.TR_ATTR_TEAM_DECEPTICON)
                decepticons.add(tr);
            else if (tr.team == CommonUtil.TR_ATTR_TEAM_AUTOBOT)
                autobots.add(tr);
            else
                Log.e("StartBattle", "The transformer is neither Autobot or Decpeticon: " + tr.team);
        }

        Collections.sort(decepticons, new TransformerBattleComparator());
        Collections.sort(autobots, new TransformerBattleComparator());

        int battleCount = decepticons.size() > autobots.size() ? autobots.size() : decepticons.size(); // number of battles need to be fought
        int autobotDestroyed = 0;
        int decepticonDestoryed = 0;
        boolean isAllDestroyed = false;

        for (int i = 0; i < battleCount; i ++) {
            // Special Rule: All competitors destroyed & game ends
            if ((autobots.get(i).name.equals(CommonUtil.NAME_OPTIMUS_PRIME) || autobots.get(i).name.equals(CommonUtil.NAME_PREDAKING)) &&
                    (decepticons.get(i).name.equals(CommonUtil.NAME_OPTIMUS_PRIME) || decepticons.get(i).name.equals(CommonUtil.NAME_PREDAKING))) {
                isAllDestroyed = true;
                break;
            }
            else if (autobots.get(i).name.equals(CommonUtil.NAME_OPTIMUS_PRIME) || autobots.get(i).name.equals(CommonUtil.NAME_PREDAKING)) {
                decepticonDestoryed ++;
            }
            else if (decepticons.get(i).name.equals(CommonUtil.NAME_OPTIMUS_PRIME) || decepticons.get(i).name.equals(CommonUtil.NAME_PREDAKING)) {
                autobotDestroyed ++;
            }
            else {
                if ((autobots.get(i).courage >= decepticons.get(i).courage + 4) && (autobots.get(i).strength >= decepticons.get(i).strength + 3)) {
                    decepticonDestoryed++;
                }
                else if ((decepticons.get(i).courage >= autobots.get(i).courage + 4) && (decepticons.get(i).strength >= autobots.get(i).strength + 3)) {
                    autobotDestroyed++;
                }
                else {
                    if (autobots.get(i).skill >= decepticons.get(i).skill + 3) {
                        decepticonDestoryed++;
                    }
                    else if (decepticons.get(i).skill >= autobots.get(i).skill + 3) {
                        autobotDestroyed++;
                    }
                    else {
                        int overallRatingAutobot = autobots.get(i).strength + autobots.get(i).intelligence + autobots.get(i).speed + autobots.get(i).endurance + autobots.get(i).firepower;
                        int overallRatingDecepticon = decepticons.get(i).strength + decepticons.get(i).intelligence + decepticons.get(i).speed
                                + decepticons.get(i).endurance + decepticons.get(i).firepower;

                        if (overallRatingAutobot > overallRatingDecepticon) {
                            decepticonDestoryed++;
                        }
                        else if (overallRatingAutobot < overallRatingDecepticon) {
                            autobotDestroyed++;
                        }
                        else {
                            autobotDestroyed++;
                            decepticonDestoryed++;
                        }
                    }
                }
            }
        }

        int resultTextID = -1;
        if (isAllDestroyed) {
            autobotDestroyed = autobots.size();
            decepticonDestoryed = decepticons.size();

            if (autobotDestroyed > decepticonDestoryed)
                resultTextID = R.string.msg_all_destroyed_decepticon;
            else if (autobotDestroyed < decepticonDestoryed)
                resultTextID = R.string.msg_all_destroyed_autobot;
            else
                resultTextID = R.string.msg_all_destroyed_tie;
        }
        else {
            if (autobotDestroyed > decepticonDestoryed)
                resultTextID = R.string.msg_decepticon_won;
            else if (autobotDestroyed < decepticonDestoryed)
                resultTextID = R.string.msg_autobot_won;
            else
                resultTextID = R.string.msg_tie;
        }

        return resultTextID;
    }

    private class TransformerBattleComparator implements Comparator<Transformer> {
        @Override
        public int compare(Transformer lhs, Transformer rhs) {
            if (lhs.rank == rhs.rank)
                return 0;
            else if (lhs.rank > rhs.rank)
                return -1;
            else
                return 1;
        }
    }

    private void showFailAlertDialog(VolleyError error, Context ctx, int msgID, String tag) {
        Log.w(tag, error.toString());

        new AlertDialog.Builder(ctx)
                .setMessage(msgID)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
