package tablecreation;

import exceptions.NoPrimaryKeyException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableConstructorImpl implements TableConstructor {
    Class<?> toBuildClass;
    Table table;


    public TableConstructorImpl(Class<?> toBuildClass) {
        this.table = new Table(getTableName());
        this.toBuildClass = toBuildClass;
    }

    @Override
    public Table buildTable() {
       table.setColumns(getColumns());
        return table;
    }

    private String getTableName(){
        if(toBuildClass.isAnnotationPresent(annotations.Table.class)){
            return toBuildClass.getAnnotation(annotations.Table.class).name();
        }else{
            return toBuildClass.getSimpleName();
        }

    }
    private List<Column> getColumns(){
        Field[] classFields = toBuildClass.getFields();
        List<Column> columns  = new ArrayList<>();
        for(int i=0;i<classFields.length;i++){
            Column builtColumn = new ColumnConstructor(classFields[i]).buildColumn();
            columns.add(builtColumn);
            if(builtColumn.isForeignKey()){
            formFK();
            }
            if(builtColumn.isPrimaryKey()){
                formPK();
            }
        }

        return columns;
    }
   private PrimaryKey formPK(){

        return null;
   }
   private ForeignKey formFK(){

        return null;
   }
   private void checkIfPrimaryKeyPresent(){
        if(table.getPrimaryKey()==null)
            throw new NoPrimaryKeyException();
   }


}
