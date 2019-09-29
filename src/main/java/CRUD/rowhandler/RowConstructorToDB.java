package CRUD.rowhandler;

import annotations.*;

import java.lang.reflect.Field;

public class RowConstructorToDB extends RowConstructor {
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

    private void setColumnValuesAndNames(){
        Field[] classFields = classToConvertToRow.getClass().getDeclaredFields();
        for(int i =0;i<classFields.length;i++){
           Field fieldToAdd = classFields[i];
           if(fieldToAdd.isAnnotationPresent(Column.class) && fieldToAdd.isAnnotationPresent(ForeignKey.class)
                   && fieldToAdd.isAnnotationPresent(OneToMany.class) && fieldToAdd.isAnnotationPresent(OneToOne.class))
                    continue;
           String name = getNameOfField(fieldToAdd);
            String value = getValueOfAllFields(fieldToAdd);
            if (fieldToAdd.isAnnotationPresent(PrimaryKey.class)) {
              row.setIdField(fieldToAdd);
              setId(name,value);
            } else {
                row.setToMap(name, value);
            }

        }
    }

    private void setId(String name,String value){
        row.setIdValue(value);
        row.setIdName(name);
    }

    private Object getValueOfSimpleField(Field field) throws IllegalAccessException{
        return field.get(classToConvertToRow);
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
        Object object = field.get(classToConvertToRow);
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
