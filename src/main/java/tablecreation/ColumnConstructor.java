package tablecreation;

import annotations.ForeignKey;
import annotations.PrimaryKey;
import annotations.*;
import exceptions.NoPrimaryKeyException;
import exceptions.WrongSQLType;

import java.lang.reflect.Field;

public class ColumnConstructor {
    private Field field;
    private tablecreation.Column column;

    public ColumnConstructor(Field field) throws WrongSQLType, NoPrimaryKeyException {
        this.field = field;
        column = new tablecreation.Column(getNameOfField(), determineTypeOfColumnInSql());
    }

    public tablecreation.Column buildColumn() {
        checkConstraints();
        return column;
    }

    private String getNameOfField() {
        if (AnnotationUtils.isColumnPresentAndNotEmpty(field)) {
            return AnnotationUtils.getColumnName(field);
        } else if (AnnotationUtils.isForeignKeyPresentAndNotEmpty(field)) {
            return AnnotationUtils.getFKName(field);
        } else if (AnnotationUtils.isOneToOneAndMapsIdPresent(field)) {
            return getNameIfOneToOne();
        } else {
            return field.getName();
        }
    }

    private String getNameIfOneToOne() {
        Class currentClass = field.getDeclaringClass();
        Field[] fields = currentClass.getDeclaredFields();
        for (Field currentField : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(currentField)) {
                if (AnnotationUtils.isColumnPresentAndNotEmpty(currentField)) {
                    return AnnotationUtils.getColumnName(currentField);
                } else {
                    return currentField.getName();
                }
            }
        }
        return field.getName();
    }

    private SQLTypes determineTypeOfColumnInSql() throws WrongSQLType, NoPrimaryKeyException {
        if (field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class)) {
            return DeterminatorOfType.getSQLType(determinePrimaryKeyType(field));
        }
        if (field.isAnnotationPresent(Type.class)) {
            return field.getAnnotation(Type.class).type();
        }
        if (AnnotationUtils.isManyToManyPresent(field))
            return DeterminatorOfType.getSQLType(Integer.class);
        SQLTypes type = DeterminatorOfType.getSQLType(field.getType());
        if (type == null)
            throw new WrongSQLType(field.getClass());
        return type;
    }

    private void checkConstraints() {
        if (AnnotationUtils.isColumnPresent(field)) {
            if (field.getAnnotation(annotations.Column.class).unique()) {
                column.setUnique(true);
            }
            if (field.isAnnotationPresent(Default.class)) {
                column.setDefaultValue(field.getAnnotation(Default.class).value());
            }
            if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                if (field.getAnnotation(PrimaryKey.class).autoincrement())
                    column.setAutoincrement(true);
                column.setPrimaryKey(true);
                column.setNullable(false);
            }
        }

        if (field.isAnnotationPresent(ForeignKey.class) || field.isAnnotationPresent(MapsId.class)) {
            column.setForeignKey(true);
        }
        if (field.isAnnotationPresent(NotNull.class)) {
            column.setNullable(false);
        }
        if (AnnotationUtils.isManyToManyPresent(field) && AnnotationUtils.isAssociatedTablePresentAndNotEmpty(field)) {
            column.setManyToMany(true);
        }
    }

    private Class determinePrimaryKeyType(Field field) throws NoPrimaryKeyException {
        Class classOfForeignKey = field.getType();
        Field[] fields = classOfForeignKey.getDeclaredFields();
        for (Field elOfFields : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(elOfFields)) {
                return elOfFields.getType();
            }
        }
        throw new NoPrimaryKeyException();
    }
}
