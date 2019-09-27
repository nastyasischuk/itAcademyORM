package CRUD;

import CRUD.querycreation.DeleteQueryBuilder;
import CRUD.querycreation.UpdateQueryBuilder;

public class CRUDImpl implements CRUD {
    //private Connection connection;

//    public CRUDImpl(Connection connection) {
//        this.connection = connection;
//    }

    @Override
    public void save(Object objectToDB) {

    }

    @Override
    public Object find(Class<?> objectType, Object id) {
        return null;
    }

    @Override
    public void delete(Object objectToDelete) {
        RowToDatabase rowToDatabase = new RowToDatabaseConstructor(objectToDelete).buildRow();
        String deleteQuery = new DeleteQueryBuilder(rowToDatabase).buildQuery();
//      Statement statement = connection.createStatement();
//      statement.executeUpdate(deleteQuery);
    }

    @Override
    public void update(Object objectToUpdate) {
        RowToDatabase rowToDatabase = new RowToDatabaseConstructor(objectToUpdate).buildRow();
        String updateQuery = new UpdateQueryBuilder(rowToDatabase).buildQuery();
        //executeUpdate
    }
}
