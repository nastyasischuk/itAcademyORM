package tablecreation;

import java.lang.reflect.Field;
import java.util.List;

public class TableConstructorImpl implements TableConstructor {
    Class<?> toBuildClass;
    Table table;

    public TableConstructorImpl(Class<?> toBuildClass) {
        //this.table = new Table();
        this.toBuildClass = toBuildClass;
    }

    @Override
    public Table buildTable() {
        //todo new Table
        //todo add all columns to the table
        return null;
    }

    private String getTableName(){
        if(toBuildClass.isAnnotationPresent(annotations.Table.class)){
            return toBuildClass.getAnnotation(annotations.Table.class).name();
        }else{
            return toBuildClass.getName();
        }

    }
    private List<Column> getColumns(){
        Field[] classFields = toBuildClass.getFields();
        return null;
    }
    //todo add check primary and foreign key


}
