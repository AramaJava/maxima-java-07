package org.example.repository;

import java.sql.SQLException;
import java.util.List;

public interface BaseRepository<T, I> {
    // CRUD
    boolean create(T element) throws SQLException;   // save()
    T read(I id) throws SQLException;     //findById()
    int update(I id, T element);   //save()
    void delete(I id);   //remove

    // Search
    List<T> findAll();  //search(), get.. select
}