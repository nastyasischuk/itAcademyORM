package tablecreation;

import annotations.*;
import annotations.ForeignKey;
import annotations.ManyToMany;
import annotations.PrimaryKey;
import exceptions.WrongSQLType;

import java.lang.reflect.Field;


public class ColumnConstructor{
   private Field field;
   private tablecreation.Column column;

    public ColumnConstructor(Field field) throws WrongSQLType{
        this.field = field;
        column = new tablecreation.Column(getNameOfField(), determineTypeOfColumnInSql());
    }

    public tablecreation.Column buildColumn()
    {
        checkConstraints();
        return column;
    }

    private String getNameOfField(){
        if(AnnotationUtils.isColumnPresentAndNotEmpty(field)){
            return AnnotationUtils.getColumnName(field);
        }else if(AnnotationUtils.isForeignKeyPresentAndNotEmpty(field)){
           return AnnotationUtils.getFKName(field);
        }else if (AnnotationUtils.isOneToOneAndMapsIdPresent(field)) {
            return getNameIfOneToOne();
        } else{
            return field.getName();
        }
    }

    private String getNameIfOneToOne() {
        Class currentClass = field.getDeclaringClass();
        Field[] fields = currentClass.getDeclaredFields();
        for (Field currentField : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                if (AnnotationUtils.isColumnPresentAndNotEmpty(currentField)) {
                    return AnnotationUtils.getColumnName(field);
                } else {
                    return currentField.getName();
                }
            }
        }
        return field.getName();
    }

    private SQLTypes determineTypeOfColumnInSql()throws WrongSQLType{
        if(field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class)){
           return DeterminatorOfType.getSQLType(Integer.class);
        }
        if (field.isAnnotationPresent(Type.class)){
            return field.getAnnotation(Type.class).type();
        }
        if (AnnotationUtils.isManyToManyPresent(field))
            return DeterminatorOfType.getSQLType(Integer.class);//TODO change for smth more normal
        SQLTypes type = DeterminatorOfType.getSQLType(field.getType());
        if(type==null)
            throw new WrongSQLType(field.getClass());
        return type;
    }

    private void checkConstraints(){
        if(AnnotationUtils.isColumnPresent(field)){
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
        if (AnnotationUtils.isManyToManyPresent(field) && AnnotationUtils.isAssociatedTablePresentAndNotEmpty(field)) {
            column.setManyToMany(true);
        }
    }
}
