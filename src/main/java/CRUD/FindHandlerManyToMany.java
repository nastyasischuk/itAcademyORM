package CRUD;
import CRUD.buildingObject.ObjectSimpleBuilding;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.RowFromDBManyToManyConstructor;
import annotations.AssociatedTable;
import connection.DataBase;

import javax.sql.rowset.CachedRowSet;
import java.util.Collection;
import java.util.HashSet;

public class FindHandlerManyToMany extends FindHandler {
    private AssociatedTable associatedTable;
    public FindHandlerManyToMany(DataBase dataBase, Class<?> objectType, Object id, AssociatedTable associatedTable) {
        super(dataBase, objectType, id);
        this.associatedTable = associatedTable;
    }

    @Override
    public String buildQuery() {
        row = new RowFromDBManyToManyConstructor(objectType,idOfClassToFind, associatedTable).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row, QueryType.SELECT_MANYTOMANY).buildQuery();
        return queryFind;
    }

    @Override
    public Collection<Object> buildObject(CachedRowSet rowSet){
        Collection<Object> collection = new HashSet<>();
        try {

            while (rowSet.next()) {
                Object object = new ObjectSimpleBuilding(rowSet,objectType).buildObject();
                collection.add(object);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e.getCause());
        }
        dataBase.closeStatement(statement);
        return collection;
    }



}
