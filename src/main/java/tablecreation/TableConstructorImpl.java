package tablecreation;

import annotations.Check;
import annotations.Index;
import annotations.MapsId;
import exceptions.NoPrimaryKeyException;
import exceptions.SeveralPrimaryKeysException;
import exceptions.WrongSQLType;

import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;

public class TableConstructorImpl implements TableConstructor {
    Class<?> toBuildClass;
    Table table;

    public TableConstructorImpl(Class<?> toBuildClass) {
        this.toBuildClass = toBuildClass;
        this.table = new Table(getTableName());
    }

    @Override
    public Table buildTable() throws NoPrimaryKeyException,SeveralPrimaryKeysException{
        table.setColumns(getColumns());
        addcheckConstraintIfExists();
        return table;
    }

    private String getTableName() {
        if (toBuildClass.isAnnotationPresent(annotations.Table.class) && !toBuildClass.getAnnotation(annotations.Table.class).name().equals("")) {
            return toBuildClass.getAnnotation(annotations.Table.class).name();
        } else {
            return toBuildClass.getSimpleName();
        }

    }

    private void addcheckConstraintIfExists() {
        if (toBuildClass.isAnnotationPresent(Check.class)) {
            table.setCheckConstraint(toBuildClass.getAnnotation(Check.class).value());
        }
    }

    private List<Column> getColumns() throws NoPrimaryKeyException,SeveralPrimaryKeysException{
        Field[] classFields = toBuildClass.getDeclaredFields();
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < classFields.length; i++) {
            //todo check if column
            if (!classFields[i].isAnnotationPresent(annotations.Column.class) &&
                    !classFields[i].isAnnotationPresent(annotations.ForeignKey.class) &&
                    !classFields[i].isAnnotationPresent(MapsId.class))
                continue;

            Column builtColumn = null;
            try {
                builtColumn = new ColumnConstructor(classFields[i]).buildColumn();
            } catch (WrongSQLType e) {
                e.getMessage();
                continue;
            }
            columns.add(builtColumn);
            if (builtColumn.isForeignKey()) {
                table.addForeignKey(formFK(classFields[i]));
            }
            if (builtColumn.isPrimaryKey()) {
                checkIfPrimaryKeyIsNotOne();
                table.setPrimaryKey(formPK(builtColumn));
            }
            if (classFields[i].isAnnotationPresent(Index.class)) {
                tablecreation.Index indexToTable = generateIndex(classFields[i]);
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
            primaryKey = new PrimaryKey();
        }
        primaryKey.addPrimaryKey(column);
        return primaryKey;
    }

    private ForeignKey formFK(Field field) {
        return new ForeignKeyConstructorImpl(field).buildForeignKey();
    }

    private tablecreation.Index generateIndex(Field field) {
        tablecreation.Index indexTOCreate = new tablecreation.Index(field.getAnnotation(Index.class).name(), field.getAnnotation(Index.class).unique());
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
    private void checkIfPrimaryKeyIsNotOne()throws SeveralPrimaryKeysException{
        if(table.getPrimaryKey()!=null){
            throw new SeveralPrimaryKeysException();
        }
    }

}
