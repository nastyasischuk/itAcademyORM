package CRUDdb.querycreation;


import CRUDdb.requests.MarkingChars;
import CRUDdb.rowhandler.RowToDB;
import tablecreation.SQLStatements;

import java.util.Iterator;
import java.util.Map;

public class InsertQueryBuilder extends QueryBuilder {

    public InsertQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        boolean isAI = row.isAutoIncrement();
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
        if (!isAI) { //TODO change a little
            columnNames.append(row.getIdName()).append(", ");
            columnValues.append("'").append(row.getIdValue()).append("'").append(", ");
        }
        for (Map.Entry<String, String> pair : row.getMap().entrySet()) {
                columnNames.append(pair.getKey());
                columnValues.append("'").append(pair.getValue()).append("'");
                if (!pair.getKey().equals(lastColumnNameIterator)) {
                    columnNames.append(", ");
                    columnValues.append(", ");
                }
        }

        request.append(MarkingChars.openBracket).

                append(columnNames).

                append(MarkingChars.closedBracket).

                append(SQLStatements.VALUES.getValue()).

                append(MarkingChars.space);
        request.append(MarkingChars.openBracket).

                append(columnValues).

                append(MarkingChars.closedBracket).

                append(MarkingChars.semicolon);

        return request.toString();
    }

}
