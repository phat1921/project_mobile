package com.bkacad.ddp.project.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bkacad.ddp.project.database.DAO;
import com.bkacad.ddp.project.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements DAO<Task> {
    private DBHelper dbHelper;
    public TaskDAO(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }
    @Override
    public List all() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE status = 1";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexStatus = cursor.getColumnIndex("status");
            do {
                Task item = new Task();
                item.setId(cursor.getLong(indexId));
                item.setName(cursor.getString(indexName));
                item.setStatus(cursor.getInt(indexStatus));

                tasks.add(item);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    @Override
    public Task get(long id) {
        String sql = "SELECT * FROM task WHERE status = 1 AND id =" + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        Task item = null;
        if(cursor.moveToFirst()){
            item = new Task();
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexStatus = cursor.getColumnIndex("status");

            item.setId(cursor.getLong(indexId));
            item.setName(cursor.getString(indexName));
            item.setStatus(cursor.getInt(indexStatus));
        }

        cursor.close();
        return item;
    }

    @Override
    public long create(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("status", 1);
        long id = db.insert("task", null, values);
        return id;
    }

    @Override
    public int update(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        int rs = db.update("task", values, "id="+item.getId(), null);
        return rs;
    }

    @Override
    public int delete(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.getName());
        values.put("status", 0);
        int rs = db.update("task", values, "id="+item.getId(), null);
        return rs;
    }
}
