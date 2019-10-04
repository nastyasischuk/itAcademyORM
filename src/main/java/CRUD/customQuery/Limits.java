package CRUD.customQuery;

import annotations.Column;
import exceptions.NoPrimaryKeyException;
import exceptions.WrongColumnNameException;
import exceptions.WrongSQLType;
import org.apache.log4j.Logger;
import tablecreation.ColumnConstructor;
import tablecreation.SQLStatements;

import java.lang.reflect.Field;

public class Limits  {
    private Logger logger = Logger.getLogger(Limits.class);
    private StringBuilder query;
    private Class classType;

    public Limits(Class<?> classType) {
        this.classType = classType;
        query = new StringBuilder();
    }
    public StringBuilder getQuery() {
        return query;
    }


    public Limits equals(String name, String value) {
        query.append(createColumn(name)).append(MarkingChars.equally)
                .append(MarkingChars.quote).append(value).append(MarkingChars.quote);
        return this;
    }

    public Limits equals(String name) {

        query.append(createColumn(name));
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

    public Limits inSubQuery(SubQuery subQuery, Limits limits) {
         query.append(SQLStatements.IN.getValue()).append(MarkingChars.openBracket)
                .append(subQuery.build()).append(limits.build()).append(MarkingChars.closedBracket);
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

    public Limits moreOrEqual(String value) {
        query.append(MarkingChars.space).append(MarkingChars.more).append(MarkingChars.equally)
                .append(MarkingChars.space).append(value);
        return this;
    }

    public Limits less(String value) {
        query.append(MarkingChars.space).append(MarkingChars.less).append(MarkingChars.space).append(value);
        return this;
    }

    public Limits lessOrEqual(String value) {
        query.append(MarkingChars.space).append(MarkingChars.less).append(MarkingChars.equally)
                .append(MarkingChars.space).append(value);
        return this;
    }

    public Limits notEqual(String value) {
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

    protected String createColumn(String name) {
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
        return column.getName();
    }

    @Override
    public String toString() {
        return query.toString();
    }

    public String build() {
        return query.toString();
    }

    public Limits builder(){
        return new Limits(classType);
    }

}
