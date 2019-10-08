package CRUD.rowhandler;

import annotations.*;

import java.lang.reflect.Field;

public abstract class RowConstructor {
    public abstract Row buildRow();

    public String getTableName(Class<?> classToConvertTorow) {
        if (AnnotationUtils.isTablePresentAndNotEmpty(classToConvertTorow)) {
            return AnnotationUtils.getTableName(classToConvertTorow);
        } else {
            return classToConvertTorow.getSimpleName();
        }
    }

    String getNameOfField(Field field) {
        if (AnnotationUtils.isColumnPresentAndNotEmpty(field)) {
            return AnnotationUtils.getColumnName(field);
        } else if (AnnotationUtils.isForeignKeyPresentAndNotEmpty(field)) {
            return AnnotationUtils.getFKName(field);
        } else if (field.isAnnotationPresent(MapsId.class) && field.isAnnotationPresent(OneToOne.class)) {
            return getNameIfOneToOne(field);
        } else {
            return field.getName();
        }
    }

    private String getNameIfOneToOne(Field field) {
        Class currentClass = field.getDeclaringClass();
        Field[] fields = currentClass.getDeclaredFields();
        for (Field fieldOfEntity : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(fieldOfEntity)) {
                if (AnnotationUtils.isColumnPresentAndNotEmpty(fieldOfEntity)) {
                    return AnnotationUtils.getColumnName(fieldOfEntity);
                } else {
                    return fieldOfEntity.getName();
                }
            }

        }
        return field.getName();
    }
}
