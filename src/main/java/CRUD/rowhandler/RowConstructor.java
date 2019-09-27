package CRUD.rowhandler;

import annotations.Column;
import annotations.Table;

import java.lang.reflect.Field;

public abstract class RowConstructor  {
    public abstract Row buildRow();

    protected String getTableName(Class classToConvertTorow) {
        if (classToConvertTorow.isAnnotationPresent(Table.class) && !((Table)classToConvertTorow.getAnnotation(Table.class)).name().equals(""))
            return ((Table) classToConvertTorow.getAnnotation(Table.class)).name();
        else
            return classToConvertTorow.getSimpleName();
    }
    protected String getNameOfField(Field field){
        if(field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).name().equals("")){
            return field.getAnnotation(Column.class).name();
        }
        return field.getName();
    }
}
