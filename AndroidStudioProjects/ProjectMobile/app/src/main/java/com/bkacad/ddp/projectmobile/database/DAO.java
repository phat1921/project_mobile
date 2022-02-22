package com.bkacad.ddp.projectmobile.database;

import java.util.List;

public interface DAO <T>{
    public List<T> all();
    public List<T> listDone();
    public List<T> listTrash();
    public T get(long id);
    public long create(T item);
    public int edit(T item);
    public int delete(T item);
    public int done (T item);
    public int undo (T item);
}
