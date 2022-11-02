package org.example;

import java.util.List;
import java.util.Optional;

public interface BaseRepository <T> {

    Optional<List<T>> read(Long id);

    void create(T t);

    void update(T t, String[] params);

    void delete(Long id);

    List<T> findAll();

}

  //  С Optional и обобщениями - хорошие идеи.
//  Но тогда уж List> :))
//  В read и delete нужно заменить T t на какой-то идентификатор объекта,
//  иначе придется сравнивать все объекты в базе с указанным.
//  В update этот идентификатор нужно добавить.