package tablecreation;

import annotations.*;
import annotations.Index;
import exceptions.NoPrimaryKeyException;
import exceptions.SeveralPrimaryKeysException;
import exceptions.WrongSQLType;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;

public class TableConstructorImpl implements TableConstructor {
    private static Logger logger = Logger.getLogger(TableConstructorImpl.class);
    private Class<?> toBuildClass;
    private Table table;

    public TableConstructorImpl(Class<?> toBuildClass) {
        this.toBuildClass = toBuildClass;
        this.table = new Table(getTableName());
    }

    @Override
    public Table buildTable() throws NoPrimaryKeyException, SeveralPrimaryKeysException {
        table.setColumns(getColumns());
        addCheckConstraintIfExists();
        return table;
    }

    private String getTableName() {
        if (AnnotationUtils.isTablePresentAndNotEmpty(toBuildClass)) {
            return AnnotationUtils.getTableName(toBuildClass);
        } else {
            return toBuildClass.getSimpleName();
        }
    }

    private void addCheckConstraintIfExists() {
        if (toBuildClass.isAnnotationPresent(Check.class)) {
            table.setCheckConstraint(toBuildClass.getAnnotation(Check.class).value());
        }
    }

    private List<Column> getColumns() throws NoPrimaryKeyException, SeveralPrimaryKeysException {
        Field[] classFields = toBuildClass.getDeclaredFields();
        List<Column> columns = new ArrayList<>();
        for (Field classField : classFields) {
            if (!classField.isAnnotationPresent(annotations.Column.class) &&
                    !classField.isAnnotationPresent(annotations.ForeignKey.class) &&
                    !classField.isAnnotationPresent(MapsId.class) &&
                    !classField.isAnnotationPresent(AssociatedTable.class))
                continue;

            Column builtColumn;
            try {
                builtColumn = new ColumnConstructor(classField).buildColumn();
            } catch (WrongSQLType e) {
                logger.error(e.getMessage());
                continue;
            }
            columns.add(builtColumn);
            if (builtColumn.isForeignKey()) {
                table.addForeignKey(formFK(classField));
            }
            if (builtColumn.isPrimaryKey()) {
                checkIfPrimaryKeyIsNotOne();
                table.setPrimaryKey(formPK(builtColumn));
            }
            if (builtColumn.isManyToMany()) {
                table.addManyToManyAssociation(formManyToMany(classField));
            }
            if (classField.isAnnotationPresent(Index.class)) {
                tablecreation.Index indexToTable = generateIndex(classField);
                indexToTable.addColumns(builtColumn);
            }
        }
        checkIfPrimaryKeyWasBuilt();
        return columns;
    }

    private PrimaryKey formPK(Column column) {
        PrimaryKey primaryKey;
        if (table.getPrimaryKey() != null) {
            primaryKey = table.getPrimaryKey();
        } else {
            primaryKey = new PrimaryKey(column);
        }
        return primaryKey;
    }

    private ForeignKey formFK(Field field) {
        return new ForeignKeyConstructorImpl(field).buildForeignKey();
    }

    private ManyToMany formManyToMany(Field field) throws NoPrimaryKeyException {
        return new ManyToManyConstructor(field).build();
    }

    private tablecreation.Index generateIndex(Field field) {
        tablecreation.Index indexTOCreate = new tablecreation.Index(
                field.getAnnotation(Index.class).name(), field.getAnnotation(Index.class).unique());
        if (table.getIndexes().contains(indexTOCreate)) {
            indexTOCreate = table.getIndexes().get(table.getIndexes().indexOf(indexTOCreate));
        } else {
            table.setIndex(indexTOCreate);
        }
        return indexTOCreate;
    }

    private void checkIfPrimaryKeyWasBuilt() throws NoPrimaryKeyException {
        if (table.getPrimaryKey() == null)
            throw new NoPrimaryKeyException();
    }

    private void checkIfPrimaryKeyIsNotOne() throws SeveralPrimaryKeysException {
        if (table.getPrimaryKey() != null) {
            throw new SeveralPrimaryKeysException();
        }
    }
}
