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
                .append(row.getTableName()).append(MarkingChars.space);

        while (columnNamesIterator.hasNext()) {
            lastColumnNameIterator = String.valueOf(columnNamesIterator.next());

        }
        if (!row.isAutoIncrement()) {
            columnNames.append(row.getIdName()).append(MarkingChars.comma);
            columnValues.append(MarkingChars.singleQuote).append(row.getIdValue()).append(MarkingChars.singleQuote).append(MarkingChars.comma);
        }
        for (Map.Entry<String, String> pair : row.getMap().entrySet()) {
            columnNames.append(pair.getKey());
            columnValues.append(MarkingChars.singleQuote).append(pair.getValue()).append(MarkingChars.singleQuote);
            if (!pair.getKey().equals(lastColumnNameIterator)) {
                columnNames.append(MarkingChars.comma);
                columnValues.append(MarkingChars.comma);
            }
        }

        request.append(MarkingChars.openBracket).append(columnNames).append(MarkingChars.closedBracket).
                append(SQLStatements.VALUES.getValue()).append(MarkingChars.space).append(MarkingChars.openBracket)
                .append(columnValues).append(MarkingChars.closedBracket).append(MarkingChars.semicolon);
        return request.toString();
    }
}
