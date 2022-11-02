package org.example;

import java.util.List;
import java.util.Optional;

public interface BaseRepository   <T> {

    Optional<T> read(T t);

        void create(T t);

        void update(T t);

        void delete(T t);

        List<T> findAll();

}

