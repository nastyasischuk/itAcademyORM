package customQuery;

import annotations.Column;
import exceptions.NoPrimaryKeyException;
import exceptions.WrongColumnNameException;
import exceptions.WrongSQLType;
import org.apache.log4j.Logger;
import tablecreation.ColumnConstructor;
import tablecreation.SQLStatements;
import java.lang.reflect.Field;

public class Aggregation {
    private Logger logger = Logger.getLogger(Aggregation.class);
    private StringBuilder query = new StringBuilder();
    private Class classType;

    Aggregation(Class classType) {
        this.classType = classType;
    }

    public Aggregation count(String columnName, String inEachColumn) {
        query.append(SQLStatements.COUNT.getValue()).append(MarkingChars.openBracket)
                .append(createColumn(columnName).getName()).append(MarkingChars.closedBracket)
                .append(MarkingChars.comma).append(createColumn(inEachColumn).getName());
        return this;
    }
    public Aggregation count(String columnName) {
        query.append(SQLStatements.COUNT.getValue()).append(MarkingChars.openBracket)
                .append(createColumn(columnName).getName()).append(MarkingChars.closedBracket);
        return this;
    }

    public Aggregation selectMin(String columnName) {
        query.append(SQLStatements.MIN.getValue()).append(MarkingChars.openBracket)
                .append(createColumn(columnName).getName()).append(MarkingChars.closedBracket);
        return this;
    }

    public Aggregation selectMax(String columnName) {
        query.append(SQLStatements.MAX.getValue()).append(MarkingChars.openBracket)
                .append(createColumn(columnName).getName()).append(MarkingChars.closedBracket);
        return this;
    }

    public Aggregation avg(String columnName) {
        query.append(SQLStatements.AVG.getValue()).append(MarkingChars.openBracket)
                .append(createColumn(columnName).getName()).append(MarkingChars.closedBracket);
        return this;
    }

    public Aggregation sum(String columnName) {
        query.append(SQLStatements.SUM.getValue()).append(MarkingChars.openBracket)
                .append(createColumn(columnName).getName()).append(MarkingChars.closedBracket);
        return this;
    }


    private tablecreation.Column createColumn(String name) {
        tablecreation.Column column = null;
        Field[] fields = classType.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                if (field.getName().equals(name)) {
                    try {
                        column = new ColumnConstructor(field).buildColumn();
                    } catch (NoPrimaryKeyException | WrongSQLType | WrongColumnNameException e) {
                        logger.error(e,e.getCause());
                    }
                }
            }
        }
        return column;
    }

    String build(){
        return query.toString();
    }

    Aggregation builder(){
        return new Aggregation(classType);
    }
}
