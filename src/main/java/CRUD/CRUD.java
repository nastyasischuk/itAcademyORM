package CRUD;

public interface CRUD {
    void save(Object objectToDB);
    Object find(Class<?> objectType,Object id);
    void delete(Object objectToDelete);
    void update(Object objectToUpdate);

}
