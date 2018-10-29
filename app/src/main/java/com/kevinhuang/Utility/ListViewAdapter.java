package com.kevinhuang.Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kevinhuang.ServerUtil.TransformerManager;
import com.kevinhuang.transformer.CreateActivity;
import com.kevinhuang.transformer.R;
import com.kevinhuang.transformer.Transformer;

import java.net.URL;
import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<Transformer> {

    private ArrayList<Transformer> list;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtRating;
        TextView txtStrength;
        TextView txtCourage;
        TextView txtSkill;
        ImageView imgTeam;
    }

    public ListViewAdapter(ArrayList<Transformer> data, Context context) {
        super(context, R.layout.listview_transformer_stat, data);
        this.list = data;
        this.mContext = context;
    }

    public void setData(ArrayList<Transformer> newList) {
        list.clear();
        list.addAll(newList);
        this.notifyDataSetChanged();
    }

    public Transformer getTRFromList(int position) {
        return getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transformer tr = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_transformer_stat, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.txtRating = (TextView) convertView.findViewById(R.id.txt_rating);
            viewHolder.txtStrength = (TextView) convertView.findViewById(R.id.txt_strength);
            viewHolder.txtCourage = (TextView) convertView.findViewById(R.id.txt_courage);
            viewHolder.txtSkill = (TextView) convertView.findViewById(R.id.txt_skill);
            viewHolder.imgTeam = (ImageView) convertView.findViewById(R.id.img_team);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            DisplayTeamImageTask dtiTask = new DisplayTeamImageTask(viewHolder.imgTeam);
            dtiTask.execute(tr.team_icon);
            viewHolder.txtName.setText(tr.name);
            viewHolder.txtRating.setText((tr.strength + tr.endurance + tr.firepower + tr.intelligence + tr.speed) + "");
            viewHolder.txtStrength.setText(tr.strength + "");
            viewHolder.txtCourage.setText(tr.courage + "");
            viewHolder.txtSkill.setText(tr.skill + "");
        }
        catch (Exception e) {
            Log.e("ListAdapter", e.toString());
        }

        return convertView;
    }
}