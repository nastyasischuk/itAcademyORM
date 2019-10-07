package CRUD;


import annotations.AssociatedClass;
import CRUD.buildingObject.ObjectSimpleBuilding;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.RowFromDBManyToManyConstructor;
import connection.*;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.Collection;
import java.util.HashSet;

public class FindHandlerManyToMany extends FindHandler {
    protected static org.apache.log4j.Logger logger = Logger.getLogger(FindHandlerManyToMany.class);
    private AssociatedClass associatedClass;

    public FindHandlerManyToMany(DataBase dataBase, Class<?> objectType, Object id, AssociatedClass associatedClass) {
        super(dataBase, objectType, id);
        this.associatedClass = associatedClass;
    }

    @Override
    public String buildQuery() {
        row = new RowFromDBManyToManyConstructor(objectType, idOfClassToFind, associatedClass).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row, QueryType.SELECT_MANY_TO_MANY).buildQuery();
        return queryFind;
    }

    @Override
    public Collection<Object> buildObject(CachedRowSet rowSet) {
        Collection<Object> collection = new HashSet<>();
        try {

            while (rowSet.next()) {
                Object object = new ObjectSimpleBuilding(rowSet, objectType).buildObject();
                collection.add(object);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
        }
        dataBase.closeStatement(statement);
        return collection;
    }
}
