package com.kevinhuang.transformer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.*;

import com.google.gson.JsonObject;
import com.kevinhuang.ServerUtil.TransformerManager;
import com.kevinhuang.Utility.CommonUtil;
import com.kevinhuang.Utility.InputFilterOneToTen;

import java.util.*;

public class CreateActivity extends AppCompatActivity {

    private boolean isEdit = false;
    private String editID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button btnCreateEdit = findViewById(R.id.btn_create_or_edit);
        Button btnAuto = findViewById(R.id.btn_auto);
        Button btnCancel = findViewById(R.id.btn_cancel);

        final EditText edtStrength = findViewById(R.id.edt_strength);
        final EditText edtIntelligence = findViewById(R.id.edt_intelligence);
        final EditText edtSpeed = findViewById(R.id.edt_speed);
        final EditText edtEndurance = findViewById(R.id.edt_endurance);
        final EditText edtRank = findViewById(R.id.edt_rank);
        final EditText edtCourage = findViewById(R.id.edt_courage);
        final EditText edtFirepower = findViewById(R.id.edt_firepower);
        final EditText edtSkill = findViewById(R.id.edt_skill);
        final EditText edtName = findViewById(R.id.edt_name);
        final Spinner spnTeam = findViewById(R.id.spn_team);

        InputFilter[] inputFilters = new InputFilter[] { new InputFilterOneToTen(1, 10) };
        edtStrength.setFilters(inputFilters);
        edtIntelligence.setFilters(inputFilters);
        edtSpeed.setFilters(inputFilters);
        edtEndurance.setFilters(inputFilters);
        edtRank.setFilters(inputFilters);
        edtCourage.setFilters(inputFilters);
        edtFirepower.setFilters(inputFilters);
        edtSkill.setFilters(inputFilters);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.team_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTeam.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String type = extras.getString(CommonUtil.CREATE_TYPE);

            if (type.equals(CommonUtil.CREATE_TYPE_CREATE)) {
                isEdit = false;
                btnCreateEdit.setText(R.string.btn_create);
            }
            else if (type.equals(CommonUtil.CREATE_TYPE_EDIT)) {
                isEdit = true;
                btnCreateEdit.setText(R.string.btn_save);
                editID = extras.getString(CommonUtil.CREATE_ID);
                HashMap<String, Transformer> trList = TransformerManager.getInstance().GetTransformerList();
                Transformer editTransformer = trList.get(editID);

                if (editTransformer != null) {
                    edtName.setText(editTransformer.name);
                    edtStrength.setText(editTransformer.strength + "");
                    edtIntelligence.setText(editTransformer.intelligence  + "");
                    edtEndurance.setText(editTransformer.endurance  + "");
                    edtCourage.setText(editTransformer.courage + "");
                    edtRank.setText(editTransformer.rank + "");
                    edtSpeed.setText(editTransformer.speed + "");
                    edtFirepower.setText(editTransformer.firepower + "");
                    edtSkill.setText(editTransformer.skill + "");

                    if (editTransformer.team == CommonUtil.TR_ATTR_TEAM_AUTOBOT)
                        spnTeam.setSelection(0);
                    else if (editTransformer.team == CommonUtil.TR_ATTR_TEAM_DECEPTICON)
                        spnTeam.setSelection(1);
                    else
                        Log.w("CreateEdit","Edit: No team is matched for the Transformer");
                }
            }
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();

                edtStrength.setText((random.nextInt(10) + 1) + "");
                edtIntelligence.setText((random.nextInt(10) + 1) + "");
                edtSpeed.setText((random.nextInt(10) + 1) + "");
                edtEndurance.setText((random.nextInt(10) + 1) + "");
                edtRank.setText((random.nextInt(10) + 1) + "");
                edtCourage.setText((random.nextInt(10) + 1) + "");
                edtFirepower.setText((random.nextInt(10) + 1) + "");
                edtSkill.setText((random.nextInt(10) + 1) + "");
            }
        });

        btnCreateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TransformerManager.getInstance().IsAllSparkKeyReady()) {
                    JsonObject body = new JsonObject();

                    if (edtCourage.getText().toString().equals("") || edtEndurance.getText().toString().equals("") || edtFirepower.getText().toString().equals("")
                            || edtIntelligence.getText().toString().equals("") || edtRank.getText().toString().equals("") || edtSkill.getText().toString().equals("")
                            || edtSpeed.getText().toString().equals("") || edtStrength.getText().toString().equals("")) {
                        new AlertDialog.Builder(CreateActivity.this)
                                .setMessage(R.string.msg_fail_create_invalid_data)
                                .setNeutralButton(R.string.msg_btn_ok, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                }).show();
                    }
                    else {
                        body.addProperty(CommonUtil.TR_ATTR_NAME, edtName.getText().toString());
                        body.addProperty(CommonUtil.TR_ATTR_STRENGTH, Integer.parseInt(edtStrength.getText().toString()));
                        body.addProperty(CommonUtil.TR_ATTR_INTELLIGENCE, Integer.parseInt(edtIntelligence.getText().toString()));
                        body.addProperty(CommonUtil.TR_ATTR_SPEED, Integer.parseInt(edtSpeed.getText().toString()));
                        body.addProperty(CommonUtil.TR_ATTR_ENDURANCE, Integer.parseInt(edtEndurance.getText().toString()));
                        body.addProperty(CommonUtil.TR_ATTR_COURAGE, Integer.parseInt(edtCourage.getText().toString()));
                        body.addProperty(CommonUtil.TR_ATTR_RANK, Integer.parseInt(edtRank.getText().toString()));
                        body.addProperty(CommonUtil.TR_ATTR_FIREPOWER, Integer.parseInt(edtFirepower.getText().toString()));
                        body.addProperty(CommonUtil.TR_ATTR_SKILL, Integer.parseInt(edtSkill.getText().toString()));

                        if (spnTeam.getSelectedItem().toString().equals(getResources().getString(R.string.txt_autobot)))
                            body.addProperty("team", "A");
                        else if (spnTeam.getSelectedItem().toString().equals(getResources().getString(R.string.txt_decepticon)))
                            body.addProperty("team", "D");
                        else
                            Log.e("CreateEdit", "no class is matched selected team");

                        if (isEdit) {
                            body.addProperty(CommonUtil.TR_ATTR_ID, editID);
                            TransformerManager.getInstance().editTransformer(CreateActivity.this, body);
                        } else {
                            TransformerManager.getInstance().createTransformer(CreateActivity.this, body);
                        }
                    }
                }
                else {
                    new AlertDialog.Builder(CreateActivity.this)
                            .setMessage(R.string.msg_allspark_not_ready)
                            .setNeutralButton(R.string.msg_btn_ok, new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            }).show();
                }
            }
        });
    }
}
