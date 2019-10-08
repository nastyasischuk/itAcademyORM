package fluentquery.Dslmpl;

import annotations.AnnotationUtils;
import customQuery.MarkingChars;
import exceptions.WrongFieldException;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

import static annotations.AnnotationUtils.getColumnName;

public class TableForQuery {
    private static Logger logger = Logger.getLogger(TableForQuery.class);
    private Class<?> tableClass;

    public TableForQuery(Class<?> tableClass) {
        this.tableClass = tableClass;
    }

    public TableImpl table() {
        return new TableImpl(getTableName());
    }

    private String getTableName() {
        if (AnnotationUtils.isTablePresentAndNotEmpty(tableClass)) {
            return AnnotationUtils.getTableName(tableClass);
        } else {
            return tableClass.getSimpleName();
        }
    }

    public FieldImpl field(String name)  {
        try {
            return new FieldImpl(getTableName()+ MarkingChars.dot + getFieldName(name));
        } catch (NoSuchFieldException e) {
            logger.error(e,e.getCause());
        }
        throw new WrongFieldException(name);
    }

    private String getFieldName(String name) throws NoSuchFieldException {
        Field field = tableClass.getDeclaredField(name);
        String columnInTable;
        if (AnnotationUtils.isColumnPresentAndNotEmpty(field) ) {
            columnInTable = getColumnName(field);
        }
        else {
            columnInTable = field.getName();
        }
        return columnInTable;
    }
}
