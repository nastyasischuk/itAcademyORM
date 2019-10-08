package CRUD.querycreation;



import CRUD.rowhandler.RowToDB;
import customQuery.MarkingChars;
import tablecreation.SQLStatements;

import java.util.Iterator;
import java.util.Map;

public class InsertQueryBuilder extends QueryBuilder {

    InsertQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder columnValues = new StringBuilder();
        Iterator columnNamesIterator = row.getMap().keySet().iterator();
        String lastColumnNameIterator = null;

        request.append(SQLStatements.INSERT.getValue()).append(SQLStatements.INTO.getValue())
                .append(row.getTableName()).append(MarkingChars.SPACE);

        while (columnNamesIterator.hasNext()) {
            lastColumnNameIterator = String.valueOf(columnNamesIterator.next());

        }
        if (!row.isAutoIncrement()) {
            columnNames.append(row.getIdName()).append(MarkingChars.COMMA);
            columnValues.append(MarkingChars.QUOTE).append(row.getIdValue()).append(MarkingChars.QUOTE).append(MarkingChars.COMMA);
        }
        for (Map.Entry<String, String> pair : row.getMap().entrySet()) {
            columnNames.append(pair.getKey());
            columnValues.append(MarkingChars.QUOTE).append(pair.getValue()).append(MarkingChars.QUOTE);
            if (!pair.getKey().equals(lastColumnNameIterator)) {
                columnNames.append(MarkingChars.COMMA);
                columnValues.append(MarkingChars.COMMA);
            }
        }

        request.append(MarkingChars.OPEN_BRACKET).append(columnNames).append(MarkingChars.CLOSED_BRACKET).
                append(SQLStatements.VALUES.getValue()).append(MarkingChars.SPACE).append(MarkingChars.OPEN_BRACKET)
                .append(columnValues).append(MarkingChars.CLOSED_BRACKET).append(MarkingChars.SEMICOLON);
        return request.toString();
    }
}
