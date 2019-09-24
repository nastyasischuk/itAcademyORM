package tablecreation;

import annotations.*;
import annotations.ForeignKey;
import annotations.PrimaryKey;

import java.lang.reflect.Field;


public class ColumnConstructor{
   private Field field;
   private tablecreation.Column column;

    public ColumnConstructor(Field field) {
        this.field = field;
        column = new tablecreation.Column(getNameOfField(),determineTypeOfColumnInSql());
    }

    public tablecreation.Column buildColumn()
    {
        checkConstraints();
        return column;

    }

    private String getNameOfField(){
        if(field.getAnnotation(annotations.Column.class).name().equals("")){
            return field.getAnnotation(annotations.Column.class).name();
        }else{
            return field.getName();
        }
    }

    private SQLTypes determineTypeOfColumnInSql(){
        if (field.isAnnotationPresent(Type.class)){
            return field.getAnnotation(Type.class).type();
        }else{
            //todo create an exception and throw no sql type9
            return DeterminatorOfType.getSQLType(field.getType());
        }
    }
    private void checkConstraints(){
        if(field.getAnnotation(annotations.Column.class).unique()){
            column.setUnique(true);
        }
        if(field.isAnnotationPresent(ForeignKey.class)){
            column.setForeignKey(true);
        }
        if(field.isAnnotationPresent(NotNull.class)){
            column.setNullable(false);
        }
        if(field.isAnnotationPresent(PrimaryKey.class)){
            column.setPrimaryKey(true);
        }
        if(field.isAnnotationPresent(Default.class)){
            column.setDefaultValue(field.getAnnotation(Default.class).value());
        }

        if(field.getAnnotation(annotations.Column.class).autoincrement()){
            column.setAutoincrement(true);
        }
    }

}
