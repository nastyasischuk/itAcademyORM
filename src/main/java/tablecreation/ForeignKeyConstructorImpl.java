package tablecreation;

import annotations.AnnotationUtils;
import exceptions.NoPrimaryKeyException;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ForeignKeyConstructorImpl implements ForeignKeyConstructor {
    private static Logger logger = Logger.getLogger(ForeignKeyConstructor.class);
    private Field field;
    private ForeignKey foreignKey;

    public ForeignKeyConstructorImpl(Field field) {
        this.field = field;
        foreignKey = new ForeignKey();
    }

    public ForeignKey buildForeignKey() {
        try {
            foreignKey.setConstructionName(getConstraintNameFK());
            foreignKey.setForeignKeyName(getForeignKeyName());
            foreignKey.setReferencePKName(getReferencedPKName());
            foreignKey.setTableName(getTableName());
            foreignKey.setReferenceTableName(getReferencedTableName());
        }catch (NoPrimaryKeyException e){
            logger.error(e.getMessage(),e.getCause());
            return null;
        }
        return foreignKey;
    }

    private String getTableName() {
        Class entityClass = field.getDeclaringClass();
        if (AnnotationUtils.isTablePresentAndNotEmpty(entityClass)) {
            return AnnotationUtils.getTableName(entityClass);
        } else {
            return entityClass.getSimpleName();
        }
    }

    private String getForeignKeyName() {
        if (AnnotationUtils.isForeignKeyPresentAndNotEmpty(field)){
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
        for (Field f : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(f)) {
                if (AnnotationUtils.isColumnPresentAndNotEmpty(f)) {
                    return AnnotationUtils.getColumnName(f);
                } else {
                    return f.getName();
                }
            }
        }
        return field.getName();
    }

    private String getConstraintNameFK() {
        return MD5(getTableName(), getForeignKeyName());
    }

    private String getReferencedTableName() {
        Class classOfReferencedTable = field.getType();
        if (AnnotationUtils.isTablePresentAndNotEmpty(classOfReferencedTable)) {
            return AnnotationUtils.getTableName(classOfReferencedTable);
        } else {
            return classOfReferencedTable.getSimpleName();
        }
    }

    private String getReferencedPKName() throws NoPrimaryKeyException {
        Class classOfReferencedTable = field.getType();
        Field[] fields = classOfReferencedTable.getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationUtils.isPrimaryKeyPresent(field)) {
                if (AnnotationUtils.isColumnPresentAndNotEmpty(field)) {
                    return AnnotationUtils.getColumnName(field);
                } else {
                    return field.getName();
                }
            } else {
                throw new NoPrimaryKeyException("There is no PK in referenced class");
            }
        }
        return null;
    }

    private String MD5(String tableName, String FKName) {
        String toHash = tableName + FKName;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(toHash.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger( 1, digest );
            return "FK" + bigInt.toString( 35 );
        }
        catch (NoSuchAlgorithmException e) {
          logger.error( "Unable to generate a hashed Constraint name!", e.getCause());
          return null;
        }
    }
}
