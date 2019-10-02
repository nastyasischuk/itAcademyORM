package CRUD;

import annotations.AssociatedTable;

import java.sql.SQLException;
import java.util.Collection;

public interface CRUD {
    void save(Object objectToDB);
    Object find(Class<?> objectType,Object id);
    public Object findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping);
    void delete(Object objectToDelete);
    void update(Object objectToUpdate);
    Object findCollectionFoManyToMany(Class classToFind, Object id, String mapping, AssociatedTable associatedTable);

}
