package tablecreation;

import java.util.ArrayList;
import java.util.List;

public class SQLTableQueryCreator {
    private Table table;


    public SQLTableQueryCreator(Table table) {
        this.table = table;
    }

    public String createTableQuery() {
        StringBuilder request = new StringBuilder();
        request.append(SQLStatements.CREATE).append(table.getTableName()).append('(');
        for (Column column : table.getColumns()) {//TODO value of column.getType length varchar
            Column lastColumn = table.getColumns().get(table.getColumns().size() - 1);

            if (column.isForeignKey() && column.getName().equals(findPKName(table.getColumns()))) {
                continue;
            }

            request.append(column.getName()).append(" ").append(column.getType());
            if (column.getType().equals(SQLTypes.VARCHAR)) {
                request.append('(').append(column.getType().getLength()).append(')');
            }
            if (!column.isNullable()) {
                request.append(SQLStatements.NOT_NULL.getValue());
            }
            if (column.isUnique()) {
                request.append(SQLStatements.UNIQUE.getValue());
            }
            if (column.isAutoincrement()) {
                request.append(SQLStatements.A_INCREMENT.getValue());
            }
            if (!(column.getDefaultValue() == null)) {
                request.append(SQLStatements.DEFAULT.getValue()).append(column.getDefaultValue());
            }
            if (!(table.getCheckConstraint() == null)) {
                request.append(SQLStatements.CHECK.getValue()).append('(').append(table.getCheckConstraint()).append(')');
            }
            if (column.equals(lastColumn)) {
                break;
            } else {
                request.append(", ");
            }
        }
        request.append(')').append(';');
        if (request.toString().endsWith(", );"))
            request = new StringBuilder(request.toString().replace(", );", ");"));
        //request.append(" ").append(createPKQuery());
        return request.toString();
    }

    public List<String> createIndexQuery() {
        List<String> queryIndexList = new ArrayList<>();
        StringBuilder request = new StringBuilder();
        StringBuilder columns = new StringBuilder();
        request.append(SQLStatements.CREATE.getValue());
        for (Index index : table.getIndexes()) {
            if (index.isUnique()) {
                request.append(SQLStatements.UNIQUE.getValue());
            }
            Column lastColumnInIndex = index.getColumnsInIndex().get(index.getColumnsInIndex().size() - 1);
            request.append(SQLStatements.INDEX.getValue()).append(index.getName()).append(SQLStatements.ON.getValue());
            for (Column column : index.getColumnsInIndex()) {
                columns.append(column);
                if (!column.equals(lastColumnInIndex)) {
                    columns.append(", ");
                }
                request.append('(').append(columns).append(')').append(';');
                queryIndexList.add(request.toString());
            }
            createPKQuery();
        }
        return queryIndexList;
    }

    public List<String> createFKQuery() {//TODO Handle empty of foreign keys
        List<String> queryFKList = new ArrayList<>();
        StringBuilder request = new StringBuilder();
        for (ForeignKey fk : table.getForeignKeys())
            if (!table.getForeignKeys().isEmpty()) {
                request.append(SQLStatements.ALTER.getValue()).append(fk.getTableName())
                        .append(SQLStatements.ADD.getValue()).append(SQLStatements.CONSTRAINT.getValue()).append(fk.getConstructionName())
                        .append(SQLStatements.FK.getValue()).append('(').append(fk.getForeignKeyName()).append(')')
                        .append(SQLStatements.REFERENCE.getValue()).append(fk.getReferenceTableName())
                        .append('(').append(fk.getReferencePKName()).append(')').append(';');
                queryFKList.add(request.toString());
            }
        return queryFKList;
    }


    public String createPKQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        request.append(SQLStatements.ALTER.getValue()).append(table.getTableName())
                .append(SQLStatements.ADD.getValue());
        if (table.getPrimaryKey().getPKList().size() > 1) {
            request.append(SQLStatements.CONSTRAINT.getValue()).append("Need to create method constraint ");
        }
        for (Column column : table.getPrimaryKey().getPKList()) {
            Column lastColumn = table.getPrimaryKey().getPKList().get(table.getPrimaryKey().getPKList().size()-1);
            columnNames.append(column.getName());
            if (!column.equals(lastColumn)) {
                columnNames.append(", ");
            }
        }
        request.append(SQLStatements.PK.getValue()).append('(').append(columnNames).append(')').append(';');
        return request.toString();
    }

    private String findPKName(List<Column> columns) {
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                return column.getName();
            }
        }
        return null;
    }

}
