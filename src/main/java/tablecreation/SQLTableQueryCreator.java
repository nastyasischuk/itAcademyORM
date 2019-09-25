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
        request.append(SQLStatements.CREATE_TABLE).append(table.getTableName()).append('(');
        for (Column column : table.getColumns()) {//TODO value of column.getType length varchar
            request.append(column.getName()).append(" ").append(column.getType().toString());
            if (!column.isNullable()) {
                request.append(SQLStatements.NOT_NULL);
            }
            if (column.isUnique()) {
                request.append(SQLStatements.UNIQUE);
            }
            if (column.isAutoincrement()) {
                request.append(SQLStatements.A_INCREMENT);
            }
            if (!(column.getDefaultValue().equals(""))) {
                request.append(SQLStatements.DEFAULT).append(column.getDefaultValue());
            }
            if (!(table.getCheckConstraint().equals(""))) {
                request.append(SQLStatements.CHECK).append('(').append(table.getCheckConstraint()).append(')');
            }
        }
        request.append(')').append(';');
        return request.toString();
    }

    public List<String> createIndexQuery() {
        List<String> queryIndexList = new ArrayList<>();
        StringBuilder request = new StringBuilder();
        StringBuilder columns = new StringBuilder();

        for (Index index : table.getIndexes()) {
            if (index.isUnique()) {
                request.append(SQLStatements.UNIQUE);
            }
            request.append(SQLStatements.INDEX).append(index.getName()).append(SQLStatements.ON);
            for (Column column : index.getColumnsInIndex()) {
                columns.append(column);
                if (index.getColumnsInIndex().isEmpty()) {
                    break;
                } else {
                    columns.append(", ");
                }
            }
            request.append('(').append(columns).append(')').append(';');
            queryIndexList.add(request.toString());
        }
        return queryIndexList;
    }

    public List<String> createFKQuery() {//TODO Handle empty of foreign keys
        List<String> queryFKList = new ArrayList<>();
        StringBuilder request = new StringBuilder();
        for (ForeignKey fk : table.getForeignKeys())
            if (!table.getForeignKeys().isEmpty()) {
                request.append(SQLStatements.ALTER_TABLE).append(fk.getTableName())
                        .append(SQLStatements.ADD).append(SQLStatements.CONSTRAINT).append(fk.getConstructionName())
                        .append(SQLStatements.FK).append('(').append(fk.getForeignKeyName()).append(')')
                        .append(SQLStatements.REFERENCE).append(fk.getReferenceTableName())
                        .append('(').append(fk.getReferencePKName()).append(')').append(';');
                queryFKList.add(request.toString());
            }
        return queryFKList;
    }

    public String createPKQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        request.append(SQLStatements.ALTER_TABLE).append(table.getTableName())
                .append(SQLStatements.ADD);
        if (table.getPrimaryKey().getPKList().size() > 1) {
            request.append(SQLStatements.CONSTRAINT).append("Need to create method constraint ");
        }
        for (Column column : table.getPrimaryKey().getPKList()) {
            columnNames.append(column.getName());
            if (table.getPrimaryKey().getPKList().isEmpty()) {
                break;
            } else {
                columnNames.append(", ");
            }
        }
        request.append(SQLStatements.PK).append('(').append(columnNames).append(')').append(';');
        return request.toString();
    }

}
