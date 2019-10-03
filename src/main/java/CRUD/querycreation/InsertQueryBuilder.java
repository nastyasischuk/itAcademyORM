package CRUD.querycreation;

import CRUD.rowhandler.RowToDB;
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

        request.append(SQLStatements.INSERT.getValue()).append(SQLStatements.INTO.getValue()).append(row.getTableName()).append(" ");

        while (columnNamesIterator.hasNext()) {
            lastColumnNameIterator = String.valueOf(columnNamesIterator.next());

        }
        if (!isAI) { //TODO change a little
            columnNames.append(row.getIdName()).append(", ");
            columnValues.append("'").append(row.getIdValue()).append("'").append(", ");
        }
        for (Map.Entry<String, String> pair : row.getMap().entrySet()) {
//            if (pair.getKey().equals(row.getIdName()) && isAI) {
//                break;
                columnNames.append(pair.getKey());
                columnValues.append("'").append(pair.getValue()).append("'");
                if (!pair.getKey().equals(lastColumnNameIterator)) {
                    columnNames.append(", ");
                    columnValues.append(", ");
                }

        }
        request.append('(').append(columnNames).append(')').append(SQLStatements.VALUES.getValue()).append(" ");
        request.append('(').append(columnValues).append(')').append(';');

        System.out.println(request);
        return request.toString();
    }

}
