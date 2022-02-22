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
import com.bkacad.ddp.projectmobile.dialog.EditTaskDialog;
import com.bkacad.ddp.projectmobile.model.Task;
import com.bkacad.ddp.projectmobile.model.TaskDAO;

import java.util.List;

public class DoneActivity extends AppCompatActivity {
    private ListView lvListDone;
    private MyAdapter myAdapter;
    private List<Task> taskList;
    private DBHelper dbHelper;
    private TaskDAO taskDAO;
    private int position = 0;
    private boolean itemClickFromAdapter = false;

    private void initUI(){
        lvListDone = findViewById(R.id.lv_list_done);
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
                startActivity(new Intent(DoneActivity.this, MainActivity.class));
                break;
            case R.id.main_done:
                startActivity(new Intent(DoneActivity.this, DoneActivity.class));
                break;
            case R.id.main_trash:
                startActivity(new Intent(DoneActivity.this, TrashActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        initUI();
        dbHelper = new DBHelper(this);
        taskDAO = new TaskDAO(dbHelper);
        //test list hoat dong
        taskList = taskDAO.listDone();

        myAdapter = new MyAdapter(this, taskList) {
            @Override
            public void listenerItemMenuClick(int position) {
                itemClickFromAdapter = true;
                DoneActivity.this.position = position;
                lvListDone.showContextMenu();
            }
        };
        lvListDone.setAdapter(myAdapter);
        registerForContextMenu(lvListDone);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_done_menu, menu);
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
                    Toast.makeText(DoneActivity.this, "Thành công!!", Toast.LENGTH_SHORT).show();
                }
                break;
            //Sửa task

            //Xóa task
            case R.id.context_list_menu_delete:
                int rs = taskDAO.delete(taskList.get(position));
                if(rs == 1){
                    taskList.remove(position);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(DoneActivity.this, "Xóa thành công!!", Toast.LENGTH_SHORT).show();
                }
                break;
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