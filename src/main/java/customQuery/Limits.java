package customQuery;

import annotations.AnnotationUtils;
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

import static customQuery.commandsForLikeQuery.*;

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
        query.append(getColumnName(name)).append(MarkingChars.EQUALLY)
                .append(MarkingChars.QUOTE).append(value).append(MarkingChars.QUOTE);
        return this;
    }

    public Limits equals(String name, int value) {
        query.append(getColumnName(name)).append(MarkingChars.EQUALLY).append(value);
        return this;
    }

    public Limits equals(String name) {
        query.append(getColumnName(name));
        return this;
    }


    public Limits equals(String name, QueryBuilderImpl queryBuilderImpl) {
        query.append(getColumnName(name)).append(MarkingChars.EQUALLY).append(MarkingChars.OPEN_BRACKET).append(queryBuilderImpl)
                .append(MarkingChars.CLOSED_BRACKET);
        return this;
    }

    Limits in(String... values) {
        query.append(SQLStatements.IN.getValue()).append(MarkingChars.OPEN_BRACKET);
        for (String value : values) {
            String lastElement = values[values.length - 1];
            query.append(MarkingChars.QUOTE).append(value)
                    .append(MarkingChars.QUOTE);
            if (!value.equals(lastElement)) {
                query.append(MarkingChars.COMMA);
            } else {
                break;
            }
        }
        query.append(MarkingChars.CLOSED_BRACKET);
        return this;
    }

    Limits inSubQuery(QueryBuilderImpl subQuery) {
        query.append(SQLStatements.IN.getValue()).append(MarkingChars.OPEN_BRACKET)
                .append(subQuery).append(MarkingChars.CLOSED_BRACKET);
        return this;
    }

    public Limits like(String command, String value) {
        switch (command) {
            case STARTS: {
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.QUOTE)
                        .append(value).append(MarkingChars.PERCENT).append(MarkingChars.QUOTE);
                break;
            }
            case ENDS: {
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.QUOTE)
                        .append(MarkingChars.PERCENT).append(value).append(MarkingChars.QUOTE);
                break;
            }
            case ANY_POSITION: {
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.QUOTE)
                        .append(MarkingChars.PERCENT).append(value).append(MarkingChars.PERCENT).append(MarkingChars.QUOTE);
                break;
            }
        }
        return this;
    }

    public Limits like(String command, String position, String secondValue) {
        StringBuilder underscore = new StringBuilder();
        int index;
        switch (command) {
            case STARTS_AND_ENDS:
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.QUOTE).append(position)
                        .append(MarkingChars.PERCENT).append(secondValue).append(MarkingChars.QUOTE);
                break;
            case POSITION_AFTER:
                index = Integer.parseInt(position) - 1;
                while (index != 0) {
                    underscore.append(MarkingChars.UNDERSCORE);
                    index--;
                }
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.QUOTE).append(underscore)
                        .append(secondValue).append(MarkingChars.PERCENT).append(MarkingChars.QUOTE);
                break;
            case POSITION_BEFORE:
                index = Integer.parseInt(position) - 1;
                while (index != 0) {
                    underscore.append(MarkingChars.UNDERSCORE);
                    index--;
                }
                query.append(SQLStatements.LIKE.getValue()).append(MarkingChars.QUOTE).append(secondValue)
                        .append(underscore).append(MarkingChars.PERCENT).append(MarkingChars.QUOTE);
                break;
        }
        return this;
    }

    public Limits equals(String columnNameFrom,Class fromClassType , String columnNameTo,Class toClassType){
        query.append(getColumnName(columnNameFrom, fromClassType)).append(MarkingChars.EQUALLY).append(getColumnName(columnNameTo, toClassType));
        return this;
    }


    public Limits equals(String columnName, Class fromClassType){
        query.append(getColumnName(columnName, fromClassType));
        return this;
    }

    public Limits between(String valueFrom, String valueTill) {
        query.append(SQLStatements.BETWEEN.getValue()).append(MarkingChars.QUOTE)
                .append(valueFrom).append(MarkingChars.QUOTE).append(SQLStatements.AND.getValue())
                .append(MarkingChars.QUOTE).append(valueTill).append(MarkingChars.QUOTE);
        return this;
    }

    public Limits more(String value) {
        query.append(MarkingChars.SPACE).append(MarkingChars.MORE).append(MarkingChars.SPACE).
                append(value);
        return this;
    }

    public Limits equally(){
        query.append(MarkingChars.EQUALLY);
        return this;
    }

    public Limits moreOrEqually(String value) {
        query.append(MarkingChars.SPACE).append(MarkingChars.MORE).append(MarkingChars.EQUALLY)
                .append(MarkingChars.SPACE).append(value);
        return this;
    }

    public Limits less(String value) {
        query.append(MarkingChars.SPACE).append(MarkingChars.LESS).append(MarkingChars.SPACE).append(value);
        return this;
    }

    public Limits lessOrEqually(String value) {
        query.append(MarkingChars.SPACE).append(MarkingChars.LESS).append(MarkingChars.EQUALLY)
                .append(MarkingChars.SPACE).append(value);
        return this;
    }

    public Limits notEqually(String value) {
        query.append(MarkingChars.SPACE).append(MarkingChars.LESS).append(MarkingChars.MORE)
                .append(MarkingChars.SPACE).append(value);
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
        QueryBuilderImpl queryBuilderImpl = new QueryBuilderImpl(classType);
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
        return lane.append(AnnotationUtils.getTableName(classType)).append(MarkingChars.DOT).append(column.getName()).toString();
    }

    String build() {
        return query.toString();
    }

    public Limits builder() {
        return new Limits(classType);
    }

}
