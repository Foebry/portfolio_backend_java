package controllers;

import java.sql.SQLException;
import java.util.HashMap;

import entities.Paginated;
import entities.Serializable;

public abstract class AbstractController<T extends Serializable> {

    public abstract T create();

    public abstract T getById(String id);

    public abstract Paginated<T> getManyAndCount(HashMap<String, Object> query) throws SQLException;

    public abstract T update(String id);

    public abstract void delete(String id);

}
