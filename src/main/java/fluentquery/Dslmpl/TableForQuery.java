package fluentquery.Dslmpl;

import annotations.AnnotationUtils;

import java.lang.reflect.Field;

import static annotations.AnnotationUtils.getColumnName;

public class TableForQuery {
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
            return new FieldImpl(getTableName()+ "." + getFieldName(name));
        } catch (NoSuchFieldException e) {
            e.printStackTrace(); //TODO LOGGER
        }
        throw new RuntimeException("cHECK YOUR FIELDS");
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
