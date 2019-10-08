package CRUD;

import CRUD.buildingObject.ObjectBuilderWithLinks;
import CRUD.querycreation.QueryBuilderFactory;
import CRUD.querycreation.QueryType;
import CRUD.rowhandler.RowConstructorFromDBByForeignKey;
import connection.DataBase;
import org.apache.log4j.Logger;

import javax.sql.rowset.CachedRowSet;
import java.util.Collection;
import java.util.HashSet;

public class FindHandlerCollection extends FindHandler {
    private static org.apache.log4j.Logger logger = Logger.getLogger(FindHandlerCollection.class);
    private Object ownerOfCollection;
    private String nameInObjectOwner;

    public FindHandlerCollection(DataBase dataBase, Class<?> objectType, Object id, Object usingForeignKey, String mapping) {
        super(dataBase, objectType, id);
        this.ownerOfCollection = usingForeignKey;
        this.nameInObjectOwner = mapping;
    }

    @Override
    public String buildQuery() {
        row = new RowConstructorFromDBByForeignKey(objectType, idOfClassToFind).buildRow();
        String queryFind = new QueryBuilderFactory().createQueryBuilderFromDB(row, QueryType.SELECT_OBJECT).buildQuery();
        return queryFind;
    }

    @Override
    public Collection<Object> buildObject(CachedRowSet rowSet) {
        Collection<Object> collection = new HashSet<>();
        try {
            while (rowSet.next()) {
                Object object = new ObjectBuilderWithLinks(row, rowSet, objectType, ownerOfCollection, nameInObjectOwner, dataBase).buildObject();
                collection.add(object);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
        }
        dataBase.closeStatement(statement);
        return collection;
    }
}
