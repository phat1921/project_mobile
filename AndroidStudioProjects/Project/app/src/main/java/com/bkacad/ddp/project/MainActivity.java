package com.bkacad.ddp.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bkacad.ddp.project.database.DAO;
import com.bkacad.ddp.project.database.DBHelper;
import com.bkacad.ddp.project.model.Task;
import com.bkacad.ddp.project.model.TaskDAO;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private DAO<Task> taskDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        taskDAO = new TaskDAO(dbHelper);
    }
}