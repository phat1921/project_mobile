package com.bkacad.ddp.projectmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bkacad.ddp.projectmobile.adapter.MyAdapter;
import com.bkacad.ddp.projectmobile.database.DBHelper;
import com.bkacad.ddp.projectmobile.model.Task;
import com.bkacad.ddp.projectmobile.model.TaskDAO;

import java.util.List;

public class TrashActivity extends AppCompatActivity {
    private ListView lvListTrash;
    private MyAdapter myAdapter;
    private List<Task> taskList;
    private DBHelper dbHelper;
    private TaskDAO taskDAO;
    private int position = 0;
    private boolean itemClickFromAdapter = false;

    private void initUI(){
        lvListTrash = findViewById(R.id.lv_list_trash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        initUI();
        dbHelper = new DBHelper(this);
        taskDAO = new TaskDAO(dbHelper);
        //test list hoat dong
        taskList = taskDAO.listTrash();

        myAdapter = new MyAdapter(this, taskList) {
            @Override
            public void listenerItemMenuClick(int position) {
                itemClickFromAdapter = true;
                TrashActivity.this.position = position;
                lvListTrash.showContextMenu();
            }
        };
        lvListTrash.setAdapter(myAdapter);
        registerForContextMenu(lvListTrash);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_inprocess:
                startActivity(new Intent(TrashActivity.this, MainActivity.class));
                break;
            case R.id.main_done:
                startActivity(new Intent(TrashActivity.this, DoneActivity.class));
                break;
            case R.id.main_trash:
                startActivity(new Intent(TrashActivity.this, TrashActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_trash_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(!itemClickFromAdapter){
            position = contextMenuInfo.position;
        }
        switch (item.getItemId()){
            //undo task
            case R.id.context_list_menu_undo:
                int rsDone = taskDAO.undo(taskList.get(position));
                if(rsDone == 1){
                    taskList.remove(position);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(TrashActivity.this, "Thành công!!", Toast.LENGTH_SHORT).show();
                }
                break;
            //Sửa task
        }
        itemClickFromAdapter = false;
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}