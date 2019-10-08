package CRUD;
import annotations.AssociatedClass;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;

import CRUD.rowhandler.*;

import connection.DataBaseImplementation;

import exceptions.QueryExecutionException;
import org.apache.log4j.Logger;
import javax.sql.rowset.CachedRowSet;

import java.sql.SQLException;

import java.util.*;

public class CRUDImpl implements CRUD {
    private static Logger logger = Logger.getLogger(CRUDImpl.class);
    private DataBaseImplementation dataBase;

    public CRUDImpl(DataBaseImplementation dataBase) {
        this.dataBase = dataBase;
    }

    DataBaseImplementation getDataBase() {
        return dataBase;
    }

    @Override
    public void save(Object objectToDB) {
        try {
            new SaveDelegate(this, objectToDB).save();
            logger.debug("Object " + objectToDB + "  is saved.");
        } catch (SQLException e) {
            throw new QueryExecutionException("Could not save or update " + objectToDB, e);
        }
    }

    @Override
    public void delete(Object objectToDelete) {
        try {
            cudBasics(objectToDelete, QueryType.DELETE);
            logger.debug("Object " + objectToDelete + "  is deleted.");
        } catch (SQLException e) {
            throw new QueryExecutionException("Could not delete ", e);
        }
    }

    @Override
    public void update(Object objectToUpdate) {
        try {
            cudBasics(objectToUpdate, QueryType.UPDATE);
            logger.debug("Object " + objectToUpdate + "  is updated.");
        } catch (SQLException e) {
            throw new QueryExecutionException("Could not update ", e);
        }
    }


    @Override
    public Collection<Object> findCollectionFoManyToMany(Class classToFind, Object id, AssociatedClass associatedClass) {
        FindHandler findHandler = new FindHandlerManyToMany(dataBase,classToFind,id, associatedClass);
        return (Collection<Object>) getBuiltObject(findHandler);
    }

    RowToDB cudBasics(Object objectToDB, QueryType queryType) throws SQLException{
        RowToDB rowToDB = new RowConstructorToDB(objectToDB).buildRow();
        String query = new QueryBuilderFactory().createQueryBuilder(rowToDB, queryType).buildQuery();
        dataBase.executeUpdateQuery(query);
        return rowToDB;
    }

    public Object find(Class<?> objectType, Object id) {
        FindHandler findHandler = new FindHandler(dataBase, objectType, id);
        return getBuiltObject(findHandler);
    }

    @Override
    public Collection<Object> findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping) {
        FindHandler findHandler = new FindHandlerCollection(dataBase, classToFind, id, usingForeignKey, mapping);
        return (Collection<Object>) getBuiltObject(findHandler);
    }


    private Object getBuiltObject(FindHandler findHandler) {
        String query = findHandler.buildQuery();
        CachedRowSet cachedRowSet = findHandler.getResultSetFromQuery(query);
        return findHandler.buildObject(cachedRowSet);
    }
}
