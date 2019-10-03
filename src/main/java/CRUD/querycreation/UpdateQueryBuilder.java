package CRUD.querycreation;

import CRUD.requests.MarkingChars;
import CRUD.rowhandler.RowToDB;

import tablecreation.SQLStatements;

import java.util.Iterator;
import java.util.Map;

public class UpdateQueryBuilder extends QueryBuilder {
    private RowToDB row;

    public UpdateQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNamesAndColumnValues = new StringBuilder();
        Iterator columnNamesIterator = row.getMap().keySet().iterator();
        String lastColumnNameIterator = null;

        request.append(SQLStatements.UPDATE.getValue()).append(MarkingChars.space).append(row.getTableName())
                .append(SQLStatements.SET).append(columnNamesAndColumnValues);

        while (columnNamesIterator.hasNext()) {
            lastColumnNameIterator = String.valueOf(columnNamesIterator.next());

        }

        for (Map.Entry<String, String> pair : row.getMap().entrySet()) {
            columnNamesAndColumnValues.append(pair.getKey()).append(MarkingChars.equally)
                    .append(MarkingChars.quote).append(pair.getValue()).append(MarkingChars.quote);
            if (!pair.getKey().equals(lastColumnNameIterator)) {
                columnNamesAndColumnValues.append(MarkingChars.comma);
            }
            request.append(SQLStatements.WHERE.getValue()).append(row.getIdName()).append(MarkingChars.equally).append(row.getIdValue()).append(MarkingChars.semicolon);
            return request.toString();
        }

        return request.toString();
    }
}
