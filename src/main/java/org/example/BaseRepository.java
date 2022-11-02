package org.example;

import java.util.List;

public interface BaseRepository <T> {

    void create(T t);
    List<T> read(int id);

    void update(T t, String[] params);

    void delete(int id);

    List<T> findAll();

}

//  В read и delete нужно заменить T t на какой-то идентификатор объекта,
//  иначе придется сравнивать все объекты в базе с указанным.
//  В update этот идентификатор нужно добавить.