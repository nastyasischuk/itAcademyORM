package CRUD.rowhandler;

import annotations.Column;

import java.lang.reflect.Field;

public abstract class RowConstructor  {
    public abstract Row buildRow();
    protected String getTableName(Class classToConvertTorow) {
        if (classToConvertTorow.getClass().isAnnotationPresent(annotations.Table.class) && !classToConvertTorow.getClass().getAnnotation(annotations.Table.class).name().equals("")) {
            return classToConvertTorow.getClass().getAnnotation(annotations.Table.class).name();
        } else {
            return classToConvertTorow.getClass().getSimpleName();
        }

    }
    protected String getNameOfField(Field field){
        if(field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).name().equals("")){
            return field.getAnnotation(Column.class).name();
        }
        return field.getName();
    }
}
