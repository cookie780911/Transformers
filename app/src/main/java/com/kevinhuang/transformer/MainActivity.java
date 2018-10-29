package com.kevinhuang.transformer;

import android.app.AlertDialog;
import android.content.*;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.kevinhuang.ServerUtil.TransformerManager;
import com.kevinhuang.Utility.CommonUtil;
import com.kevinhuang.Utility.ListViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView lvTransformerList;
    private ListViewAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this).registerReceiver(listener, new IntentFilter(CommonUtil.INTENT_FILTER_COMMUNICATION));

        TransformerManager.getInstance().getAllSparkKey(this, false);

        Button btnCreate = findViewById(R.id.btn_create);
        Button btnReset = findViewById(R.id.btn_reset);
        Button btnBattle = findViewById(R.id.btn_battle);
        TextView txtName = findViewById(R.id.txt_name);
        TextView txtRating = findViewById(R.id.txt_rating);
        TextView txtStrength = findViewById(R.id.txt_strength);
        TextView txtCourage = findViewById(R.id.txt_courage);
        TextView txtSkill = findViewById(R.id.txt_skill);
        lvTransformerList = findViewById(R.id.lv_transformers);

        txtName.setText(R.string.txt_name);
        txtRating.setText(R.string.txt_overall_rating);
        txtStrength.setText(R.string.txt_strength);
        txtCourage.setText(R.string.txt_courage);
        txtSkill.setText(R.string.txt_skill);

        HashMap<String, Transformer> list = TransformerManager.getInstance().GetTransformerList();
        listAdapter = new ListViewAdapter(new ArrayList<Transformer>(list.values()), MainActivity.this);
        lvTransformerList.setAdapter(listAdapter);
        lvTransformerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Transformer tr = listAdapter.getTRFromList(position);
                Intent editIntent = new Intent(MainActivity.this, CreateActivity.class);
                editIntent.putExtra(CommonUtil.CREATE_TYPE, CommonUtil.CREATE_TYPE_EDIT);
                editIntent.putExtra(CommonUtil.CREATE_ID, tr.id);
                MainActivity.this.startActivity(editIntent);
            }
        });

        lvTransformerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Transformer tr = listAdapter.getTRFromList(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(R.string.msg_delete)
                        .setPositiveButton(R.string.msg_btn_yes, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TransformerManager.getInstance().deleteTransformer(MainActivity.this, tr.id);
                            }
                        })
                        .setNegativeButton(R.string.msg_btn_no, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        }).show();

                return true;
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransformerManager.getInstance().getAllSparkKey(MainActivity.this, true);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                intent.putExtra(CommonUtil.CREATE_TYPE, CommonUtil.CREATE_TYPE_CREATE);
                startActivity(intent);
            }
        });

        btnBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resultTextID = TransformerManager.getInstance().startBattle();

                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(resultTextID)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

        if (TransformerManager.getInstance().IsAllSparkKeyReady())
            TransformerManager.getInstance().getTransformers(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(listener);
    }

    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isUpdated = intent.getBooleanExtra(CommonUtil.INTENT_TRANSFORMERS_UPDATED, false);

            if (isUpdated) {
                ArrayList trList = new ArrayList<Transformer>(TransformerManager.getInstance().GetTransformerList().values());
                Collections.sort(trList, new TransformerListComparator());
                listAdapter.setData(trList);
            }
        }
    };

    private class TransformerListComparator implements Comparator<Transformer> {
        @Override
        public int compare(Transformer lhs, Transformer rhs) {
            if (lhs.team == rhs.team) {
                int lRating = lhs.strength + lhs.intelligence + lhs.speed + lhs.endurance + lhs.firepower;
                int rRating = rhs.strength + rhs.intelligence + rhs.speed + rhs.endurance + rhs.firepower;

                if (lRating == rRating)
                    return lhs.name.compareToIgnoreCase(rhs.name);
                else if (lRating > rRating)
                    return -1;
                else
                    return 1;
            }
            else if (lhs.team == CommonUtil.TR_ATTR_TEAM_DECEPTICON)
                return 1;
            else
                return -1;
        }
    }
}
