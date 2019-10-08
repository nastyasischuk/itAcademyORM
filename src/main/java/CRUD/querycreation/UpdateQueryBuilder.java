package CRUD.querycreation;


import CRUD.rowhandler.RowToDB;

import customQuery.MarkingChars;
import tablecreation.SQLStatements;

import java.util.Iterator;
import java.util.Map;

public class UpdateQueryBuilder extends QueryBuilder {
    UpdateQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNamesAndColumnValues = new StringBuilder();
        Iterator columnNamesIterator = row.getMap().keySet().iterator();
        String lastColumnNameIterator = null;

        request.append(SQLStatements.UPDATE.getValue()).append(MarkingChars.SPACE).append(row.getTableName())
                .append(SQLStatements.SET.getValue());

        while (columnNamesIterator.hasNext()) {
            lastColumnNameIterator = String.valueOf(columnNamesIterator.next());
        }

        for (Map.Entry<String, String> pair : row.getMap().entrySet()) {
            columnNamesAndColumnValues.append(pair.getKey()).append(MarkingChars.EQUALLY).append(MarkingChars.SPACE)
                    .append(MarkingChars.QUOTE).append(pair.getValue()).append(MarkingChars.QUOTE);
            if (!pair.getKey().equals(lastColumnNameIterator)) {
                columnNamesAndColumnValues.append(MarkingChars.COMMA);
            }
        }
        request.append(columnNamesAndColumnValues);
        request.append(SQLStatements.WHERE.getValue()).append(row.getIdName()).append(MarkingChars.EQUALLY)
                .append(row.getIdValue()).append(MarkingChars.SEMICOLON);

        return request.toString();
    }
}
