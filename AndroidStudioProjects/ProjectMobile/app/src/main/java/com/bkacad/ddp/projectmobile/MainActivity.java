package com.bkacad.ddp.projectmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.bkacad.ddp.projectmobile.dialog.NewTaskDialog;
import com.bkacad.ddp.projectmobile.model.Task;
import com.bkacad.ddp.projectmobile.model.TaskDAO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private ListView lvList;
    private MyAdapter myAdapter;
    private List<Task> taskList;
    private NewTaskDialog newTaskDialog = null;
    private EditTaskDialog editTaskDialog =null;
    private DBHelper dbHelper;
    private TaskDAO taskDAO;

    private int position = 0;
    private boolean itemClickFromAdapter = false;

    private void initUI(){
        btnAdd = findViewById(R.id.btn_add);
        lvList = findViewById(R.id.lv_list);
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
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.main_done:
                startActivity(new Intent(MainActivity.this, DoneActivity.class));
                break;
            case R.id.main_trash:
                startActivity(new Intent(MainActivity.this, TrashActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        dbHelper = new DBHelper(this);
        taskDAO = new TaskDAO(dbHelper);
        //test list hoat dong
        taskList = taskDAO.all();

        myAdapter = new MyAdapter(this, taskList) {
            @Override
            public void listenerItemMenuClick(int position) {
                itemClickFromAdapter = true;
                MainActivity.this.position = position;
                lvList.showContextMenu();
            }
        };
        lvList.setAdapter(myAdapter);
        registerForContextMenu(lvList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newTaskDialog == null){
                    newTaskDialog = new NewTaskDialog(MainActivity.this) {
                        @Override
                        public void sendDataFromDialog(Task task) {
                            //them vao db trc
                            long id = taskDAO.create(task);
                            if(id == -1){
                                Toast.makeText(MainActivity.this, "Thêm thất bại!!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            task.setId(id);
                            //them vao DB -> cap nhat Listview
                            taskList.add(task);
                            myAdapter.notifyDataSetChanged();
                        }
                    };
                }
                newTaskDialog.show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(!itemClickFromAdapter){
            position = contextMenuInfo.position;
        }
        switch (item.getItemId()){
            //Hoàn thành task
            case R.id.context_list_menu_done:
                int rsDone = taskDAO.done(taskList.get(position));
                if(rsDone == 1){
                    taskList.remove(position);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Thành công!!", Toast.LENGTH_SHORT).show();
                }
                break;
                //Sửa task
            case R.id.context_list_menu_edit:
                if(editTaskDialog == null){
                    editTaskDialog = new EditTaskDialog(MainActivity.this) {
                        @Override
                        public void sendEditDataFromDialog(Task task) {
                            //them vao db trc
                            long id = taskDAO.edit(task);
                            if(id == -1){
                                Toast.makeText(MainActivity.this, "Sửa thất bại!!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //them vao DB -> cap nhat Listview
//                            taskList.remove(position);
//                            taskList.add(task);
                            myAdapter.notifyDataSetChanged();
                        }
                    };
                }
                editTaskDialog.show();

                break;
                //Xóa task
            case R.id.context_list_menu_delete:
                int rs = taskDAO.delete(taskList.get(position));
                if(rs == 1){
                    taskList.remove(position);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Xóa thành công!!", Toast.LENGTH_SHORT).show();
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