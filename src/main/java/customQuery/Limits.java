package customQuery;

import annotations.Column;
import exceptions.NoPrimaryKeyException;
import exceptions.WrongColumnNameException;
import exceptions.WrongSQLType;
import org.apache.log4j.Logger;
import tablecreation.ColumnConstructor;
import tablecreation.SQLStatements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Limits {
    private Logger logger = Logger.getLogger(Limits.class);
    private StringBuilder query;
    private Class classType;

    Limits(Class<?> classType) {
        this.classType = classType;
        query = new StringBuilder();
    }

    public StringBuilder getQuery() {
        return query;
    }


    public Limits equals(String name, String value) {
        query.append(getColumnName(name)).append(MarkingChars.equally)
                .append(MarkingChars.quote).append(value).append(MarkingChars.quote);
        return this;
    }

    public Limits equals(String name, int value) {
        query.append(getColumnName(name)).append(MarkingChars.equally).append(value);
        return this;
    }

    public Limits equals(String name) {
        query.append(getColumnName(name));
        return this;
    }


    public Limits equals(String name, QueryImpl queryImpl) {
        query.append(getColumnName(name)).append(MarkingChars.equally).append(MarkingChars.openBracket).append(queryImpl)
                .append(MarkingChars.closedBracket);
        return this;
    }

    public Limits in(String... values) {
        query.append(SQLStatements.IN.getValue()).append(MarkingChars.openBracket);
        for (String value : values) {
            String lastElement = values[values.length - 1];
            query.append(MarkingChars.quote).append(value)
                    .append(MarkingChars.quote);
            if (!value.equals(lastElement)) {
                query.append(MarkingChars.comma);
            } else {
                break;
            }
        }
        query.append(MarkingChars.closedBracket);
        return this;
    }

    public Limits inSubQuery(QueryImpl subQuery) {
        query.append(SQLStatements.IN.getValue()).append(MarkingChars.openBracket)
                .append(subQuery).append(MarkingChars.closedBracket);
        return this;
    }

    public Limits like(String command, String value) {
        switch (command) {
            case "starts": {
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.quote)
                        .append(value).append(MarkingChars.percent).append(MarkingChars.quote);
                break;
            }
            case "ends": {
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.quote)
                        .append(MarkingChars.percent).append(value).append(MarkingChars.quote);
                break;
            }
            case "any position": {
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.quote)
                        .append(MarkingChars.percent).append(value).append(MarkingChars.percent).append(MarkingChars.quote);
                break;
            }
        }
        return this;
    }

    public Limits like(String command, String position, String secondValue) {
        StringBuilder underscore = new StringBuilder();
        int index;
        switch (command) {
            case "starts and ends":
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.quote).append(position)
                        .append(MarkingChars.percent).append(secondValue).append(MarkingChars.quote);
                break;
            case "position after":
                index = Integer.parseInt(position) - 1;
                while (index != 0) {
                    underscore.append(MarkingChars.underscore);
                    index--;
                }
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.quote).append(underscore)
                        .append(secondValue).append(MarkingChars.percent).append(MarkingChars.quote);
                break;
            case "position before":
                index = Integer.parseInt(position) - 1;
                while (index != 0) {
                    underscore.append(MarkingChars.underscore);
                    index--;
                }
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.quote).append(secondValue)
                        .append(underscore).append(MarkingChars.percent).append(MarkingChars.quote);
                break;
        }
        return this;
    }

    public Limits equals(String columnNameFrom,Class fromClassType , String columnNameTo,Class toClassType){
        query.append(getColumnName(columnNameFrom, fromClassType)).append(MarkingChars.equally).append(getColumnName(columnNameTo, toClassType));
        return this;
    }


    public Limits equals(String columnName, Class fromClassType){
        query.append(getColumnName(columnName, fromClassType));
        return this;
    }

    public Limits between(String valueFrom, String valueTill) {
        query.append(SQLStatements.BETWEEN.getValue()).append(MarkingChars.quote)
                .append(valueFrom).append(MarkingChars.quote).append(SQLStatements.AND.getValue())
                .append(MarkingChars.quote).append(valueTill).append(MarkingChars.quote);
        return this;
    }

    public Limits more(String value) {
        query.append(MarkingChars.space).append(MarkingChars.more).append(MarkingChars.space).
                append(value);
        return this;
    }

    public Limits equally(){
        query.append(MarkingChars.equally);
        return this;
    }

    public Limits moreOrEqually(String value) {
        query.append(MarkingChars.space).append(MarkingChars.more).append(MarkingChars.equally)
                .append(MarkingChars.space).append(value);
        return this;
    }

    public Limits less(String value) {
        query.append(MarkingChars.space).append(MarkingChars.less).append(MarkingChars.space).append(value);
        return this;
    }

    public Limits lessOrEqually(String value) {
        query.append(MarkingChars.space).append(MarkingChars.less).append(MarkingChars.equally)
                .append(MarkingChars.space).append(value);
        return this;
    }

    public Limits notEqually(String value) {
        query.append(MarkingChars.space).append(MarkingChars.less).append(MarkingChars.more)
                .append(MarkingChars.space).append(value);
        return this;
    }

    public Limits and() {
        query.append(SQLStatements.AND.getValue());
        return this;
    }

    public Limits or() {
        StringBuilder lane = new StringBuilder();
        query.append(lane.append(SQLStatements.OR.getValue()));
        return this;
    }

    public Limits is() {
        StringBuilder lane = new StringBuilder();
        query.append(lane.append(SQLStatements.IS.getValue()));
        return this;
    }

    public Limits nuLL() {
        StringBuilder lane = new StringBuilder();
        query.append(lane.append(SQLStatements.NULL.getValue()));
        return this;
    }

    public Limits not() {
        query.append(SQLStatements.NOT.getValue());
        return this;
    }

    protected String getColumnName(String name) {
        tablecreation.Column column = null;
        Field[] fields = classType.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                if (field.getName().equals(name)) {
                    try {
                        column = new ColumnConstructor(field).buildColumn();
                    } catch (NoPrimaryKeyException | WrongSQLType | WrongColumnNameException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        return column != null ? column.getName() : null;
    }

    protected List<String> getAllColumnNames(Class typeOfClass){
        List<String> allColumnNames = new ArrayList<>();
        tablecreation.Column column;
        Field[] fields = typeOfClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                    try {
                        column = new ColumnConstructor(field).buildColumn();
                        allColumnNames.add(column.getName());
                    } catch (NoPrimaryKeyException | WrongSQLType | WrongColumnNameException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        return allColumnNames;
    }

    protected String getColumnName(String name, Class classType) {
        StringBuilder lane = new StringBuilder();
        QueryImpl queryImpl = new QueryImpl(classType);
        tablecreation.Column column = null;
        Field[] fields = classType.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                if (field.getName().equals(name)) {
                    try {
                        column = new ColumnConstructor(field).buildColumn();
                    } catch (NoPrimaryKeyException | WrongSQLType | WrongColumnNameException e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        assert column != null;
        return lane.append(queryImpl.getFromClassTableName(classType)).append(MarkingChars.dot).append(column.getName()).toString();
    }

    String build() {
        return query.toString();
    }

    public Limits builder() {
        return new Limits(classType);
    }

}
