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
    public Row buildRow() {
        rowFromDB.setIdValue(id.toString());
        rowFromDB.setTableName(getTableName(typeOfObject));
        rowFromDB.setIdName(getIdName());
        return null;
    }

    private String getIdName() {
        Field[] fields = typeOfObject.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return getNameOfField(field);
            }
        }
        throw new RuntimeException("No pksadsdbf");
    }

    private Map<String, Class> getNameAndType() {
        Field[] allFields = typeOfObject.getDeclaredFields();
        Map<String, Class> namesAndType = new HashMap<>();
        for (Field currentField : allFields) {
            if (currentField.isAnnotationPresent(ForeignKey.class) || currentField.isAnnotationPresent(MapsId.class)) {
                String name = getFieldNameIfFK(currentField);
                Class type = currentField.getType();
                namesAndType.put(name, type);
            } else {
                String name = getNameOfField(currentField);
                Class type = currentField.getType();
                namesAndType.put(name, type);
            }
        }
        return namesAndType;
    }

    private String getFieldNameIfFK(Field field) {
        if (field.isAnnotationPresent(annotations.ForeignKey.class) &&
                !field.getAnnotation(annotations.ForeignKey.class).name().equals("")){
            return field.getAnnotation(annotations.ForeignKey.class).name();
        } else if (field.isAnnotationPresent(MapsId.class) && field.isAnnotationPresent(OneToOne.class)) {
            Class currentClass = field.getDeclaringClass();
            Field[] fields = currentClass.getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(PrimaryKey.class)) {
                    if (f.isAnnotationPresent(annotations.Column.class) && !f.getAnnotation(annotations.Column.class).name().equals("")) {
                        return f.getAnnotation(annotations.Column.class).name();
                    } else {
                        return f.getName();
                    }
                }
            }
            return field.getName();
        } else {
            return field.getName();
        }
    }

}
