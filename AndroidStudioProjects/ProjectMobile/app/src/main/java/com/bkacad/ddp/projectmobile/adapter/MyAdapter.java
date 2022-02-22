package com.bkacad.ddp.projectmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkacad.ddp.projectmobile.R;
import com.bkacad.ddp.projectmobile.model.Task;

import java.util.List;

public abstract class MyAdapter extends BaseAdapter {

    public abstract void listenerItemMenuClick(int position);
    private Context context;
    private List<Task> taskList;

    public MyAdapter(Context context, List<Task> taskList){
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       if (convertView == null){
           convertView = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
       }
       //Bind id
        TextView tvTask;
        tvTask = convertView.findViewById(R.id.tv_task);
        //do du lieu
        tvTask.setText(taskList.get(position).getName());

        //su kien khi click menu
        ImageView imgMenu = convertView.findViewById(R.id.item_task_menu);
        //khi click vao imgMenu
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerItemMenuClick(position);
            }
        });

        return convertView;
    }
}
