package tablecreation;

import customQuery.MarkingChars;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SQLTableQueryCreator {
    private static Logger logger = Logger.getLogger(SQLTableQueryCreator.class);
    private Table table;

    public SQLTableQueryCreator(Table table) {
        this.table = table;
    }

    public String createTableQuery() {
        StringBuilder query = new StringBuilder();
        query.append(SQLStatements.CREATE_TABLE.getValue()).append(MarkingChars.SPACE).append(table.getTableName()).append(MarkingChars.OPEN_BRACKET);
        for (Column column : table.getColumns()) {
            if (column.isForeignKey() && column.getName().equals(findPKName(table.getColumns())))
                continue;
            if (column.isManyToMany())
                continue;

            query.append(column.getName()).append(MarkingChars.SPACE).append(column.getType());
            if (column.getType().equals(SQLTypes.VARCHAR)) {
                query.append(MarkingChars.OPEN_BRACKET).append(column.getType().getLength()).append(MarkingChars.CLOSED_BRACKET);
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
                query.append(SQLStatements.CHECK.getValue()).append(MarkingChars.OPEN_BRACKET).append(table.getCheckConstraint()).append(MarkingChars.CLOSED_BRACKET);
            }

            query.append(MarkingChars.COMMA);
        }

        query.append(SQLStatements.PK.getValue()).append(MarkingChars.OPEN_BRACKET)
                .append(table.getPrimaryKey().getColumnPrimaryKey().getName()).append(MarkingChars.CLOSED_BRACKET);
        query.append(MarkingChars.CLOSED_BRACKET).append(MarkingChars.SEMICOLON);
        if (query.toString().endsWith(MarkingChars.REDUNDANT_COMA))
            query = new StringBuilder(query.toString().replace(MarkingChars.REDUNDANT_COMA, MarkingChars.CLOSE_BRACKET_AND_SEMICOLON));
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
                columns.append(column.getName());
                if (!column.equals(lastColumnInIndex)) {
                    columns.append(MarkingChars.COMMA);
                }
                request.append(MarkingChars.OPEN_BRACKET).append(columns).append(MarkingChars.CLOSED_BRACKET)
                        .append(MarkingChars.SEMICOLON);
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
                        .append(SQLStatements.FK.getValue()).append(MarkingChars.OPEN_BRACKET).append(fk.getForeignKeyName()).append(MarkingChars.CLOSED_BRACKET)
                        .append(SQLStatements.REFERENCE.getValue()).append(fk.getReferenceTableName())
                        .append(MarkingChars.OPEN_BRACKET).append(fk.getReferencePKName()).append(MarkingChars.CLOSED_BRACKET)
                        .append(SQLStatements.ON_DELETE_CASCADE.getValue()).append(MarkingChars.SEMICOLON);
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
                    .append(MarkingChars.OPEN_BRACKET).append(mtm.getForeignKeyToOriginalTableName())
                    .append(MarkingChars.SPACE).append(mtm.getTypeOfPKOriginal()).append(SQLStatements.NOT_NULL.getValue())
                    .append(MarkingChars.COMMA)
                    .append(mtm.getForeignKeyToLinkedTableName()).append(MarkingChars.SPACE).append(mtm.getTypeOfPKLinked())
                    .append(SQLStatements.NOT_NULL.getValue())
                    .append(MarkingChars.COMMA).append(SQLStatements.PK.getValue()).append(MarkingChars.OPEN_BRACKET)
                    .append(mtm.getForeignKeyToOriginalTableName()).append(MarkingChars.COMMA)
                    .append(mtm.getForeignKeyToLinkedTableName()).append(MarkingChars.CLOSED_BRACKET).append(MarkingChars.CLOSED_BRACKET).append(MarkingChars.SEMICOLON);
            queryMTMList.add(queryCreateTableMTMT.toString());
        }
        for (ManyToMany mtm : table.getMtmAssociations()) {
            queryAlterMTM.append(SQLStatements.ALTER_TABLE.getValue()).append(mtm.getAssociatedTableName()).append(MarkingChars.SPACE)
                    .append(SQLStatements.ADD.getValue()).append(SQLStatements.FK.getValue())
                    .append(MarkingChars.OPEN_BRACKET).append(mtm.getForeignKeyToLinkedTableName()).append(MarkingChars.CLOSED_BRACKET)
                    .append(SQLStatements.REFERENCE.getValue()).append(mtm.getLinkedTableName())
                    .append(MarkingChars.OPEN_BRACKET).append(mtm.getPrimaryKeyOfLinkedTableName()).append(MarkingChars.CLOSED_BRACKET)
                    .append(SQLStatements.ON_DELETE_CASCADE.getValue()).append(MarkingChars.SEMICOLON);
            queryMTMList.add(queryAlterMTM.toString());
        }
        for (ManyToMany mtm : table.getMtmAssociations()) {
            querySecondCreateTableMTMT.append(SQLStatements.ALTER_TABLE.getValue()).append(mtm.getAssociatedTableName()).append(MarkingChars.SPACE)
                    .append(SQLStatements.ADD).append(SQLStatements.FK.getValue())
                    .append(MarkingChars.OPEN_BRACKET).append(mtm.getForeignKeyToOriginalTableName()).append(MarkingChars.CLOSED_BRACKET)
                    .append(SQLStatements.REFERENCE.getValue()).append(mtm.getOriginalTableName())
                    .append(MarkingChars.OPEN_BRACKET).append(mtm.getPrimaryKeyOfOriginalTableName()).append(MarkingChars.CLOSED_BRACKET)
                    .append(SQLStatements.ON_DELETE_CASCADE.getValue()).append(MarkingChars.SEMICOLON);
            queryMTMList.add(querySecondCreateTableMTMT.toString());
        }

        return queryMTMList;
    }
}
