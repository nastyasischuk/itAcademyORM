package CRUD.querycreation;

import CRUD.rowhandler.Row;
import CRUD.rowhandler.RowToDB;
import tablecreation.Column;
import tablecreation.SQLStatements;

import java.util.Iterator;
import java.util.Map;

public class InsertQueryBuilder extends QueryBuilder {
    private RowToDB row;

    public InsertQueryBuilder(RowToDB row) {
        super(row);
    }

    @Override
    public String buildQuery() {
        StringBuilder request = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder columnValues = new StringBuilder();
        Iterator columnNamesIterator = row.getMap().keySet().iterator();
        String lastColumnNameIterator = null;

        request.append(SQLStatements.INSERT.getValue()).append(SQLStatements.INTO.getValue()).append(row.getTableName()).append(" ");

        while (columnNamesIterator.hasNext()) {
            lastColumnNameIterator = String.valueOf(columnNamesIterator.next());

        }
        for (Map.Entry<String, String> pair : row.getMap().entrySet()) {
            if (pair.getKey().equals(row.getIdName())) {
                break;
            } else {
                columnNames.append(pair.getKey());
                columnValues.append(pair.getValue());
                if (!pair.getKey().equals(lastColumnNameIterator)) {
                    columnNames.append(", ");
                    columnValues.append(", ");
                }
            }
        }
        request.append('(').append(columnNames).append(')').append(SQLStatements.VALUES.getValue()).append(" ");
        request.append('(').append(columnValues).append(')').append(';');

        return request.toString();
    }
}
