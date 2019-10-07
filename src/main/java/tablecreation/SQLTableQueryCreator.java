package tablecreation;




import customQuery.MarkingChars;

import java.util.ArrayList;
import java.util.List;

public class SQLTableQueryCreator {
    private Table table;

    public SQLTableQueryCreator(Table table) {
        this.table = table;
    }

    public String createTableQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.CREATE_TABLE.getValue()).append(MarkingChars.space).append(table.getTableName()).append(MarkingChars.openBracket);
        for (Column column : table.getColumns()) {
            if (column.isForeignKey() && column.getName().equals(findPKName(table.getColumns())))
                continue;
            if (column.isManyToMany())
                continue;

            query.append(column.getName()).append(MarkingChars.space).append(column.getType());
            if (column.getType().equals(SQLTypes.VARCHAR)) {
                query.append(MarkingChars.openBracket).append(column.getType().getLength()).append(MarkingChars.closedBracket);
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
                query.append(SQLStatements.CHECK.getValue()).append(MarkingChars.openBracket).append(table.getCheckConstraint()).append(MarkingChars.closedBracket);
            }

            query.append(MarkingChars.comma);
        }

        query.append(SQLStatements.PK.getValue()).append(MarkingChars.openBracket)
                .append(table.getPrimaryKey().getColumnPrimaryKey().getName()).append(MarkingChars.closedBracket);
        query.append(MarkingChars.closedBracket).append(MarkingChars.semicolon);
        if (query.toString().endsWith(MarkingChars.redundantComa))
            query = new StringBuilder(query.toString().replace(MarkingChars.redundantComa, MarkingChars.closeBracketAndSemicolon));
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
                    columns.append(MarkingChars.comma);
                }
                request.append(MarkingChars.openBracket).append(columns).append(MarkingChars.closedBracket)
                        .append(MarkingChars.semicolon);
                queryIndexList.add(request.toString());
            }
        }
        return queryIndexList;
    }

    public List<String> createFKQuery() {
        List<String> queryFKList = new ArrayList<>();
        StringBuilder request = new StringBuilder();
        for (ForeignKey fk : table.getForeignKeys())
            if (!table.getForeignKeys().isEmpty()) {
                request.append(SQLStatements.ALTER_TABLE.getValue()).append(fk.getTableName())
                        .append(SQLStatements.ADD.getValue()).append(SQLStatements.CONSTRAINT.getValue()).append(fk.getConstructionName())
                        .append(SQLStatements.FK.getValue()).append(MarkingChars.openBracket).append(fk.getForeignKeyName()).append(MarkingChars.closedBracket)
                        .append(SQLStatements.REFERENCE.getValue()).append(fk.getReferenceTableName())
                        .append(MarkingChars.openBracket).append(fk.getReferencePKName()).append(MarkingChars.closedBracket)
                        .append(SQLStatements.ON_DELETE_CASCADE.getValue()).append(MarkingChars.semicolon);
                queryFKList.add(request.toString());
            }
        return queryFKList;
    }

    public List<String> createManyToManyQuery() {
        List<String> queryMTMList = new ArrayList<>();
        StringBuilder queryAlterMTM = new StringBuilder();
        StringBuilder queryCreateTableMTMT = new StringBuilder();
        StringBuilder querySecondCreateTableMTMT = new StringBuilder();
        for (ManyToMany mtm : table.getMtmAssociations()) {
            queryCreateTableMTMT.append(SQLStatements.CREATE_TABLE.getValue()).append(mtm.getAssociatedTableName())
                    .append(MarkingChars.openBracket).append(mtm.getForeignKeyToOriginalTableName())
                    .append(MarkingChars.space).append(mtm.getTypeOfPKOriginal()).append(SQLStatements.NOT_NULL.getValue())
                    .append(MarkingChars.comma)
                    .append(mtm.getForeignKeyToLinkedTableName()).append(MarkingChars.space).append(mtm.getTypeOfPKLinked())
                    .append(SQLStatements.NOT_NULL.getValue())
                    .append(MarkingChars.comma).append(SQLStatements.PK.getValue()).append(MarkingChars.openBracket)
                    .append(mtm.getForeignKeyToOriginalTableName()).append(MarkingChars.comma)
                    .append(mtm.getForeignKeyToLinkedTableName()).append(MarkingChars.closedBracket).append(MarkingChars.closedBracket).append(MarkingChars.semicolon);
            queryMTMList.add(queryCreateTableMTMT.toString());
        }
        for (ManyToMany mtm : table.getMtmAssociations()) {
            queryAlterMTM.append(SQLStatements.ALTER_TABLE.getValue()).append(mtm.getAssociatedTableName()).append(MarkingChars.space)
                    .append(SQLStatements.ADD.getValue()).append(SQLStatements.FK.getValue())
                    .append(MarkingChars.openBracket).append(mtm.getForeignKeyToLinkedTableName()).append(MarkingChars.closedBracket)
                    .append(SQLStatements.REFERENCE.getValue()).append(mtm.getLinkedTableName())
                    .append(MarkingChars.openBracket).append(mtm.getPrimaryKeyOfLinkedTableName()).append(MarkingChars.closedBracket)
                    .append(SQLStatements.ON_DELETE_CASCADE.getValue()).append(MarkingChars.semicolon);
            queryMTMList.add(queryAlterMTM.toString());
        }
        for (ManyToMany mtm : table.getMtmAssociations()) {
            querySecondCreateTableMTMT.append(SQLStatements.ALTER_TABLE.getValue()).append(mtm.getAssociatedTableName()).append(MarkingChars.space)
                    .append(SQLStatements.ADD).append(SQLStatements.FK.getValue())
                    .append(MarkingChars.openBracket).append(mtm.getForeignKeyToOriginalTableName()).append(MarkingChars.closedBracket)
                    .append(SQLStatements.REFERENCE.getValue()).append(mtm.getOriginalTableName())
                    .append(MarkingChars.openBracket).append(mtm.getPrimaryKeyOfOriginalTableName()).append(MarkingChars.closedBracket)
                    .append(SQLStatements.ON_DELETE_CASCADE.getValue()).append(MarkingChars.semicolon);
            queryMTMList.add(querySecondCreateTableMTMT.toString());
        }

        return queryMTMList;
    }
}
