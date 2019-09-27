package CRUD.rowhandler;

import annotations.*;

import java.lang.reflect.Field;

public abstract class RowConstructor  {
    public abstract Row buildRow();
    protected String getTableName(Class<?> classToConvertTorow) {
        if (classToConvertTorow.isAnnotationPresent(annotations.Table.class) && !classToConvertTorow.getAnnotation(annotations.Table.class).name().equals("")) {
            return classToConvertTorow.getAnnotation(annotations.Table.class).name();
        } else {
            return classToConvertTorow.getSimpleName();
        }

    }
    protected String getNameOfField(Field field){
        if(field.isAnnotationPresent(annotations.Column.class) && !field.getAnnotation(annotations.Column.class).name().equals("")){
        return field.getAnnotation(annotations.Column.class).name();
    }else if(field.isAnnotationPresent(ForeignKey.class) && !field.getAnnotation(ForeignKey.class).name().equals("")){
        return field.getAnnotation(ForeignKey.class).name();
    }else if (field.isAnnotationPresent(MapsId.class) && field.isAnnotationPresent(OneToOne.class)) {
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
    }else{
        return field.getName();
    }
    }
}
