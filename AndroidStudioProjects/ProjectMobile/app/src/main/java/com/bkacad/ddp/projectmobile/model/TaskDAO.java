package com.bkacad.ddp.projectmobile.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bkacad.ddp.projectmobile.database.DAO;
import com.bkacad.ddp.projectmobile.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements DAO<Task> {
    private DBHelper dbHelper;
    public  TaskDAO(DBHelper dbHelper){
        this.dbHelper = dbHelper;
    }
    @Override
    public List<Task> all() {
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM tasks_app WHERE status = 1";
        Cursor cursor = db.rawQuery(sql, null);
        List<Task> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexStatus = cursor.getColumnIndex("status");

            do{
                long id = cursor.getLong(indexId);
                String name = cursor.getString(indexName);
                int status = cursor.getInt(indexStatus);

                Task task = new Task(id, name, status);
                list.add(task);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Task> listDone() {
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM tasks_app WHERE status = 2";
        Cursor cursor = db.rawQuery(sql, null);
        List<Task> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexStatus = cursor.getColumnIndex("status");

            do{
                long id = cursor.getLong(indexId);
                String name = cursor.getString(indexName);
                int status = cursor.getInt(indexStatus);

                Task task = new Task(id, name, status);
                list.add(task);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Task> listTrash() {
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM tasks_app WHERE status = 0";
        Cursor cursor = db.rawQuery(sql, null);
        List<Task> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexStatus = cursor.getColumnIndex("status");

            do{
                long id = cursor.getLong(indexId);
                String name = cursor.getString(indexName);
                int status = cursor.getInt(indexStatus);

                Task task = new Task(id, name, status);
                list.add(task);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public Task get(long id) {
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM tasks_app WHERE satus = 1 AND id="+id;
        Cursor cursor = db.rawQuery(sql, null);
        List<Task> list = new ArrayList<>();
        Task task = null;
        if(cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex("id");
            int indexName = cursor.getColumnIndex("name");
            int indexStatus = cursor.getColumnIndex("status");

            String name = cursor.getString(indexName);
            int status = cursor.getInt(indexStatus);

           task = new Task(id, name, status);

        }
        cursor.close();
        return task;
    }

    @Override
    public long create(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", item.getName());
        contentValues.put("status", 1);
        long id = db.insert("tasks_app", null, contentValues);
        return id;
    }

    @Override
    public int edit(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", item.getName());
        int rs = db.update("tasks_app", contentValues,"id =" + item.getId(), null);
        return rs;
    }

    @Override
    public int delete(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 0);
        int rs = db.update("tasks_app", contentValues,"id =" + item.getId(), null);
        return rs;
    }

    @Override
    public int done(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 2);
        int rs = db.update("tasks_app", contentValues,"id =" + item.getId(), null);
        return rs;
    }

    @Override
    public int undo(Task item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 1);
        int rs = db.update("tasks_app", contentValues,"id =" + item.getId(), null);
        return rs;
    }
}
