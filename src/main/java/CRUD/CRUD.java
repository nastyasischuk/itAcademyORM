package CRUD;

import java.sql.SQLException;

public interface CRUD {
    void save(Object objectToDB);
    Object find(Class<?> objectType,Object id) throws SQLException;
    void delete(Object objectToDelete);
    void update(Object objectToUpdate);

}
