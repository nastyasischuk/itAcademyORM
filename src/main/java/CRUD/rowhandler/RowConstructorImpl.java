package CRUD.rowhandler;

import annotations.Column;
import annotations.ForeignKey;
import annotations.OneToMany;
import annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.Map;

public class RowConstructorImpl implements RowConstructor {
    Row row;
    Object classToConvertTorow;
    Field idField;

    public RowConstructorImpl(Object initialObject) {
       this.classToConvertTorow = initialObject;
    }

    @Override
    public Row buildRow() {

        return null;
    }
    private String getTableName() {
        if (classToConvertTorow.getClass().isAnnotationPresent(annotations.Table.class) && !classToConvertTorow.getClass().getAnnotation(annotations.Table.class).name().equals("")) {
            return classToConvertTorow.getClass().getAnnotation(annotations.Table.class).name();
        } else {
            return classToConvertTorow.getClass().getSimpleName();
        }

    }
    private void getIdType(){
        classToConvertTorow.getClass();
        //todo add sth
    }
    private void setColumnValuesAndNames(){
        Field[] classFields = classToConvertTorow.getClass().getDeclaredFields();
        for(int i =0;i<classFields.length;i++){
           Field fieldToAdd = classFields[i];

           String name = getNameOfField(fieldToAdd);
            String value = getValueOfAllFields(fieldToAdd);
            if (fieldToAdd.isAnnotationPresent(PrimaryKey.class)) {
              setIdField(fieldToAdd);
            } else {
                row.setToMap(name, value);
            }

        }
    }
    private String getNameOfField(Field field){
        if(field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).name().equals("")){
            return field.getAnnotation(Column.class).name();
        }
        return field.getName();
    }
    private void setIdField(Field prField){
        idField = prField;
    }
    private void setId(String name,String value){
        row.setId(value);
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
                return getValueOfSimpleField(field).toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void setIdToRow(){

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
