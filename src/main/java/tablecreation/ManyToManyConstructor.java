package tablecreation;

import annotations.AnnotationUtils;
import exceptions.NoPrimaryKeyException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class ManyToManyConstructor {
    private Field fieldOfOriginalTableClass;
    private ManyToMany mtmToBuild;

    public ManyToManyConstructor(Field fieldOfOriginalTableClass) {
        this.fieldOfOriginalTableClass = fieldOfOriginalTableClass;
        mtmToBuild = new ManyToMany();
    }

    public ManyToMany build() throws NoPrimaryKeyException {
        mtmToBuild.setOriginalTableName(getOriginalTableName());
        mtmToBuild.setPrimaryKeyOfOriginalTableName(getPrimaryKeyOfOriginalTableName());

        mtmToBuild.setLinkedTableName(getLinkedTableName());
        mtmToBuild.setPrimaryKeyOfLinkedTableName(getPrimaryKeyOfLinkedTableName());

        mtmToBuild.setAssociatedTableName(getAssociatedTableName());
        mtmToBuild.setForeignKeyToOriginalTableName(getFKToOriginalTable());
        mtmToBuild.setForeignKeyToLinkedTableName(getFKToLinkedTable());

        mtmToBuild.setTypeOfPKOriginal(getTypeOfPKOriginal());
        mtmToBuild.setTypeOfPKLinked(getTypeOfPKLinked());
        return mtmToBuild;
    }

    private String getOriginalTableName() {
        Class originalTableClass = fieldOfOriginalTableClass.getDeclaringClass();
        return getTableName(originalTableClass);
    }

    private String getPrimaryKeyOfOriginalTableName() throws NoPrimaryKeyException {
        Class originalTableClass = fieldOfOriginalTableClass.getDeclaringClass();
        return getPrimaryKeyName(originalTableClass);
    }

    private String getLinkedTableName() {
        Class linkedTableClass = getLinkedTableClass();
        return getTableName(linkedTableClass);
    }

    private String getPrimaryKeyOfLinkedTableName() throws NoPrimaryKeyException {
        Class linkedTableClass = getLinkedTableClass();
        return getPrimaryKeyName(linkedTableClass);
    }

    private String getTypeOfPKOriginal() throws NoPrimaryKeyException {
        Class originalClass = fieldOfOriginalTableClass.getDeclaringClass();
        Field[] fields = originalClass.getDeclaredFields();
        for (Field currentField : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(currentField)) {
                return DeterminatorOfType.getSQLType(currentField.getType()).toString();
            }
        }
        throw new NoPrimaryKeyException();
    }

    private String getTypeOfPKLinked() throws NoPrimaryKeyException {
        Class linkedTableClass = getLinkedTableClass();
        Field[] fields = linkedTableClass.getDeclaredFields();
        for (Field currentField : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(currentField)) {
                return DeterminatorOfType.getSQLType(currentField.getType()).toString();
            }
        }
        throw new NoPrimaryKeyException();
    }

    private String getAssociatedTableName() {
        return AnnotationUtils.getAssociatedTableName(fieldOfOriginalTableClass);
    }

    private String getFKToOriginalTable() {
        return AnnotationUtils.getJoinColumn(fieldOfOriginalTableClass);
    }

    private String getFKToLinkedTable() {
        return AnnotationUtils.getInverseJoinColumn(fieldOfOriginalTableClass);
    }

    private Class getLinkedTableClass() {
        ParameterizedType stringListType = (ParameterizedType) fieldOfOriginalTableClass.getGenericType();
        return (Class<?>) stringListType.getActualTypeArguments()[0];
    }

    private String getTableName(Class currentClass) {
        if (AnnotationUtils.isTablePresentAndNotEmpty(currentClass)) {
            return AnnotationUtils.getTableName(currentClass);
        } else {
            return currentClass.getSimpleName();
        }
    }

    private String getPrimaryKeyName(Class currentClass) throws NoPrimaryKeyException {
        Field[] fields = currentClass.getDeclaredFields();
        for (Field currentField : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(currentField))
                if (AnnotationUtils.isColumnPresentAndNotEmpty(currentField))
                    return AnnotationUtils.getColumnName(currentField);
                else return currentField.getName();
        }
        throw new NoPrimaryKeyException();
    }
}
