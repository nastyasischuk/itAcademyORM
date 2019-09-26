package tablecreation;

import annotations.*;
import annotations.ForeignKey;
import annotations.PrimaryKey;
import exceptions.WrongSQLType;

import java.lang.reflect.Field;


public class ColumnConstructor{
   private Field field;
   private tablecreation.Column column;

    public ColumnConstructor(Field field) throws WrongSQLType{
        this.field = field;
        column = new tablecreation.Column(getNameOfField(),determineTypeOfColumnInSql());
    }

    public tablecreation.Column buildColumn()
    {
        checkConstraints();
        return column;
    }

    private String getNameOfField(){

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

    private SQLTypes determineTypeOfColumnInSql()throws WrongSQLType{
        if(field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class)){
           return DeterminatorOfType.getSQLType(Integer.class);
        }
        if (field.isAnnotationPresent(Type.class)){
            return field.getAnnotation(Type.class).type();
        }
            SQLTypes type = DeterminatorOfType.getSQLType(field.getType());
            if(type==null)
                throw new WrongSQLType(field.getClass());
            return type;

    }



    private void checkConstraints(){
        if(field.isAnnotationPresent(annotations.Column.class)){
            if(field.getAnnotation(annotations.Column.class).unique()){
                column.setUnique(true);
            }

            if(field.isAnnotationPresent(Default.class)){
                column.setDefaultValue(field.getAnnotation(Default.class).value());
            }

            if(field.getAnnotation(annotations.Column.class).autoincrement()){
                column.setAutoincrement(true);
            }
            if(field.isAnnotationPresent(PrimaryKey.class)){
                column.setPrimaryKey(true);
                column.setNullable(false);
            }
        }

        if(field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class)){
            column.setForeignKey(true);
        }
        if(field.isAnnotationPresent(NotNull.class)){
            column.setNullable(false);
        }

    }

}
