package tablecreation;

import java.lang.String;

import java.util.ArrayList;
import java.util.List;

public class SQLTableQueryCreator {
    private Table table;

    public SQLTableQueryCreator(Table table) {
        this.table = table;
    }

    public String createTableQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.CREATE_TABLE).append(table.getTableName()).append('(');
        for (Column column : table.getColumns()) {
            Column lastColumn = table.getColumns().get(table.getColumns().size() - 1);

            if (column.isForeignKey() && column.getName().equals(findPKName(table.getColumns())))
                continue;
            if (column.isManyToMany())
                continue;

            query.append(column.getName()).append(" ").append(column.getType());
            if (column.getType().equals(SQLTypes.VARCHAR)) {
                query.append('(').append(column.getType().getLength()).append(')');
            }
            if (!column.isNullable()) {
                query.append(SQLStatements.NOT_NULL.getValue());
            }
            if (column.isUnique()) {
                query.append(SQLStatements.UNIQUE.getValue());
            }
            if (column.isAutoincrement()) {
                query.append(SQLStatements.A_INCREMENT.getValue());
            }
            if (!(column.getDefaultValue() == null)) {
                query.append(SQLStatements.DEFAULT.getValue()).append(column.getDefaultValue());
            }
            if (!(table.getCheckConstraint() == null)) {
                query.append(SQLStatements.CHECK.getValue()).append('(').append(table.getCheckConstraint()).append(')');
            }
            if (column.equals(lastColumn)) {
                break;
            } else {
                query.append(", ");
            }
        }
        query.append(')').append(';');
        if (query.toString().endsWith(", );"))
            query = new StringBuilder(query.toString().replace(", );", ");"));
        return query.toString();
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
                request.append(SQLStatements.ALTER_TABLE.getValue()).append(fk.getTableName())
                        .append(SQLStatements.ADD.getValue()).append(SQLStatements.CONSTRAINT.getValue()).append(fk.getConstructionName())
                        .append(SQLStatements.FK.getValue()).append('(').append(fk.getForeignKeyName()).append(')')
                        .append(SQLStatements.REFERENCE.getValue()).append(fk.getReferenceTableName())
                        .append('(').append(fk.getReferencePKName()).append(')').append(';');
                queryFKList.add(request.toString());
            }
        return queryFKList;
    }

    public List<String> createManyToManyQuery() {
        List<String> queryMTMList = new ArrayList<>();
        StringBuilder queryAlterMTM = new StringBuilder();
        StringBuilder queryCreateTableMTMT = new StringBuilder();
        StringBuilder querySecondCreateTableMTMT = new StringBuilder();
        for(ManyToMany mtm: table.getMtmAssociations()){
            queryCreateTableMTMT.append(SQLStatements.CREATE_TABLE.getValue()).append(mtm.getAssociatedTableName()).append('(')
                    .append(mtm.getForeignKeyToOriginalTableName()).append(" ").append(mtm.getTypeOfPKOriginal()).append(SQLStatements.NOT_NULL.getValue())
                    .append(mtm.getForeignKeyToLinkedTableName()).append(" ").append(mtm.getTypeOfPKLinked()).append(SQLStatements.NOT_NULL.getValue())
                    .append(SQLStatements.PK.getValue()).append('(').append(mtm.getForeignKeyToOriginalTableName()).append(", ")
                    .append(mtm.getForeignKeyToLinkedTableName()).append(") )");
            queryMTMList.add(queryCreateTableMTMT.toString());
        }
        for (ManyToMany mtm : table.getMtmAssociations()) {
            queryAlterMTM.append(SQLStatements.ALTER_TABLE.getValue()).append(mtm.getAssociatedTableName())
                    .append(SQLStatements.ADD.getValue()).append(SQLStatements.FK.getValue())
                    .append('(').append(mtm.getForeignKeyToLinkedTableName()).append(')')
                    .append(SQLStatements.REFERENCE.getValue()).append(mtm.getLinkedTableName())
                    .append('(').append(mtm.getPrimaryKeyOfLinkedTableName()).append(");");
            queryMTMList.add(queryAlterMTM.toString());
        }
        for (ManyToMany mtm: table.getMtmAssociations()){
            querySecondCreateTableMTMT.append(SQLStatements.ALTER_TABLE.getValue()).append(mtm.getAssociatedTableName())
                    .append(SQLStatements.ADD).append(SQLStatements.FK.getValue())
                    .append('(').append(mtm.getForeignKeyToOriginalTableName()).append(')')
                    .append(SQLStatements.REFERENCE.getValue()).append(mtm.getOriginalTableName())
                    .append('(').append(mtm.getPrimaryKeyOfOriginalTableName()).append(");");
            queryMTMList.add(querySecondCreateTableMTMT.toString());
        }
        return queryMTMList;
    }

    public String createPKQuery() {
        StringBuilder query = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        query.append(SQLStatements.ALTER_TABLE.getValue()).append(table.getTableName())
                .append(SQLStatements.ADD.getValue());
        if (table.getPrimaryKey().getPKList().size() > 1) {
            query.append(SQLStatements.CONSTRAINT.getValue()).append(table.getCheckConstraint());
        }
        for (Column column : table.getPrimaryKey().getPKList()) {
            Column lastColumn = table.getPrimaryKey().getPKList().get(table.getPrimaryKey().getPKList().size() - 1);
            columnNames.append(column.getName());
            if (!column.equals(lastColumn)) {
                columnNames.append(", ");
            }
        }
        query.append(SQLStatements.PK.getValue()).append('(').append(columnNames).append(')').append(';');
        return query.toString();
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
