package tablecreation;

import annotations.MapsId;
import annotations.OneToOne;
import annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ForeignKeyConstructorImpl implements ForeignKeyConstructor {
    private Field field;
    private ForeignKey foreignKey;

    public ForeignKeyConstructorImpl(Field field) {
        this.field = field;
        foreignKey = new ForeignKey();
    }

    public ForeignKey buildForeignKey() {
        foreignKey.setConstructionName(getConstraintNameFK());
        foreignKey.setForeignKeyName(getForeignKeyName());
        foreignKey.setReferencePKName(getReferencedPKName());
        foreignKey.setTableName(getTableName());
        foreignKey.setReferenceTableName(getReferencedTableName());
        return foreignKey;
    }

    private String getTableName() {
        Class entityClass = field.getDeclaringClass();
        // если присутствует аннотация Table c именем отличным от `""`
        //comment to method
        if (entityClass.isAnnotationPresent(annotations.Table.class) &&
                !((annotations.Table) entityClass.getAnnotation(annotations.Table.class)).name().equals("")) {

            annotations.Table table = (annotations.Table) entityClass.getAnnotation(annotations.Table.class);
            return table.name();
        } else {
            return entityClass.getSimpleName();
        }
    }

    private String getForeignKeyName() {
        if (field.isAnnotationPresent(annotations.ForeignKey.class) &&
                !field.getAnnotation(annotations.ForeignKey.class).name().equals("")){
            return field.getAnnotation(annotations.ForeignKey.class).name();
        } else if (field.isAnnotationPresent(MapsId.class) && field.isAnnotationPresent(OneToOne.class)) {
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
        } else {
            return field.getName();
        }
    }

    private String getConstraintNameFK() {
        return MD5(getTableName(), getForeignKeyName());
    }

    private String getReferencedTableName() {
        Class classOfReferencedTable = field.getType();
        if (classOfReferencedTable.isAnnotationPresent(annotations.Table.class) &&
                !((annotations.Table) classOfReferencedTable.getAnnotation(annotations.Table.class)).name().equals("")) {

            annotations.Table table = (annotations.Table) classOfReferencedTable.getAnnotation(annotations.Table.class);
            return table.name();
        } else {
            return classOfReferencedTable.getSimpleName();
        }
    }

    private String getReferencedPKName() {
        Class classOfReferencedTable = field.getType();
        Field[] fields = classOfReferencedTable.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotations.PrimaryKey.class)) {
                if (field.isAnnotationPresent(annotations.Column.class) &&
                        !field.getAnnotation(annotations.Column.class).name().equals("")) {
                    return field.getAnnotation(annotations.Column.class).name();
                } else {
                    return field.getName();
                }
            } else {
                throw new RuntimeException("There is no PK in referenced class"); //todo: create Exception?
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
            // By converting to base 35 (full alphanumeric), we guarantee
            // that the length of the name will always be smaller than the 30
            // character identifier restriction enforced by a few dialects.
            return "FK" + bigInt.toString( 35 );
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException( "Unable to generate a hashed Constraint name!", e); //todo: create exception?
        }
    }
}
