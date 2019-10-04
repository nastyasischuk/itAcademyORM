package annotations;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class AnnotationUtils {

    public static boolean isTablePresentAndNotEmpty(Class<?> annotatedClass) {
        return annotatedClass.isAnnotationPresent(Table.class)
                && !StringUtils.isEmpty(annotatedClass.getAnnotation(Table.class).name());
    }

    public static boolean isColumnPresent(Field field) {
        return field.isAnnotationPresent(Column.class);
    }

    public static boolean isColumnPresentAndNotEmpty(Field field) {
        return field.isAnnotationPresent(Column.class)
                && !StringUtils.isEmpty(field.getAnnotation(Column.class).name());
    }

    public static boolean isForeignKeyPresentAndNotEmpty(Field field) {
        return field.isAnnotationPresent(ForeignKey.class)
                && !StringUtils.isEmpty(field.getAnnotation(ForeignKey.class).name());
    }

    public static boolean isPrimaryKeyPresent(Field field) {
        return field.isAnnotationPresent(PrimaryKey.class);
    }

    public static boolean isOneToOneAndMapsIdPresent(Field field) {
        return field.isAnnotationPresent(MapsId.class) && field.isAnnotationPresent(OneToOne.class);
    }

    public static boolean isManyToOnePresent(Field field) {
        return field.isAnnotationPresent(ManyToOne.class);
    }

    @SafeVarargs
    public static boolean isAnyOfAnnotationIsPresent(Field field, Class<? extends Annotation> ... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (field.isAnnotationPresent(annotation))
                return true;
        }
        return false;
    }

    public static String getTableName(Class toBuildClass) {
        return ((annotations.Table) toBuildClass.getAnnotation(annotations.Table.class)).name();
    }

    public static String getColumnName(Field field) {
        return field.getAnnotation(Column.class).name();
    }

    public static String getFKName(Field field) {
        return field.getAnnotation(ForeignKey.class).name();
    }

    public static boolean isManyToManyPresent(Field field) {
        return field.isAnnotationPresent(ManyToMany.class);
    }

    public static boolean isManyToManyPresentAndMappedByNotEmpty(Field field) {
        return field.isAnnotationPresent(ManyToMany.class) &&
                !StringUtils.isEmpty(field.getAnnotation(ManyToMany.class).mappedBy());
    }

    public static String getMappedByInManyToMany(Field field) {
        return field.getAnnotation(ManyToMany.class).mappedBy();
    }

    public static boolean isAssociatedTablePresentAndNotEmpty(Field field) {
        return field.isAnnotationPresent(AssociatedTable.class) &&
                !StringUtils.isEmpty(field.getAnnotation(AssociatedTable.class).associatedTableName()) &&
                !StringUtils.isEmpty(field.getAnnotation(AssociatedTable.class).inverseJoinColumns().name()) &&
                !StringUtils.isEmpty(field.getAnnotation(AssociatedTable.class).joinColumns().name());
    }

    public static String getAssociatedTableName(Field field) {
        return field.getAnnotation(AssociatedTable.class).associatedTableName();
    }

    public static String getJoinColumn(Field field) {
        return field.getAnnotation(AssociatedTable.class).joinColumns().name();
    }

    public static String getInverseJoinColumn(Field field) {
        return field.getAnnotation(AssociatedTable.class).inverseJoinColumns().name();
    }

    public static Class<?> classGetTypeOfCollectionField(Field field) {
        ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
        if (Collection.class.isAssignableFrom(field.getType())) {
            return (Class<?>) collectionType.getActualTypeArguments()[0];
        }
        throw new RuntimeException("Field is not Collection");
    }
}
