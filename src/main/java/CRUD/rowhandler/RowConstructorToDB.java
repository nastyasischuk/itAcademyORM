package CRUD.rowhandler;

import annotations.*;
import connection.DataBaseImplementation;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

public class RowConstructorToDB extends RowConstructor {
    private static org.apache.log4j.Logger logger = Logger.getLogger(DataBaseImplementation.class);
    private RowToDB row;
    private Object classToConvertToRow;

    public RowConstructorToDB(Object initialObject) {
        this.classToConvertToRow = initialObject;
        row = new RowToDB(getTableName(initialObject.getClass()));
    }

    @Override
    public RowToDB buildRow() {
        setColumnValuesAndNames();
        return row;
    }

    private void setColumnValuesAndNames() {
        Field[] classFields = classToConvertToRow.getClass().getDeclaredFields();
        for (Field fieldToAdd : classFields) {
            if (fieldToAdd.isAnnotationPresent(OneToMany.class) || fieldToAdd.isAnnotationPresent(OneToOne.class))
                continue;

            String name = getNameOfField(fieldToAdd);
            String value = getValueOfAllFields(fieldToAdd);
            if (fieldToAdd.isAnnotationPresent(PrimaryKey.class)) {
                row.setIdField(fieldToAdd);
                setId(name, value);
            } else if (fieldToAdd.isAnnotationPresent(ManyToOne.class)) {
                fieldToAdd.setAccessible(true);
                row.setToMap(fieldToAdd.getName(), getValueOfPK(fieldToAdd));
            } else if (fieldToAdd.isAnnotationPresent(ManyToMany.class)) {
                continue;
            } else {
                row.setToMap(name, value);
            }
        }
    }

    private String getValueOfPK(Field fieldToAdd) {
        String valueOfPK = "";
        try {
            Object valueOfPrimaryKeyInOriginalTable = fieldToAdd.get(classToConvertToRow);
            Field[] fields = valueOfPrimaryKeyInOriginalTable.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                    field.setAccessible(true);
                    valueOfPK = field.get(valueOfPrimaryKeyInOriginalTable).toString();
                }
            }
        } catch (IllegalAccessException e) {
            logger.debug(e.getMessage());
            logger.debug(e.getCause().getMessage());
        }
        return valueOfPK;
    }

    private void setId(String name, String value) {
        row.setIdValue(value);
        row.setIdName(name);
    }

    private Object getValueOfSimpleField(Field field) throws IllegalAccessException {
        return field.get(classToConvertToRow);
    }

    public String getValueOfAllFields(Field field) {
        field.setAccessible(true);
        try {
            if (field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(OneToMany.class)) {
                return determineValueOfForeignKey(field).toString();
            } else if (getValueOfSimpleField(field) != null)
                return getValueOfSimpleField(field).toString();
        } catch (IllegalAccessException e) {
            logger.debug(e.getMessage());
        }
        return null;
    }

    private Object determineValueOfForeignKey(Field field) throws IllegalAccessException {
        Object object = field.get(classToConvertToRow);
        Field[] fieldsOfReferencedClass = object.getClass().getDeclaredFields();
        for (Field fieldInArray : fieldsOfReferencedClass) {
            if (fieldInArray.isAnnotationPresent(PrimaryKey.class)) {
                fieldInArray.setAccessible(true);
                return fieldInArray.get(object);
            }
        }
        throw new RuntimeException();//todo create an exception
    }
}
