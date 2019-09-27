package annotations;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotationUtils {

    public static boolean isTablePresentAndNotEmpty(Class<?> annotatedClass){
        return annotatedClass.isAnnotationPresent(Table.class)
                && !StringUtils.isEmpty(annotatedClass.getAnnotation(Table.class).name());
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

    public static String getTableName(Class toBuildClass) {
        return ((annotations.Table) toBuildClass.getAnnotation(annotations.Table.class)).name();
    }

    public static String getColumnName(Field field) {
        return field.getAnnotation(Column.class).name();
    }

    public static String getFKName(Field field) {
        return field.getAnnotation(ForeignKey.class).name();
    }

}
