package CRUD;

import annotations.AssociatedTable;

import java.util.Collection;

public interface CRUD {
    void save(Object objectToDB);

    Object find(Class<?> objectType, Object id);

    Collection<Object> findCollection(Class classToFind, Object id, Object usingForeignKey, String mapping);

    void delete(Object objectToDelete);

    void update(Object objectToUpdate);

    Collection<Object> findCollectionFoManyToMany(Class classToFind, Object id, AssociatedTable associatedTable);

}
