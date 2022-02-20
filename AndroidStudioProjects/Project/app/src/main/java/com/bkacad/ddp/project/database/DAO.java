package com.bkacad.ddp.project.database;

import java.util.List;

public interface DAO <T>{
    public List all();
    public T get(long id);
    public long create(T item);
    public int update(T item);
    public int delete(T item);
}
