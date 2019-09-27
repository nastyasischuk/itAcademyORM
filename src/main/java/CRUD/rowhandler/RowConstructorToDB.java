package CRUD.rowhandler;

import annotations.*;

import java.lang.reflect.Field;

public class RowConstructorToDB extends RowConstructor {
    Row row;
    Object classToConvertTorow;
    Field idField;

    public RowConstructorToDB(Object initialObject) {
        this.classToConvertTorow = initialObject;
        row = new RowToDB(getTableName(initialObject.getClass()));
    }

    @Override
    public Row buildRow() {
        setColumnValuesAndNames();
        return row;
    }

    private void setColumnValuesAndNames(){
        Field[] classFields = classToConvertTorow.getClass().getDeclaredFields();
        for(int i =0;i<classFields.length;i++){
           Field fieldToAdd = classFields[i];
           if(fieldToAdd.isAnnotationPresent(Column.class) && fieldToAdd.isAnnotationPresent(ForeignKey.class)
                   && fieldToAdd.isAnnotationPresent(OneToMany.class) && fieldToAdd.isAnnotationPresent(OneToOne.class))
                    continue;
           String name = getNameOfField(fieldToAdd);
            String value = getValueOfAllFields(fieldToAdd);
            if (fieldToAdd.isAnnotationPresent(PrimaryKey.class)) {
              setIdField(fieldToAdd);
              setId(name,value);
            } else {
                row.setToMap(name, value);
            }

        }
    }

    private void setIdField(Field prField){
        idField = prField;
    }
    private void setId(String name,String value){
        row.setIdValue(value);
        row.setIdName(name);
    }

    private Object getValueOfSimpleField(Field field) throws IllegalAccessException{
        return field.get(classToConvertTorow);
    }
    public String getValueOfAllFields(Field field){
        field.setAccessible(true);
        try {
            if (field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(OneToMany.class)) {
                return determineValueOfForeignKey(field).toString();
            }else
                if(getValueOfSimpleField(field)!=null)
                return getValueOfSimpleField(field).toString();
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }
    private Object determineValueOfForeignKey(Field field) throws IllegalAccessException{
        Object object = field.get(classToConvertTorow);
        Field[] fieldsOfReferencedClass = object.getClass().getDeclaredFields();
        for(Field fieldInArray:fieldsOfReferencedClass){
            if(fieldInArray.isAnnotationPresent(PrimaryKey.class)){
                fieldInArray.setAccessible(true);
                return fieldInArray.get(object);
            }
        }
        throw new RuntimeException();//todo create an exception
    }
}
