package org.perfectsmiles.system.repository;

import java.util.List;

public interface IDAO<T> {

    boolean create(T entity) throws Exception;

    List<T> readAll() throws Exception;

    T readById(int id) throws Exception;

    boolean update(T entity) throws Exception;

    boolean delete(int id) throws Exception;
}
