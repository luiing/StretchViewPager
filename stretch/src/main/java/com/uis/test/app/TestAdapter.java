package com.uis.test.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends BaseAdapter {
    List<String> data = new ArrayList();
    int checkPosition = -1;
    public TestAdapter() {
    }

    public void setData(List<String> list){
        this.data.addAll(list);
    }

    public void notifyCheck(int position){
        Log.e("xx","check="+position);
        if(checkPosition == position){
            checkPosition = -1;
        }else {
            checkPosition = position;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder(parent);
            convertView = holder.getView();
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //Log.e("x-x","position = " +position +",check="+checkPosition);
        holder.tvContent.setText(data.get(position));
        holder.cbCheck.setChecked(position == checkPosition);

        return convertView;
    }

    static class ViewHolder{
        View view;
        TextView tvContent;
        CheckBox cbCheck;
        ViewHolder(ViewGroup parent){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_text,parent,false);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
        }

        View getView(){
            return view;
        }
    }
}
