package CRUD.rowhandler;

import annotations.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class RowConstructorFromDB extends RowConstructor {
    private Class typeOfObject;
    private Object id;
    private RowFromDB rowFromDB;

    public RowConstructorFromDB(Class typeOfObject, Object id) {
        this.typeOfObject = typeOfObject;
        this.id = id;
        rowFromDB = new RowFromDB();
    }

    @Override
    public RowFromDB buildRow() {
       operationsToBuild(rowFromDB);
        return rowFromDB;
    }
    protected void operationsToBuild(RowFromDB rowFromDB){
        rowFromDB.setIdValue(id.toString());
        rowFromDB.setIdName(getIdName());
        rowFromDB.setTableName(getTableName(typeOfObject));
        rowFromDB.setNameAndType(getNameAndType());
    }

    protected String getIdName() {
        Field[] fields = typeOfObject.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return getNameOfField(field);
            }
        }
        throw new RuntimeException("No pk?");
    }

    private Map<String, Class> getNameAndType() {
        Field[] allFields = typeOfObject.getDeclaredFields();
        Map<String, Class> namesAndType = new HashMap<>();
        for (Field currentField : allFields) {
            if (currentField.isAnnotationPresent(Column.class)
                    ||   currentField.isAnnotationPresent(ForeignKey.class)||
                    currentField.isAnnotationPresent(ManyToMany.class)
                    ||currentField.isAnnotationPresent(ManyToOne.class)
                    ||currentField.isAnnotationPresent(OneToMany.class)
            ||currentField.isAnnotationPresent(OneToOne.class)
            ||currentField.isAnnotationPresent(PrimaryKey.class)) {
                String name = getNameOfField(currentField);
                Class type = currentField.getType();
                namesAndType.put(name, type);
            }
        }
        return namesAndType;
    }


    public Class getTypeOfObject() {
        return typeOfObject;
    }
}
